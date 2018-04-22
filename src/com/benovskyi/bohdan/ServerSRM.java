package com.benovskyi.bohdan;

import java.sql.*;

public class ServerSRM {

	public static void main(String[] args) {
		try {
			String url = "jdbc:mysql://localhost:3306/sysmon?autoReconnect=true&useSSL=false";
			String login = "root";
			String password = "2768652";
			
			Connection con = DriverManager.getConnection(url, login, password);
			
			try {
				Statement myStmt = con.createStatement();
				ResultSet myRs = myStmt.executeQuery("select * from users");
				
				while(myRs.next()) {
					System.out.println(myRs.getString("iduser") + " " + myRs.getString("role") + " " 
									+ myRs.getString("login") + " " + myRs.getString("password"));
				}
				
				myRs.close();
				myStmt.close();
			} finally {
				con.close();
			}
		} catch (Exception e) {
            e.printStackTrace();
        }
		
	}

}
