package _10_24;

import java.util.*;
public class TestInterrupt {
	public static void main(String args[]){
		MyThread thread=new MyThread();
		thread.start();
		try{
			Thread.sleep(10000);
		}catch (InterruptedException e) {
			thread.interrupt();
		}
	}
}
class MyThread extends Thread{
	boolean flag=true;
	public void run() {
		while(flag){
			System.out.println("==="+new Date()+"===");
			try{
				sleep(1000);
			}catch (InterruptedException e) {
				return;
			}
		}
	}
}
