package pagerank;
/**
 * 配置类
 * */
public class Configuration {
	public static final String DATA_PATH ="data/web.txt";           //数据相对路径
	public static final String RESULT_PATH="data/result_pr.txt";    //实验结果路径
	
	public static final double ALPHA = 0.5;                   //alph初始
	public static final double COVERAGE_VALUE = 0.000000001; //收敛参数
	public static final int MAX_ITER = 100;          //初始迭代的最大次数
	public static final int TOP_K = 50;                      //初始控制台打印的top k数据
}
