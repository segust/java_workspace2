package _7_22;

/**
 * @author
 * @date 2017年7月22日 下午4:09:02
 * @parameter
 */
public class tst {
	public static void main(String[] args) {
		int count = 0;
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 28; j++) {
				count++;
				System.out.println("select * from (select * from `" + (i * 100 + j)
						+ "` where data_source<>'HK' AND data_source<>'SC%')f" + count + "UNION ALL");
			}
		}
	}
}
