package com.benovskyi.bohdan;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class MultiServer {
	static final int PORT = 9191;
	public static final String url = "jdbc:mysql://localhost:3306/sysmon?autoReconnect=true&useSSL=false";
	public static final String login = "root";
	public static final String password = "2768652";
	public static Connection con;

	public static void main(String[] args) throws IOException, SQLException {
		ServerSocket s = new ServerSocket(PORT);
		System.out.println("Server Started");

		try {
			con = DriverManager.getConnection(url, login, password);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			while (true) {
				Socket socket = s.accept();
				try {
					new ServerOne(socket, con);
				} catch (IOException e) {
					socket.close();
				}
			}
		} finally {
			System.out.println("return to main closing socket");
			s.close();
			con.close();
		}
	}
}