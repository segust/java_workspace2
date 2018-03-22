package model;

public class Book {
	private String bookname;
	private String oldbookname;
	private String author;
	private String price;
	private String press;

	public Book(String bookinfo) {
		bookname = bookinfo;
	}

	public Book(String bookinfo,int i) {
		String[] book = bookinfo.split("#");
		bookname = book[0];
		author = book[1];
		price = book[2];
		press = book[3];
	}

	public Book(String bookinfo,int i,int j) {
		String[] book = bookinfo.split("#");
		oldbookname = book[0];
		bookname = book[1];
		author = book[2];
		price = book[3];
		press = book[4];}
	public String getBookname() {
		return bookname;
	}

	public String getAuthor() {
		return author;
	}

	public String getPrice() {
		return price;
	}

	public String getPress() {
		return press;
	}

	public String getOldbookname() {
		return oldbookname;
	}

}
