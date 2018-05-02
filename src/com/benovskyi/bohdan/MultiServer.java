package com.benovskyi.bohdan;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiServer {
	   static final int PORT = 9191;
	   
	   public static void main(String[] args) throws IOException {
	      ServerSocket s = new ServerSocket(PORT);
	      System.out.println("Server Started");
	      try {
	         while (true) {
	            Socket socket = s.accept();
	            try {
	               new ServerOne(socket);
	            }
	            catch (IOException e) {
	               socket.close();
	            }
	         }
	      }
	      finally {
	         s.close();
	      }
	   }
	}