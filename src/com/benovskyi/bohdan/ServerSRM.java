package com.benovskyi.bohdan;

import java.sql.*;

public class ServerSRM {

	public static void main(String[] args) {
	
		try {
			Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sysmon?autoReconnect=true&useSSL=false", "root", "2768652");
			Statement myStmt = myConn.createStatement();
			ResultSet myRs = myStmt.executeQuery("select * from users");
			
			while(myRs.next()) {
				System.out.println(myRs.getString("iduser") + " " + myRs.getString("role") + " " 
								+ myRs.getString("login") + " " + myRs.getString("password"));
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}

}
