package crawling;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.KeywordContents;

public class KeywordDao {

	private DBConnectionPoolMgr dbMgr = null;
	private Connection conn = null;
	private PreparedStatement pstmt = null;

	//DB 접속
	public void dbConnect() throws Exception {
		dbMgr = DBConnectionPoolMgr.getInstance();
		conn = dbMgr.getConnection("kcmi");
		conn.setAutoCommit(false);
	}

	//DB 접속 헤제
	public void dbClose() throws Exception{
		pstmt.close(); 
		conn.commit(); 
		dbMgr.returnConnection("kcmi", conn);
		dbMgr.close();
	}
	
	
	//키워드 리스트
	public ArrayList<HashMap<String, String>> getKeywordList(String collector) throws Exception {

		dbConnect();
		
		String sql = "SELECT a.KEYWORD_ID, a.KEYWORD_NM  FROM keyword a, keyword_status b WHERE 1=1"
		  			+ " and b.COLLECTOR_NAME = ? and a.KEYWORD_ID =  b.KEYWORD_ID GROUP BY a.KEYWORD_ID ORDER BY a.KEYWORD_ID";

		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, collector);

		ResultSet rs = pstmt.executeQuery();
		ArrayList<HashMap<String, String>> keywordList = new ArrayList<HashMap<String, String>>();

		int i = 0;
		while (rs.next()) {
			HashMap<String, String> val = new HashMap<>();
			val.put("keywordNm", rs.getString("KEYWORD_NM"));
			val.put("keywordId", rs.getString("KEYWORD_ID"));
			keywordList.add(val);
			i++;
		}

		dbClose();
		
		return keywordList;
	}

	//중단된 시점 이후의 키워드 리스트
	public ArrayList<HashMap<String, String>> getKeywordContinueList(String collector, String strDate) throws Exception {
		
		/*String sql = "select KEYWORD_ID from KEYWORD_STATUS where COLLECTOR_NAME = ? and KEYWORD_ID > " 
				+ "(select KEYWORD_ID from MAIN_REPORT_CONTENTS where collecting_date = ? order by MAIN_REPORT_CONTENTS_NO desc limit 1)";*/
		dbConnect();
		
		String sql = "select a.KEYWORD_ID, a.KEYWORD_NM from KEYWORD a, "
					+ "(select KEYWORD_ID from KEYWORD_STATUS where COLLECTOR_NAME = ? and KEYWORD_ID > "  
					+ "(select KEYWORD_ID from MAIN_REPORT_CONTENTS where collecting_date = ? order by MAIN_REPORT_CONTENTS_NO desc limit 1) ) b "
					+ "where a.KEYWORD_ID = b.KEYWORD_ID group by b.KEYWORD_ID";
		
		pstmt = conn.prepareStatement(sql);
		
		pstmt.setString(1, collector);
		pstmt.setString(2, strDate);
		
		ResultSet rs = pstmt.executeQuery();
		ArrayList<HashMap<String, String>> keywordList = new ArrayList<>();

		int i = 0;
		while (rs.next()) {
			HashMap<String, String> val = new HashMap<>();
			val.put("keywordNm", rs.getString("KEYWORD_NM"));
			val.put("keywordId", rs.getString("KEYWORD_ID"));
			keywordList.add(val);
			i++;
		}
		
		dbClose();
		
		return keywordList;
	}
	
	//
	public KeywordContents getDataCnt(KeywordContents cont) throws Exception {

		dbConnect();
		
		String sql = "select keyword_id, collecting_date from main_report_contents where collecting_date >= ? and collecting_date <= ? order by COLLECTING_DATE desc, KEYWORD_ID desc limit 1";

		pstmt = conn.prepareStatement(sql);

		pstmt.setString(1, cont.getBeginDate_s());
		pstmt.setString(2, cont.getEndDate_s());

		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			cont.setKeyword_id(rs.getString("KEYWORD_ID"));
			cont.setCollecting_date(rs.getString("COLLECTING_DATE"));
		}
		
		dbClose();
		
		return cont;
	}

	public String getLastKeywordId(String collector) throws Exception{
		
		dbConnect();
		
		String sql = "select keyword_id from keyword_status where COLLECTOR_NAME = ? order by keyword_id desc limit 1";
		pstmt = conn.prepareStatement(sql);

		pstmt.setString(1, collector);
		ResultSet rs = pstmt.executeQuery();

		String keywordId = null;
		while (rs.next()) {
			keywordId = rs.getString("KEYWORD_ID");
		}

		dbClose();
		return keywordId;

	}
	
	public void setKeywordData(List<Map<Integer, String>> crawlingData) throws Exception{
		
		dbConnect();
		
		String sql = "insert into ADM_BASE.MAIN_REPORT_CONTENTS (SITE_CODE_ID, PART_CODE_ID, KEYWORD_ID, KEYWORD_NM, COLLECTING_DATE, CONTENTS_COUNT)";
		sql += "values(?, ?, ?, ?, ?, ?)";
		
		pstmt = conn.prepareStatement(sql);
		
		for (Map<Integer, String> li : crawlingData) {
			for(Map.Entry<Integer, String> map : li.entrySet()){
				pstmt.setString(map.getKey(), map.getValue());
			}
			pstmt.executeUpdate();
		}		
		dbClose();
		
	}

	//현재 자신의 IP로 수집기 정보 가져오기
	public KeywordContents getcollectorConfig(KeywordContents cont) throws Exception{
		
		dbConnect();

		String sql = "select collector_name, collect_date, collect_date_end from collector_config where collector_ip = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, cont.getCollector_ip());
		
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			cont.setCollector_name(rs.getString("collector_name"));
			cont.setBeginDate_s(rs.getString("collect_date"));
			cont.setEndDate_s(rs.getString("collect_date_end"));
		}
		
		dbClose();
		
		return cont;
	}
}
