package com.ef;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 
 * @author Pedro Henrique Veras Coelho
 *
 */
public class ConnectionManager {
	
	private Connection conn;
	private PreparedStatement pstm;
	private String host;
	private String port;
	private String username;
	private String password;
	
	public ConnectionManager() throws ClassNotFoundException, SQLException {
		InputStream in;
		try {
			in = new FileInputStream("connection.properties");
			Properties props = new Properties();   
			props.load(in);
			host = props.getProperty("host");
			port = props.getProperty("port");
			username = props.getProperty("username");
			password = props.getProperty("password");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}   
		connect();
	}
	
	private void connect() throws SQLException, ClassNotFoundException  {
		Class.forName("com.mysql.jdbc.Driver");  
		conn = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/logs?autoReconnect=true&useSSL=false",username,password);
	}
	
	public void insert(AccessHistory accessHistory) throws LogParserException {
		try {
			if (conn != null)
				connect();
		} catch (ClassNotFoundException | SQLException e) {
			throw new LogParserException("Error while trying to connect to database");
		}
		
		String query = "INSERT INTO access_history (ip,date) VALUES (?,?)";
		
		try {
			try {
				pstm = this.conn.prepareStatement(query);
				pstm.setString(1, accessHistory.getIp());
				pstm.setTimestamp(2, Timestamp.valueOf(accessHistory.getDate()));
				pstm.executeUpdate();
			} catch (SQLIntegrityConstraintViolationException e) { 
			} finally {
				if (conn != null) {
					conn.close();
				}
				
				if (pstm != null) {
					pstm.close();
				}
			}
		} catch (SQLException e) {
			throw new LogParserException("Error while ");
		}
	}
	
	public List<String> getIpsByDateThreshold (AccessHistory accessHistory) throws SQLException {
		try {
			if (conn != null)
				connect();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		List<String> result = new ArrayList<>();
		StringBuilder query = new StringBuilder("SELECT ip ");
		query.append(" FROM logs.access_history ");
		query.append("WHERE date BETWEEN ? AND ? ");
		query.append("GROUP BY IP ");
		query.append("HAVING count(ip) > ?");
		
		try {
			pstm = this.conn.prepareStatement(query.toString());
			pstm.setTimestamp(1, Timestamp.valueOf(accessHistory.getDate()));
			pstm.setTimestamp(2, Timestamp.valueOf(accessHistory.getEndDate()));
			pstm.setInt(3, accessHistory.getThreshold());
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				result.add(rs.getString("ip"));
			}
		} finally {
			if (conn != null) {
				conn.close();
			}
			
			if (pstm != null) {
				pstm.close();
			}
		}
		
		return result;
	}
}
