package model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import util.CallException;
import util.PropertyReader;

public class KeywordList {
	
	/*public static List<HashMap<String, Object>> getKeyWordList() {
		ArrayList<HashMap<String, Object>> keyWordList = new ArrayList<HashMap<String,Object>>();
		
		
		HashMap<String, Object> keyword = new HashMap<String, Object>();
		
		keyword.put("keywordId", "K0000001");
		keyword.put("keywordNm", "현대자동차");
		keyWordList.add(keyword);
		
		keyword = new HashMap<String, Object>();
		keyword.put("keywordId", "K0000002");
		keyword.put("keywordNm", "제네시스");
		keyWordList.add(keyword);
		
		keyword = new HashMap<String, Object>();
		keyword.put("keywordId", "K0000003");
		keyword.put("keywordNm", "쏘나타");
		keyWordList.add(keyword);
		
		keyword = new HashMap<String, Object>();
		keyword.put("keywordId", "K0000004");
		keyword.put("keywordNm", "투싼");
		keyWordList.add(keyword);
		
		keyword = new HashMap<String, Object>();
		keyword.put("keywordId", "K0000005");
		keyword.put("keywordNm", "아반떼");
		keyWordList.add(keyword);
		
		
		
		
		return keyWordList;
	}*/
	
	/**
	 * csv 설정 파일 읽어들이기
	 */
	@SuppressWarnings("resource")
	public static List<HashMap<String, Object>> getKeyWord() throws CallException {
		
		ArrayList<HashMap<String, Object>> keyWordList = new ArrayList<HashMap<String,Object>>();
		
		try {	
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(PropertyReader.getValue("collector.keywordData")), "MS949"));
			//BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(PropertyReader.getValue("collector.keywordData")), "UTF-8"));
			
			String line;
					
			while((line = br.readLine() ) != null) {
				String[] keywordData = line.split(",");
				HashMap<String, Object> keyword = new HashMap<String, Object>();
				//keywordData[1].replace("\"", "");
				keywordData[1] = keywordData[1].replace("\"\"", "^");
				keywordData[1] = keywordData[1].replace("\"", "");
				keywordData[1] = keywordData[1].replace("^", "\"");
								
				keyword.put("keywordId", keywordData[0]);
				keyword.put("keywordNm", keywordData[1]);
				keyWordList.add(keyword);
			}
		
		} catch (UnsupportedEncodingException e) {
			String msg = "인코딩 애러";
			throw new CallException(e, msg);
		} catch (FileNotFoundException e) {
			String msg = "날짜 계산 오류";
			throw new CallException(e, msg);
		} catch (IOException e) {
			String msg = "키워드 설정 csv 파일 읽기 오류";
			throw new CallException(e, msg);
		}
		
		return keyWordList;
	}
	
	/**
	 * 로그 기록 데이터 가져오기
	 */
	public static List<HashMap<String, Object>> getLog() throws CallException {
		
		ArrayList<HashMap<String, Object>> keyWordLogList = new ArrayList<HashMap<String,Object>>();
			
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(PropertyReader.getValue("collector.saveLogData")), "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			return keyWordLogList;
		} catch (FileNotFoundException e1) {
			return keyWordLogList;
		}
		
		String line;
					
		try {
			while((line = br.readLine() ) != null) {
				String[] logData = line.split(",");
				HashMap<String, Object> keywordLog = new HashMap<String, Object>();
				
				keywordLog.put("keywordId", logData[0]);
				keywordLog.put("keywordDate", logData[1]);
				keyWordLogList.add(keywordLog);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return keyWordLogList;
	}

}

