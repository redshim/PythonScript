package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import dao.UrlParserDAO;
import model.PageVO;
import util.CallException;
import util.TextSave;

/**
 * @Author 6546788
 * @Date 2017. 4. 24.
 * @Version : 
 */
public class UrlParserService {
	
	private UrlParserDAO urlParserDAO = new UrlParserDAO();
	
	private TextSave ts = new TextSave();
	
	public List<HashMap<String, Object>> getUrlParsing(String[] param) throws CallException {
		
		// URL 목록 구해오기
		List<HashMap<String, Object>> list = null;

		try {
			list = urlParserDAO.getUrlParsing(param);
		} catch (CallException e) {
			String msg = "수집된 데이터 없음";
			throw new CallException(e, msg);
		}
		
		List<HashMap<String, Object>> parserDetail = new ArrayList<HashMap<String,Object>>();

		if (list != null && list.size() > 0) {
			// 상세내용 기록할 객체
			parserDetail = getParserDetail(list, param);
		}
		
		return parserDetail;
	}
	
	
	@SuppressWarnings("unchecked")
	private List<HashMap<String, Object>> getParserDetail(List<HashMap<String, Object>> list, String[] param) {
		
		List<HashMap<String, Object>> parserDetail = new ArrayList<HashMap<String,Object>>();
		
		String siteCode = param[0];
		String isType = param[1];
		String keyWordCode= param[5];
		
		int count = 1;
		
		System.out.println("------상세 데이터 컨텐츠를 수집합니다.------");
		
		if (list != null) {
			HashMap<String, Object> map = list.get(0);
			ArrayList<String> totalUrl = (ArrayList<String>) map.get("totalListURL");
			int totalUrlSize = totalUrl.size();
			
			for (String url : totalUrl) {
				String con_url = null;
				// 네이버 블로그일 경우 주소를 컨버팅 한다.
				//if (siteCode.equals("CC0005") && isType.equals("CC0007")) {
				//	con_url = urlParserDAO.getUrlConverter(url);
				//} else {
				//	con_url = url;
				//}
				
				con_url = url;
				
				String content = null;

				if(!con_url.equals("no_naver")){
					if (siteCode.equals("CC0005") && isType.equals("CC0007")) {
						content = urlParserDAO.getSearchParsingString(con_url);											
					} else if (siteCode.equals("CC0005") && isType.equals("CC0008")) {
						content = urlParserDAO.getSearchParsingString(con_url);
					} else if (siteCode.equals("CC0006") && isType.equals("CC0007")) {
						content = urlParserDAO.getSearchParsingDaum(con_url);						
					} else if (siteCode.equals("CC0006") && isType.equals("CC0008")) {
						content = urlParserDAO.getSearchParsingDaumC(con_url);
					}
				} else {
					HashMap<String, Object> detail = new HashMap<String, Object>();
					detail.put("siteCode", siteCode);
					detail.put("isType", isType);
					detail.put("date", param[3]);
					detail.put("keyWord", param[4]);
					detail.put("content", "parsing fail");
					detail.put("url", con_url);
					detail.put("keyWordCode", keyWordCode);
					parserDetail.add(detail);
					/*try {
						ts.textSave(detail);
					} catch (CallException e) {
						e.printStackTrace();
					}*/
				}
				
				// 결과값
				if(content != null) {
					HashMap<String, Object> detail = new HashMap<String, Object>();
					detail.put("siteCode", param[0]);
					detail.put("isType", param[1]);
					detail.put("date", param[3]);
					detail.put("keyWord", param[4]);
					detail.put("content", content);
					detail.put("url", con_url);
					detail.put("keyWordCode", keyWordCode);
					parserDetail.add(detail);
					/*try {
						ts.textSave(detail);
					} catch (CallException e) {
						e.printStackTrace();
					}*/
					
				} else {
					HashMap<String, Object> detail = new HashMap<String, Object>();
					detail.put("siteCode", param[0]);
					detail.put("isType", param[1]);
					detail.put("date", param[3]);
					detail.put("keyWord", param[4]);
					detail.put("content", "parsing fail");
					detail.put("url", con_url);
					detail.put("keyWordCode", keyWordCode);
					parserDetail.add(detail);
					/*try {
						ts.textSave(detail);
					} catch (CallException e) {
						e.printStackTrace();
					}*/
				}
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				System.out.println("총 : " + totalUrlSize + "/ " +count + "건 수집 완료");
				count++;
			}
		}
		
		System.out.println("------상세 데이터 컨텐츠 수집이 끝났습니다.------");
		
		return parserDetail;
	}
	
	public boolean isDate(List<HashMap<String, Object>> logList, String keyWordId, String runDate) {
		
		if (logList != null && logList.size() > 0) {
			
			for (HashMap<String, Object> log : logList) {
				
				String id = (String) log.get("keywordId");
				String date = (String) log.get("keywordDate");
				
				// 같은 키워드일 경우
				if (id.equals(keyWordId)) {
					
					if (runDate.equals(date)) {
						return true;
					}
					
				}
				
			}
			
		}
		
		
		return false;
	}
	
	public List<HashMap<String, Object>> getNaverCafeUrl (String[] params) throws CallException {
		String siteCode = params[0];
		String isType = params[1];
		String word = params[2];
		String date = params[3];
		String keyWord = params[4];
		String keyWordId = params[5];
		
		List<HashMap<String, Object>> parserDetail = new ArrayList<HashMap<String,Object>>();
		
		JSONObject data = null;
		
		try {
			data = urlParserDAO.getNaverCafeUrl(keyWord, date, 1);
		} catch (IOException e) {
			e.printStackTrace();
			String msg = "데이터 수집 에러";
			throw new CallException(e, msg);
		}
		
		JSONObject result = data.getJSONObject("result");
		System.out.println(result.toString());
		
		JSONObject pageInfo = result.getJSONObject("pageInfo");
		
		String totalCount = (String) pageInfo.get("totalCountFormatted");
		
		PageVO pageVO = new PageVO(10, 1, Integer.parseInt(totalCount));
		
		//JSONArray searchList = result.getJSONArray("searchList");
		
		//int searchSize = searchList.length();
		
		/*HashMap<String, Object> detail = new HashMap<String, Object>();
		detail.put("siteCode", siteCode);
		detail.put("isType", isType);
		detail.put("date", date);
		detail.put("keyWord", keyWord);
		detail.put("content", "parsing fail");
		detail.put("url", "");
		detail.put("keyWordCode", keyWordId);
		parserDetail.add(detail);*/
		
		
		return parserDetail;
	}

}
