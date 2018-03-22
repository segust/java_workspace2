package menu;

import java.util.*;

import action.Action;

public class Start {
	Scanner in=new Scanner(System.in);
	public static void main(String args[]){
		new Start();
	}
	public Start(){
		System.out.println("欢迎进入图书管理系统");
		System.out.println("*******************");
		int i;
		do{
			i=choice();
			switch(i){
				case 1: 
					addBook();
					break;
				case 2: 
					deleBook();
					break;
				case 3:
					modifyBook();
					break;
				case 4:
					searchBook();
					break;
				case 5:
					showList();
					break;
				case 0:
					break;
			}
		}while(i!=0);		
	}
	
	
	@SuppressWarnings("resource")
	public int choice(){
		Scanner in=new Scanner(System.in);
		System.out.println("1.增加图书");
		System.out.println("2.删除图书");
		System.out.println("3.修改图书");
		System.out.println("4.查询图书");
		System.out.println("5.图书列表");
		System.out.println("0.退出系统");
		int choice=in.nextInt();
		return choice;
	}
	
	
	void addBook(){
		System.out.println("请输入增加图书的名字");
		String BookName=in.next();
		System.out.println("请输入图书作者");
		String BookAuthor=in.next();
		System.out.println("价格");
		int Price=in.nextInt();
		Action act=new Action();
		act.addBook(BookName, BookAuthor, Price);
	}

	void deleBook(){
		System.out.println("请输入要删除的图书名称");
		String BookName=in.nextLine();
		Action act=new Action();
		act.deleBook(BookName);	
	}
	void modifyBook(){
		System.out.println("请输入需要修改的图书名称");
		String oldBookName=in.nextLine();
		System.out.println("新的图书名称");
		String BookName=in.nextLine();
		System.out.println("作者");
		String BookAuthor=in.nextLine();
		System.out.println("价格");
		int Price=in.nextInt();
		Action act=new Action();
		act.modifyBook(oldBookName, BookName, BookAuthor, Price);
	}
	
	void searchBook(){
		System.out.println("请输入需要查询的图书名称");
		String BookName=in.nextLine();
		Action act=new Action();
		act.searchBook(BookName);
	}

	void showList(){
		System.out.println("ID\t名称\t作者\t价格");
		Action act=new Action();
		act.showList();
	}

}