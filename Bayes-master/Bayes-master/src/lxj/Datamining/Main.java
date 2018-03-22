package lxj.Datamining;

import java.util.ArrayList;
import java.util.Scanner;

import lxj.tool.DecimalCalculate;

public class Main {
	public static void main(String[] args){
		ArrayList<ArrayList<String>> trainList ;
		ArrayList<ArrayList<String>> testList ;
		
		String originalTrain = "file/adult.data";
		String processedTrain = "process/adultResult.txt";
		String originalTest = "file/adult1000.test";
		String originalTest100 = "file/adult100.test";
		String processedTest = "process/adult1000Result.txt";
		String processedTest100 = "process/adult100Result.txt";
		
		 String finalStr = "";
		 int wrong_number = 0; //��¼��ȷ������
		 double finalData = 0.0; //������
		 
		 boolean type = false; //�Ƿ������
		 boolean flag = false; //��־���Ե�����
		 for(int i = 0;i < 2;i++){
			 if(i == 0){
				 System.out.println("��ѡ�������������yΪ100������nΪ1000��");
				 Scanner scanner = new Scanner(System.in);
				 String str = scanner.next();
				 if(str.equals("y")){
					 flag = true; 
				 }else if(str.equals("n")){
					 flag = false; 
				 }else{
					 i = -1;
					 System.out.println("������������������");
				 }
			 }else{
				 System.out.println("��ѡ���Ƿ�ȥ�룬yΪ�ǣ�nΪ��");
				 
				 Scanner scanner = new Scanner(System.in); 
				 String str = scanner.next();
				 if(str.equals("y")){
					 type = true; 
				 }else if(str.equals("n")){
					 type = false; 
				 }else{
					 i = 0;
					 System.out.println("������������������");
				 }
			 }
		 }
		 System.out.println("�����ĵȴ���������");
		 if(type){
				System.out.println("�������������");
			}else{
				System.out.println("û�������������");
			}
		PreRead convert = new PreRead();
		convert.readFile(originalTrain, processedTrain,type); //ѵ������
		if(!flag){
			convert.readFile(originalTest, processedTest,type); //1000��������
			testList = convert.readTest(processedTest);
		}else{
			convert.readFile(originalTest100, processedTest100,type); //100����������
			testList = convert.readTest(processedTest100);
		}
		
		trainList =convert.readTest(processedTrain);	
		Bayes bayes = new Bayes();
		for(int i = 0;i < testList.size();i++){
			ArrayList<String> tmp = new ArrayList<String>();
			tmp = testList.get(i);
			String label = tmp.get(tmp.size()-1);
			tmp.remove(tmp.size() - 1);
			finalStr = bayes.predictClass(trainList, tmp);
			
			if(!label.equals(finalStr)){
				wrong_number ++;
			}
		}
		//finalData = DecimalCalculate.div(count, testList.size(),3); 
		System.out.println("Ԥ�Դ��������"+ wrong_number+", �����ܸ���"+testList.size());
		System.out.println("��ȷ��Ϊ��" + (DecimalCalculate.sub(1.00000000, DecimalCalculate.div(wrong_number, testList.size())))*100 + "%");
	}
}
