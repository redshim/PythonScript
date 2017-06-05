package crawling;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


public class CallException extends Exception {

	private static final Logger logger = Logger.getLogger(CallException.class);
	
	public CallException(Exception e, String msg) {
		
		logger.error(msg);
		
		e.printStackTrace();
	}
	
	public CallException(List<Map<Integer, String>> crawlingData, String msg) {

		logger.error(msg);
		
		// 키워드 리스트
		KeywordDao keywordDao = new KeywordDao();
		try {
			keywordDao.setKeywordData(crawlingData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("============= insert 성공 =============");
	}
}
