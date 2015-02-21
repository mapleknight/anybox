package com.anybox.database;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.tomcat.jdbc.pool.DataSource;

public class DBConnectionPool {
	
	/**
	 * Singleton
	 */
	private static DBConnectionPool instance = new DBConnectionPool();
	
	public static DBConnectionPool getInstance(){
		return instance;
	}
	
	/**
	 * private constructor
	 */
	private DBConnectionPool() {
		/*
		URI dbUri;
		try {
			dbUri = new URI(System.getenv("DATABASE_URL"));
			user = dbUri.getUserInfo().split(":")[0];
			passwd = dbUri.getUserInfo().split(":")[1];
		    jdbcUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
		    jdbcUrl = jdbcUrl + "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
		    //   postgres://icwrhoamdyfrfp:bQv1I71kugoccl5pud1C7Jzn61@ec2-50-19-236-178.compute-1.amazonaws.com:5432/dus327o98lfme
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	    
		*/
		
	}

    // 数据库驱动名称
    //final static String driver = "com.mysql.jdbc.Driver";
	final static String driver = "org.postgresql.Driver";
    // 数据库连接地址
    //final static String jdbcUrl = "jdbc:mysql://localhost:3306/anybox";
    static String jdbcUrl = "jdbc:postgresql://ec2-50-19-236-178.compute-1.amazonaws.com:5432/dus327o98lfme"
    		+ "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
    
    // 数据库用户名
    static String user = "icwrhoamdyfrfp";
    // 数据库密码
    static String passwd = "bQv1I71kugoccl5pud1C7Jzn61";
    // 连接池初始化大小
    final static int initialSize = 5;
    // 连接池最小空闲
    final static int minPoolSize = 10;
    // 连接池最大连接数量
    final static int maxPoolSize = 10;
    // 最小逐出时间，100秒
    final static int maxIdleTime = 100000;
    // 连接失败重试次数
    final static int retryAttempts = 10;
    // 当连接池连接耗尽时获取连接数
    final static int acquireIncrement = 1;
    // Tomcat Jdbc Pool数据源
    final static DataSource tomcatDataSource = getTomcatDataSource();
    
    public DataSource getDataSource()
    {
    	return tomcatDataSource;
    }
    
    //sql
    private final static String sql = "SELECT * FROM anybox.user;";

    /**
     * 获取Apache tomcat jdbc pool数据源
     * @return
     */
    private static DataSource getTomcatDataSource() {
        DataSource ds = new DataSource();
        ds.setUrl(jdbcUrl);
        ds.setUsername(user);
        ds.setPassword(passwd);
        ds.setDriverClassName(driver);
        ds.setInitialSize(initialSize);
        ds.setMaxIdle(minPoolSize);
        ds.setMaxActive(maxPoolSize);
        ds.setTestWhileIdle(false);
        ds.setTestOnBorrow(false);
        ds.setTestOnConnect(false);
        ds.setTestOnReturn(false);
        ds.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
        		"org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        return ds;
    }
    

    public static void main(String[] args) throws IOException, SQLException {

        try {
            Connection conn = getTomcatDataSource().getConnection();
            Statement st = conn.createStatement();
            ResultSet result = st.executeQuery(sql);
            while (result.next()) {
                System.out.println("Read from DB: " + result.getString("name") + ":" + result.getString("password"));
            }
            result.close();
            st.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
