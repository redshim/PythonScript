/**
 * DBPool예외처리
 * <pre>
 *    컨넥션 풀에서 오류가 발생했을 때의 오류를 처리하여 준다.
 * </pre>
 *
 * @author  신동협
 * @version 1.0.0
 */

package crawling;

public class DBPoolException extends Exception {

	private Exception e;
	
	public DBPoolException(String msg) {
		super(msg);
	}

	public DBPoolException(Exception ex) {
		super(ex.getMessage());
		this.e = ex;
	}
	
	public Exception getException() {
		return e;
	}
}
	
	
