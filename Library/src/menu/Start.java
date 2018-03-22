package menu;

import java.util.*;

import action.Action;

public class Start {
	Scanner in=new Scanner(System.in);
	public static void main(String args[]){
		new Start();
	}
	public Start(){
		System.out.println("��ӭ����ͼ�����ϵͳ");
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
		System.out.println("1.����ͼ��");
		System.out.println("2.ɾ��ͼ��");
		System.out.println("3.�޸�ͼ��");
		System.out.println("4.��ѯͼ��");
		System.out.println("5.ͼ���б�");
		System.out.println("0.�˳�ϵͳ");
		int choice=in.nextInt();
		return choice;
	}
	
	
	void addBook(){
		System.out.println("����������ͼ�������");
		String BookName=in.next();
		System.out.println("������ͼ������");
		String BookAuthor=in.next();
		System.out.println("�۸�");
		int Price=in.nextInt();
		Action act=new Action();
		act.addBook(BookName, BookAuthor, Price);
	}

	void deleBook(){
		System.out.println("������Ҫɾ����ͼ������");
		String BookName=in.nextLine();
		Action act=new Action();
		act.deleBook(BookName);	
	}
	void modifyBook(){
		System.out.println("��������Ҫ�޸ĵ�ͼ������");
		String oldBookName=in.nextLine();
		System.out.println("�µ�ͼ������");
		String BookName=in.nextLine();
		System.out.println("����");
		String BookAuthor=in.nextLine();
		System.out.println("�۸�");
		int Price=in.nextInt();
		Action act=new Action();
		act.modifyBook(oldBookName, BookName, BookAuthor, Price);
	}
	
	void searchBook(){
		System.out.println("��������Ҫ��ѯ��ͼ������");
		String BookName=in.nextLine();
		Action act=new Action();
		act.searchBook(BookName);
	}

	void showList(){
		System.out.println("ID\t����\t����\t�۸�");
		Action act=new Action();
		act.showList();
	}

}