package com.benovskyi.bohdan;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class ServerOne extends Thread {
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private Connection con;
	private Statement myStmt;
	private ResultSet myRs;

	public ServerOne(Socket s, Connection c) throws IOException {
		socket = s;
		con = c;
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
		start();
	}

	public void run() {
		try {
			while (true) {
				String str = in.readLine();
				String login;
				String password;

				if (str.equals("END"))
					break;

				System.out.println("Echoing: " + str);

				if (str.equals("login")) {
					login = in.readLine();
					password = in.readLine();
					System.out.println("log: " + login + ", pasw: " + password);
					try {
						myStmt = con.createStatement();
						myRs = myStmt.executeQuery("select * from sysmon.users where login = \'" + login
								+ "\' and password = " + password);
						while (myRs.next()) {
							if (myRs.getString("role").equals("admin")) {
								System.out.println("Before Thread priority: " + Thread.currentThread().getPriority());
								System.out.println("You are admin\n");
								Thread.currentThread().setPriority(MAX_PRIORITY);
								System.out.println("Thread priority: " + Thread.currentThread().getPriority());
								out.println("OK");
							} else if (myRs.getString("role").equals("teacher")) {
								System.out.println("Before Thread priority: " + Thread.currentThread().getPriority());
								System.out.println("You are teacher\n");
								Thread.currentThread().setPriority(NORM_PRIORITY);
								System.out.println("Thread priority: " + Thread.currentThread().getPriority());
								out.println("OK");
							} else if (myRs.getString("role").equals("student")) {
								System.out.println("Before Thread priority: " + Thread.currentThread().getPriority());
								System.out.println("You are student\n");
								Thread.currentThread().setPriority(MIN_PRIORITY);
								System.out.println("Thread priority: " + Thread.currentThread().getPriority());
								out.println("OK");
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			System.out.println("closing...");
		} catch (IOException e) {
			System.err.println("IO Exception");
		} finally {
			try {
				System.out.println("closing socket");
				myRs.close();
				myStmt.close();
				socket.close();
			} catch (IOException e) {
				System.err.println("Socket not closed");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
