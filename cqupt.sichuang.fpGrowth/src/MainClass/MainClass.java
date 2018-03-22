package MainClass;

import java.util.ArrayList;
import java.util.Scanner;

import com.sichuang.fpGrowth.Tree;

import cqupt.sichuang.Route.RouteDao;

/**
 * @author
 * @date 2017年7月25日 下午7:57:24
 * @parameter
 */
public class MainClass {

	public static void main(String[] args) {
		ArrayList<ArrayList<String>> itemList = new ArrayList<ArrayList<String>>();
		System.out.println("输入想要查询的车牌号码：");
		Scanner scan = new Scanner(System.in);
		String license_no = scan.nextLine();
		scan.close();
		RouteDao userRouteList = new RouteDao(license_no);
		itemList = userRouteList.getDayClause();
		Tree tree = new Tree(itemList);
		tree.show();
	}

}
