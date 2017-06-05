/**
 * 컨넥션 풀 관리자
 * <pre>
 *    컨넥션 풀을 생성 및 회수 시켜준다.
 * </pre>
 *
 * @author  신동협
 * @version 1.0.0
 */

package crawling;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;


	/**
	 * DBPool Instance 확인/생성
     */	

public class DBConnectionPoolMgr {
	private static DBConnectionPoolMgr instance = null;
	
	private Vector drivers = new Vector();
	private Hashtable pools = new Hashtable();
	
	public static synchronized DBConnectionPoolMgr getInstance()
			 throws DBPoolException {
		if (instance == null) {
			synchronized(DBConnectionPoolMgr.class) {
				if (instance == null)
					instance = new DBConnectionPoolMgr();
			}
		}
		return instance;
	}
	
	private DBConnectionPoolMgr() throws DBPoolException {
		init();
	}

	/**
	 * DBPool 초기화
     */	

	private void init() throws DBPoolException {
		Properties dbProps = new Properties();
//		dbProps.setProperty("kcmi.url", "jdbc:mysql://127.0.0.1:3306/adm_base");
		dbProps.setProperty("kcmi.url", "jdbc:mysql://localhost:3306/adm_base");
		dbProps.setProperty("kcmi.user", "root");
		dbProps.setProperty("kcmi.password", "root");
		dbProps.setProperty("kcmi.maxconn", "50");
		
		loadJDBCDrivers(dbProps);
		createPools(dbProps);
	}

	/**
	 * JDBC Dirver 읽어오기
	 * @param props 프로퍼티
     * @return 
     */	

	private void loadJDBCDrivers(Properties props) throws DBPoolException {
		String driverClasses = 
		props.getProperty("drivers", "com.mysql.jdbc.Driver");
		StringTokenizer st = new StringTokenizer(driverClasses);
		
		while (st.hasMoreElements()) {
			String driverClassName = st.nextToken().trim();
			try {
				Driver driver = 
					(Driver)Class.forName(driverClassName).newInstance();
				DriverManager.registerDriver(driver);
				drivers.addElement(driver);
			} catch(ClassNotFoundException ex) {
				throw new DBPoolException(
					"Can't find JDBC Driver class: "+ex.getMessage());
			} catch(InstantiationException ex) {
				throw new DBPoolException(
					 "Can't instantiate JDBC Driver class: "+ex.getMessage());
			} catch(SQLException ex) {
				throw new DBPoolException(
					 "Exception occured in DriverManager: "+ex.getMessage());
			} catch(Exception ex) {
				throw new DBPoolException("Exception: "+ex.getMessage());
			}
		}
	}

	/**
	 * DBPool 생성
	 * @param props 프로퍼티
     * @return 
     */	

	private void createPools(Properties props) {
		Enumeration propNames = props.propertyNames();
		while (propNames.hasMoreElements()) {
			String name = (String) propNames.nextElement();
			if (name.endsWith(".url")) {
				String poolName = name.substring(0, name.lastIndexOf("."));
				String url = props.getProperty(poolName + ".url");
				if (url == null) {
					continue;
				}
				String user = props.getProperty(poolName + ".user", "root");
				String password = props.getProperty(poolName + ".password", "");
				String maxconn = props.getProperty(poolName + ".maxconn", "20");
				String charset = props.getProperty(poolName + ".charset", "");
				int max;
				try {
					max = Integer.valueOf(maxconn).intValue();
				} catch (NumberFormatException e) {
					max = 20;
				}
				DBConnectionPool pool = 
					new DBConnectionPool(poolName, url, user, password, max, charset);
				pools.put(poolName, pool);
			}
		}
	}
	
	public Connection getConnection(String name) throws DBPoolException {
		DBConnectionPool pool = (DBConnectionPool) pools.get(name);
		if (pool != null) {
			return pool.getConnection();
		} else {
			throw new DBPoolException("Not exist pool name: "+name);
		}
	}
	
	public void returnConnection(String name, Connection con) {
		DBConnectionPool pool = (DBConnectionPool) pools.get(name);
		if (pool != null) {
			pool.returnConnection(con);
		}
	}

	/**
	 * DBPool 닫기
     */	
		
	public synchronized void close() {
		Enumeration allPools = pools.elements();
		while (allPools.hasMoreElements()) {
			DBConnectionPool pool = (DBConnectionPool) allPools.nextElement();
			pool.close();
		}
		Enumeration allDrivers = drivers.elements();
		while (allDrivers.hasMoreElements()) {
			Driver driver = (Driver) allDrivers.nextElement();
			try {
				DriverManager.deregisterDriver(driver);
			}
			catch (SQLException ex) { }
		}
	}
}
