package util;

import java.util.regex.Pattern;
/**
 * 工具类
 * */
public class AlgorithmUtil {

	/**
	 * 随机获取训练集编号
	 * 生成不重复随机数列：洗牌算法
	 * number 获得的数量
	 * range 随机数的范围
	 * */
	public int[] getTrainingId(int number, int range){
		int[] trainingId = new int[number];//训练集编号
		int[] rangeId = new int[range];//范围集合编号
		for (int i = 0; i < rangeId.length; i++) {//初始化
			rangeId[i] = i;
		}
		
		for (int i = 0; i < number; i++) {//产生不重复随机数列
			int id = (int) (Math.random()*range);
			trainingId[i] = rangeId[id];
			rangeId[id] = rangeId[range-1];//将最后一个数换至被选走的数的位置
			range--;
		}//此时，rangId[]中前range个编号就是测试集了
		
		return trainingId;
	}
	
	/**
	 * 获取剩余编号即测试集
	 * trainingId 除去此数组中的数
	 * range 随机数的范围
	 * */
	public int[] getTestId(int[] trainingId, int range){
		int[] testId = new int[range-trainingId.length];//测试集编号
		int[] rangeId = new int[range];//范围集合编号
		for (int i = 0; i < rangeId.length; i++) {//初始化
			rangeId[i] = i;
		}

		for (int i = 0; i < trainingId.length; i++) {//排除训练集编号
			rangeId[trainingId[i]] = -1;//令其等于-1，表示不要
		}
		int count = 0;
		for (int i = 0; i < rangeId.length; i++) {//赋值
			if(rangeId[i] != -1){
				testId[i-count] = rangeId[i];
			}else{
				count++;
			}
		}
		
		return testId;
	}
	/**
	 * 清理数据
	 * */
	public String cleanData(String info){
		//去标点
        Pattern p = Pattern.compile("\\W");
        String[] tempInfo = p.split(info);
        info = "";
        //去停用词
        //去除长度小于3的单词
        for (int i = 0; i < tempInfo.length; i++) {
        	if(tempInfo[i].length() >= 3)
        		info += tempInfo[i]+" ";
		}
        //去大写
        info = info.toLowerCase();
        
        if(info.equals("")){//经观察，处理后有25条数据为空
        	return null;
        }else{
        	return info;
        }
	}
}
