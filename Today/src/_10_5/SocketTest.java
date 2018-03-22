package _10_5;

import java.io.*;
import java.net.*;
import java.util.*;

public class SocketTest {
	public static void main(String args[]) throws IOException{
		try(Socket s=new Socket("time-A.timefreq.bldrdoc.gov",13)) {
			InputStream inStream=s.getInputStream();
			@SuppressWarnings("resource")
			Scanner in=new Scanner(inStream);
			
			while(in.hasNextLine()){
				String line=in.nextLine();
				System.out.println(line);
			}
		}
	}
}
