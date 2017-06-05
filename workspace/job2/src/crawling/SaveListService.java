package crawling;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Search;

import org.apache.log4j.Logger;


public class SaveListService {
	
	private static final Logger logger = Logger.getLogger(SaveListService.class);
	/*@Resource(name="crawlingService")
    private CrawlingService crawlingService;*/
	/**
	 * 티몬/쿠팡/위메프 검색 페이지 파싱한 데이터를 list 형식으로 반환 
	 * @param conn 
	 * @param dbMgr 
	 * @param word
	 * @return
	 */
	public Map<Integer, String> saveList(String[] args) throws CallException{

		Map<Integer, String> saveMap = new HashMap<Integer, String>();

		// 테스트용 파라메터 0:사이트 영어명 , 1:조회 페이지수 디폴트 50 , 2: 검색날짜, 3: 파트 영어명, 4: 키워드, 5: 사이트 한글명, 6:저장 파일명 , 7: 상세컨탠츠 저장여부

		String saveContent = ""; //모든 컨탠츠를 파일로 변환할 변수
		String tcontent = "";
		String saveInfo = args[0]+","+ args[3] +","+ args[8] +","+ args[2]; //저장 고정 정보

		//현재 날짜
		DateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
		Date nowDate = new Date();
		String tempDate = sdFormat.format(nowDate); 
		
		File dir = new File(PropertyReader.getValue("collect.path")+tempDate);

		//디렉토리가 없으면
        if(!dir.isDirectory())
        {
        	dir.mkdirs();
        }
		
		String savePath = dir+"/"+args[6]+".txt";

		//FileWriter fw = null;
		OutputStreamWriter ou = null;
		try {
			//fw = new FileWriter(savePath);
			ou = new OutputStreamWriter(new FileOutputStream(savePath), "UTF-8");
		} catch (Exception e) {
			String msg = "파일 생성 위치 에러";
			throw new CallException(e, msg);
			
		} // 절대주소 경로 가능

		//BufferedWriter bw = new BufferedWriter(fw);		
		BufferedWriter bw = new BufferedWriter(ou);		
		
		SearchListService sls = new SearchListService();
		
		List<Search> searchList = null;
		
		try {
			searchList = sls.crawlingList(args);
		} catch (Exception e1) {
			String msg = "수집 스트링 배열 에러";
			throw new CallException(e1, msg);
		}
		int Cn = 1;
		int totCnt = 0;

		try{
			if(searchList.size() <= 0){
				logger.debug("검색 결과가 없습니다.");
			}else{
				for(Search s : searchList){//메타정보 출력
					System.out.println(Cn);

					tcontent = s.getContent().replace(",", " ");
					tcontent = tcontent.replace("?", " ");
					tcontent = tcontent.replace(".", "");
					tcontent = tcontent.replace("(", "");
					tcontent = tcontent.replace(")", "");
					tcontent = tcontent.replace("[", " ");
					tcontent = tcontent.replace("]", " ");
					tcontent = tcontent.replace("!", " ");
					tcontent = tcontent.replace("/", " ");
					tcontent = tcontent.replace("\"", " ");
					tcontent = tcontent.replace("^", " ");
					tcontent = tcontent.replace("*", " ");
					tcontent = tcontent.replace("▶", " ");
					tcontent = tcontent.replace("◇", " ");
					tcontent = tcontent.replace("◆", " ");
					tcontent = tcontent.replace("→", " ");
					tcontent = tcontent.replace("ⓒ", " ");
					tcontent = tcontent.replace(":", " ");
					tcontent = tcontent.replace("▲", " ");
					tcontent = tcontent.replace("◈", " ");
					tcontent = tcontent.replace("★", " ");
					tcontent = tcontent.replace("♡", " ");
					tcontent = tcontent.replace("▲", " ");
					tcontent = tcontent.replace("한 ", " ");
					tcontent = tcontent.replace("차 ", " ");
					tcontent = tcontent.replace("중 ", " ");
					tcontent = tcontent.replace("적 ", " ");
					tcontent = tcontent.replace("중 ", " ");
					tcontent = tcontent.replace("적 ", " ");					
					tcontent = tcontent.replace("시  ", " ");
					tcontent = tcontent.replace("○", " ");
					
					tcontent = tcontent.replace("|", " ");
					tcontent = tcontent.replace("<", " ");
					tcontent = tcontent.replace(">", " ");
					tcontent = tcontent.replace("~", " ");
					
					
					tcontent = tcontent.replace("+", " ");
					tcontent = tcontent.replace("               "," ");
					tcontent = tcontent.replace("              "," ");
					tcontent = tcontent.replace("             "," ");
					tcontent = tcontent.replace("            "," ");
					tcontent = tcontent.replace("           "," ");
					tcontent = tcontent.replace("          "," ");
					tcontent = tcontent.replace("         "," ");
					tcontent = tcontent.replace("        "," ");
					tcontent = tcontent.replace("       "," ");
					tcontent = tcontent.replace("      "," ");
					tcontent = tcontent.replace("     "," ");
					tcontent = tcontent.replace("    "," ");
					tcontent = tcontent.replace("   "," ");
					tcontent = tcontent.replace("  "," ");
				    
					//tcontent = tcontent.replaceAll("[^\\x20-\\x7e]", "");
	
					saveContent = saveInfo +","+s.getConCount() +","+ Cn +","+ s.getUrl() +",\""+ tcontent + "\"";
					bw.write(saveContent);
					bw.newLine(); // 줄바꿈
					Cn++;
					totCnt = Integer.parseInt(s.getConCount());
				}
				
				//main_report_content에 데이터 쌓기
				//하나의 키워드 수집이 끝나면 데이터를 쌓는다.

				String[] dataArr = args[6].split("_");
				saveMap.put(1, dataArr[0]);
				saveMap.put(2, dataArr[1]);
				saveMap.put(3, dataArr[2]);
				saveMap.put(4, args[5]);
				saveMap.put(5, dataArr[3]);
				saveMap.put(6, Integer.toString(totCnt));
			}
			
			bw.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e); // 에러가 있다면 메시지 출력
			System.exit(1);
		}
		return saveMap;
		
	}	
}
