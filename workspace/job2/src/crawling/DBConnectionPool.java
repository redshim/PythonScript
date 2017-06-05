/**
 * 컨넥션 풀
 * <pre>
 *    컨넥션 풀를 정의한다.
 * </pre>
 *
 * @author  신동협
 * @version 1.0.0
 */

package crawling;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;

public class DBConnectionPool {
	private int checkedOut;
	private Vector freeConnections = new Vector();
	private int maxConn;
	private String name;
	private String password;
	private String URL;
	private String user;
	
	public static final int DEFAULT_MAX_CON = 50;


	/**
	 * ConnectionPool데이타를 설정한다.
     * @param name Pool명
     * @param URL 연결문자열
     * @param user DBUser
     * @param password User Password
     * @param maxConn 최대연결개수
     */	

	public DBConnectionPool(String name, String URL,
					 String user, String password, int maxConn, String charset) {
		this.name = name;
		this.URL = URL;
		this.user = user;
		this.password = password;
		this.maxConn = maxConn;
		
		if ( this.maxConn <= 0 ) {
			this.maxConn = DEFAULT_MAX_CON;
		}
	}


	/**
	 * Connection를 얻어온다.
     * @return conn Connection 정보
     */	

	public synchronized Connection getConnection() throws DBPoolException {
		Connection conn = null;
		
		while( freeConnections.size() <= 0 ) {
			if ( checkedOut < maxConn ) {
				conn = newConnection();
				break;
			}
			try {
				// 저장되어 있는 Connection이 없을 경우
				// Connection이 반환될 때 까지 기다린다.
				wait();
			} catch(InterruptedException ex) {}
		}
		
		if ( conn == null ) {
			conn = (Connection) freeConnections.firstElement();
			freeConnections.removeElementAt(0);
			
			try {
				if ( conn.isClosed() ) {
					conn = newConnection();
				}
			} catch(SQLException ex) {
				conn = newConnection();
			}
		}
		if (conn != null) {
			checkedOut++;
		} else {
			throw new DBPoolException("Can't obtain DB Connection!");
		}
		return conn;
	}
	
	/**
	 * 새로운 Connection를 얻어온다.
     * @return conn Connection 정보
     */	

	private Connection newConnection() {
		Connection conn = null;
		try {
			if (user == null) {
				conn = DriverManager.getConnection(URL);
			} else {
				conn = DriverManager.getConnection(URL, user, password);
			}
		} catch (SQLException ex) {
			return null;
		}
		return conn;
	}
	
	public synchronized void returnConnection(Connection conn) {
		// Connection을 Vector에 맨 마지막에 삽입한다.
		freeConnections.addElement(conn);
		checkedOut--;
		notifyAll();
	}
	
	public synchronized void close() {
		Enumeration allConnections = freeConnections.elements();
		while (allConnections.hasMoreElements()) {
			Connection conn = (Connection) allConnections.nextElement();
			try {
				conn.close();
			} catch (SQLException ex) { }
		}
		freeConnections.removeAllElements();
	}
}
