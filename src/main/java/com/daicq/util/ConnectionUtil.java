package com.daicq.util;

import com.daicq.dao.doc.MySqlDoc;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionUtil {
	
	// Connection to MySQL database had encrypt
	public static Connection getMysqlConnection(MySqlDoc mySqlDoc) {
		Connection con = null;
		String serverAddress = mySqlDoc.getServerAddress();
		int port = mySqlDoc.getPort();

		String databaseName = mySqlDoc.getDatabaseName();
		String databaseUser = mySqlDoc.getDatabaseUser();
		String databasePassword = mySqlDoc.getDatabasePassword();
		try {
			UtilBase64 s = new UtilBase64();
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://"
					+ serverAddress + ":" + port + "/" + databaseName
					//+ "?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull", databaseUser,
					+ "?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=UTF-8"
					+ "&autoReconnect=true"
					+ "&characterEncoding=UTF-8" 
					+ "&characterSetResults=UTF-8", databaseUser,
					s.decrypt(databasePassword));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}
	
	// Connection to MySQL database not encrypt
			public static Connection getMysqlConnectionNotEncode(MySqlDoc mySqlDoc) {
				Connection con = null;
				String serverAddress = mySqlDoc.getServerAddress();
				int port = mySqlDoc.getPort();

				String databaseName = mySqlDoc.getDatabaseName();
				String databaseUser = mySqlDoc.getDatabaseUser();
				String databasePassword = mySqlDoc.getDatabasePassword();
				try {
					Class.forName("com.mysql.jdbc.Driver");
					con = DriverManager.getConnection("jdbc:mysql://"
							+ serverAddress + ":" + port + "/" + databaseName
							//+ "?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull", databaseUser,
							+ "?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=UTF-8"
							+ "&autoReconnect=true"
							+ "&characterEncoding=UTF-8" 
							+ "&characterSetResults=UTF-8", databaseUser,
							databasePassword);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return con;
			}
}
