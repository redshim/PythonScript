package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;

public class TextSave {

	/**
	 * 파일에 데이터 1건씩 넣기 
	 */
	public void textSave(HashMap<String, Object> detail) throws CallException {
		
		String stidCode = (String) detail.get("siteCode");
		String isType = (String) detail.get("isType");
		String date = (String) detail.get("date");
		String content = (String) detail.get("content");
		String url = (String) detail.get("url");
		String keyWordCode = (String) detail.get("keyWordCode");

		File dir = new File(PropertyReader.getValue("collect.path"));
		
		String dirs = PropertyReader.getValue("collect.path") + "\\" + stidCode + "\\" + isType;
		
		dir = new File(dirs);
		
		//디렉토리가 없으면
        if (!dir.isDirectory()) {
        	dir.mkdirs();
        }
        
        String savePath = dir+"/"+keyWordCode+".txt";
        
        OutputStreamWriter ou = null;
        
		try {
			ou = new OutputStreamWriter(new FileOutputStream(savePath, true), "UTF-8");
		} catch (Exception e) {
			String msg = "파일 생성 위치 에러";
			throw new CallException(e, msg);
		}
		
		BufferedWriter bw = new BufferedWriter(ou);
		
		try {
			String msg = stidCode+","+isType+","+date+","+url+","+content;
			bw.write(msg);
			bw.newLine(); // 줄바꿈
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(e);
			System.exit(1);
		} finally {
			try {
				if (bw != null) bw.close();
				if (ou != null) ou.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		

	}
	
	/**
	 * 파일에 데이터 리스트단위로 넣기 
	 */
	public void textSave(List<HashMap<String, Object>> list, String[] param) throws CallException {
		
		String siteCode = param[0];
		String isType = param[1];
		String keyWordCode = param[5];
		
		if (list != null && list.size() > 0 ) {


			File dir = new File(PropertyReader.getValue("collect.path"));
			
			String dirs = PropertyReader.getValue("collect.path") + "\\" + siteCode + "\\" + isType;
			
			dir = new File(dirs);
			
			//디렉토리가 없으면
	        if (!dir.isDirectory()) {
	        	dir.mkdirs();
	        }
	        
	        String savePath = dir+"/"+keyWordCode+".txt";

	        OutputStreamWriter ou = null;
	        
			try {
				ou = new OutputStreamWriter(new FileOutputStream(savePath, true), "UTF-8");
			} catch (Exception e) {
				String msg = "파일 생성 위치 에러";
				throw new CallException(e, msg);
			}
			
			BufferedWriter bw = new BufferedWriter(ou);
	        
			for(HashMap<String, Object> detail : list) {
				try {
					String date = (String) detail.get("date");
					String content = (String) detail.get("content");
					String url = (String) detail.get("url");
					
					String msg = siteCode+","+isType+","+keyWordCode+","+date+","+url+","+content;
					bw.write(msg);
					bw.newLine(); // 줄바꿈
				} catch (IOException e) {
					e.printStackTrace();
					System.err.println(e);
					System.exit(1);
				} 
				
			}
			
			try {
				if (bw != null) bw.close();
				if (ou != null) ou.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}


	}
	
	/**
	 * 검색한 데이터를 Log파일에 저장해두기
	 */
	public void saveLog(String date, String keyWordId) throws CallException {		
		
		File dir = new File(PropertyReader.getValue("collector.saveKeywordDate"));
		
		//디렉토리가 없으면
        if(!dir.isDirectory()) {
        	dir.mkdirs();
        }
        
        String saveFile = dir + "/" + PropertyReader.getValue("collector.saveName");
        
        OutputStreamWriter ou = null;
        
		try {
			ou = new OutputStreamWriter(new FileOutputStream(saveFile, true), "UTF-8");
		} catch (Exception e) {
			String msg = "설정 파일 생성 애러";
			throw new CallException(e, msg);
		}	
		
		BufferedWriter bw = new BufferedWriter(ou);
		
		try {
			String msg = keyWordId + "," + date;
			bw.write(msg);
			bw.newLine(); // 줄바꿈
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(e);
			System.exit(1);
		} finally {
			try {
				if (bw != null) bw.close();
				if (ou != null) ou.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
