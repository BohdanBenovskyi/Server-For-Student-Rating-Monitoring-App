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
		try {
			myStmt = con.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
				String institutes = "";

				if (str.equals("END"))
					break;

				System.out.println("Echoing: " + str);

				if (str.equals("login")) {
					login = in.readLine();
					password = in.readLine();
					System.out.println("log: " + login + ", pasw: " + password);
					try {
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

				if (str.equals("inst")) {
					try {
						myRs = myStmt.executeQuery("select * from sysmon.institutes");

						while (myRs.next()) {
							institutes += myRs.getString("name");
							institutes += " ";
						}

						String[] inst = institutes.split("\\s+");
						String[] grp = new String[inst.length];
						for (int i = 0; i < grp.length; i++) {
							grp[i] = "";
						}

						for (int i = 0; i < inst.length; i++) {
							String qry = "select * from sysmon.groups where `institute` = \'" + inst[i] + "\'";
							System.out.println("Query: " + qry);

							myRs = myStmt.executeQuery(qry);

							while (myRs.next()) {
								grp[i] += myRs.getString("group");
								System.out.println(grp[i]);
								grp[i] += " ";
							}
						}

						out.println("inst");
						out.println(institutes);

						for (int i = 0; i < grp.length; i++)
							System.out.println("Groups server: " + grp[i]);

						out.println("grp");
						for (int i = 0; i < grp.length; i++) {
							out.println(grp[i]);
						}

					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
				if(str.equals("get_rating")) {
					String group = in.readLine();
					String result = "";
					String qry = "select * from sysmon.students where `group` = \'" + group + "\'";
					System.out.println("Query: " + qry);
					
					try {
						myRs = myStmt.executeQuery(qry);
						
						while (myRs.next()) {
							result += myRs.getString("name") + " " + myRs.getString("lastname") + " " + myRs.getString("group") + " " + myRs.getString("institute") + " " + myRs.getString("stn") + " " + myRs.getString("rating") + "&";
						}
						
						out.println("rating");
						out.println(result);
					
					} catch (SQLException e) {
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
