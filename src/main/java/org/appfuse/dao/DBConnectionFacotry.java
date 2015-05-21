package org.appfuse.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public enum DBConnectionFacotry {

	INSTANCE;
	
	public Connection getDBConnection()
	{
		Connection conn;

		try
		{
			//Change these settings according to your local configuration
			String driver = "com.mysql.jdbc.Driver";
			String connectString = "jdbc:mysql://localhost:3306/hps";
			String user = "root";
			String password = "";


			Class.forName(driver);
			conn = DriverManager.getConnection(connectString, user, password);
		}
		catch (ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
			
		}

		return conn;
	}
}
