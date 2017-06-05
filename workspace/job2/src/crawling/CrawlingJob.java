package crawling;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.KeywordContents;

import org.apache.log4j.Logger;

public class CrawlingJob {
	
	private static final Logger logger = Logger.getLogger(CrawlingJob.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws CallException{
		//DB 객체 선언
		KeywordDao keywordDao = new KeywordDao();

		// 수집기 선언
		KeywordContents cont = new KeywordContents();

		logger.debug("carwling start");
        cont.setCollector_ip("localhost");
        
        // 수집기 정보 가져오기
		try {
			cont = keywordDao.getcollectorConfig(cont);
		} catch (Exception e) {
			String msg = "수집기 데이터 가져오기 오류";
			throw new CallException(e, msg);
		}
		
		// 수집 날짜 설정 
		logger.debug("start_date ====> " + cont.getBeginDate_s());
		logger.debug("end_date ====> " + cont.getEndDate_s());
		
		// 기간안에 수집된 데이터가 있는지 체크 - 즉 중간에 멈추어 졌는지 판단한다.
		KeywordContents keywordCont = null;

		logger.debug(cont.getBeginDate_s());
		logger.debug(cont.getEndDate_s());
		try {
			keywordCont = keywordDao.getDataCnt(cont);
		} catch (Exception e1) {
			String msg = "기간안에 수집된 데이터 체크 오류";
			throw new CallException(e1, msg);
		}
		
		ArrayList<HashMap<String, String>> keywordList = null;
		ArrayList<HashMap<String, String>> continueList = null;

		int keywordCount = 0;
		int continueCount = 0;

		//수집기가 중단된후 다시 재 수집 시작시
		if (keywordCont.getKeyword_id() != null) {
			logger.debug(keywordCont.getCollecting_date() + "부터 수집기가 중단되었습니다.");
			logger.debug("중단된 키워드는 " + keywordCont.getKeyword_id() + " 입니다.");
			
			// 중단된 날짜의 키워드가 마지막인지 판단하여 마지막이면 다음날짜 부터 수집
			try {
				if (keywordCont.getKeyword_id().equals(keywordDao.getLastKeywordId(cont.getCollector_name()))) {
					
					logger.debug("마지막 키워드까지 수집이 되었습니다.");
					int nextDate = Integer.parseInt(keywordCont.getCollecting_date()) + 1;
					
					if (Integer.parseInt(cont.getEndDate_s()) > nextDate) {
						cont.setBeginDate_s(String.valueOf(nextDate));
					}

				} else {
					// 수집이 중단되었다면 중단된 키워드 이후로 부터의 키워드 리스트
					logger.debug("중간에서 키워드까지 수집이 멈추었습니다.");
					continueList = keywordDao.getKeywordContinueList(cont.getCollector_name(), keywordCont.getCollecting_date());
					continueCount = continueList.size();
					cont.setBeginDate_s(cont.getCollecting_date());
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.debug("재시작 날짜는" + cont.getBeginDate_s());
		}
		
		//시작날짜와 끝나는 날짜가 같지 않거나 끝나는 날과 같아도 중단된 키워드 값이 있으면 수집시작
		if (cont.getCollecting_date() == null || !cont.getEndDate_s().equals(cont.getCollecting_date()) || !keywordCont.getKeyword_id().equals("K0233555")) {

			// 키워드 전체 리스트
			try {
				keywordList = keywordDao.getKeywordList(cont.getCollector_name());
			} catch (Exception e) {
				String msg = "키워드 전체 리스트 오류";
				throw new CallException(e, msg);
			}
			
			keywordCount = keywordList.size();

			int crawlingKeywordCnt = 0;

			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

			Date beginDate = new Date(); // 시작일자
			Date endDate = new Date(); // 종료일자
			long diff;
			int diffDays = 0; // 불러올 파일 일 수

			try {
				beginDate = formatter.parse(cont.getBeginDate_s());
				endDate = formatter.parse(cont.getEndDate_s());
				diff = endDate.getTime() - beginDate.getTime();
				diffDays = (int) (diff / (24 * 60 * 60 * 1000));
			} catch (Exception e) {
				String msg = "날짜 계산 오류";
				throw new CallException(e, msg);
			}
			
			GregorianCalendar cal = new GregorianCalendar();

			cal.setTime(beginDate);
			cal.add(cal.DAY_OF_MONTH, -1);

			for (int j = 0; j <= diffDays; j++) {

				cal.add(cal.DAY_OF_MONTH, 1);

				Date run_date = cal.getTime();

				String run_date_s = formatter.format(run_date);

				// 데이터 insert를 위한 hashMap
				List<Map<Integer, String>> crawlingData = new ArrayList<Map<Integer, String>>();

				// 키워드가 중간에 시작될 경우 for문 카운터
				crawlingKeywordCnt = continueCount;
				if (keywordCont.getKeyword_id() != null && j == 0) {
					crawlingKeywordCnt = continueCount;
				} else {
					crawlingKeywordCnt = keywordCount;
				}
				
				for (int i = 0; i < crawlingKeywordCnt; i++) {

					// 과거데이터 돌릴때 사용
					int day = j;
					HashMap<String, String> dataList = null;
					
					if (keywordCont.getKeyword_id() != null && day == 0) {
						dataList = continueList.get(i);
					}else{
						dataList = keywordList.get(i);
					}
					 
					String word = null;
					try {
						word = URLEncoder.encode(dataList.get("keywordNm")+"", "UTF-8");
					} catch (Exception e1) {
						String msg = "인코딩 오류";
						throw new CallException(e1, msg);
					}
					
					//CC0005 - 네이버, CC0006 - 다음, CC0007 - 블로그, CC0008 - 카페
					//수집되는 키워드
					String[] params1 = { "CC0005", "100", run_date_s, "CC0007", word, dataList.get("keywordNm").toString(), "CC0005_CC0007_" + dataList.get("keywordId") + "_" + run_date_s, "Y", dataList.get("keywordId").toString() };
					String[] params2 = { "CC0005", "100", run_date_s, "CC0008", word, dataList.get("keywordNm").toString(), "CC0005_CC0008_" + dataList.get("keywordId") + "_" + run_date_s, "N", dataList.get("keywordId").toString() };
					String[] params3 = { "CC0006", "100", run_date_s, "CC0007", word, dataList.get("keywordNm").toString(), "CC0006_CC0007_" + dataList.get("keywordId") + "_" + run_date_s, "Y", dataList.get("keywordId").toString() };
					String[] params4 = { "CC0006", "100", run_date_s, "CC0008", word, dataList.get("keywordNm").toString(), "CC0006_CC0008_" + dataList.get("keywordId") + "_" + run_date_s, "Y", dataList.get("keywordId").toString() };
					 
				try {
					 // 사이트 블럭을 막기위한 인터벌 // Thread.sleep(30000);
					 Thread.sleep(1000);
				 
					 SaveListService slm = new SaveListService();
					 crawlingData.add(slm.saveList(params1));
					 crawlingData.add(slm.saveList(params2));
					 crawlingData.add(slm.saveList(params3));
					 crawlingData.add(slm.saveList(params4));
					 
					} catch (Exception e) {
						String msg = "데이터 수집 에러";
						throw new CallException(crawlingData, msg);
					}
				}
				
				/*try {
					keywordDao.setKeywordData(crawlingData);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
										
			}
		}
		logger.debug("수집 중단");
	}
}
