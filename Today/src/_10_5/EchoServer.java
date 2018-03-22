package _10_5;

import java.io.*;
import java.util.*;
import java.net.*;
 
public class EchoServer {
	public static void main(String args[]) throws IOException{
		try(ServerSocket s=new ServerSocket(8189)){
			try(Socket incoming=s.accept()){
				InputStream inStream=incoming.getInputStream();
				OutputStream outStream=incoming.getOutputStream();
				
				try(Scanner in=new Scanner(inStream)){
					PrintWriter out=new PrintWriter(outStream,true);
					
					out.println("Hello! Enter BYE to exit.");
					
					boolean done=false;
					while(!done&&in.hasNextLine()){
						String line=in.nextLine();
						out.println("Echo:"+line);
						if(line.trim().equals("BYE"))
							done=true;
					}
				}
			}
		}
	}
}
