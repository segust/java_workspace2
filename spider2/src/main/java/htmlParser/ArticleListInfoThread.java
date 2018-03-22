package htmlParser;

public class ArticleListInfoThread implements Runnable{
	private int pageStart;
	
	
	
	public ArticleListInfoThread(int pageStart) {
		super();
		this.pageStart = pageStart;
	}



	@Override
	public void run() {
		// TODO Auto-generated method stub
		ArticleListInfo.getArticleList(this.pageStart);
		
	}
	
}
