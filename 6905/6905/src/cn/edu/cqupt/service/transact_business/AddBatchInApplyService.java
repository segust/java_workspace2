package cn.edu.cqupt.service.transact_business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.cqupt.dao.ProductDAO;

public class AddBatchInApplyService {

	AddInApplyService singleService = new AddInApplyService();
	/**
	 * 该方法需要前台判断用户填写的数量是否多于数据库中已有的符合要求的产品数量，如果不符合，
	 * 直接在前台提示输入错误。这里只考虑用户填写的数量是符合要求的的情况。
	 * @param requests用户提交的信息，包括产品的基本信息和产品数量，产品编号范围
	 * @return
	 */
	public boolean storeBatchInApply(HashMap<String, Object> requests)
	{
		String patternString = "([^0-9]*)(0*)([0-9]+)([^0-9]*)";
		Pattern pattern = null;
		pattern = Pattern.compile(patternString);
		int start = 0;
		int end = 0;
		String str1 = (String) requests.get("productCode1");
		String str2 = (String) requests.get("productCode2");
		int number = Integer.parseInt(requests.get("num") + "");
		if(str2.equals(""))
		{
			Matcher matcher1 = pattern.matcher(str1);
			if(matcher1.matches())
			{
				System.out.println("Matches");
				start = Integer.parseInt(matcher1.group(3));
				String left = matcher1.group(1);
				String zeros = matcher1.group(2);
				String right = matcher1.group(4);
				List<String> info = new ArrayList<String>();
				info.add((String) requests.get("contractId"));//0
				info.add("新入库");//1
				info.add((String) requests.get("name"));//2
				info.add((String) requests.get("model"));//3
				info.add((String) requests.get("wholeName"));//4
				info.add((String) requests.get("unit"));//5
				info.add((String) requests.get("measure"));//6
				info.add(requests.get("price") + "");//7
				info.add( requests.get("num") + "");//8
				info.add((String) requests.get("PMNM"));//9
				info.add((String) requests.get("manufacturer"));//10
				info.add((String) requests.get("keeper"));//11
				info.add((String) requests.get("location"));//12
				info.add((String) requests.get("maintainCycle"));//13
				info.add((String) requests.get("producedDate"));//14
				info.add(left + zeros + start + right);//15productCode
				info.add((String)requests.get("batch"));//16
				info.add((String)requests.get("deviceNo"));//17
				info.add((String)requests.get("storageTime"));//18
				info.add((String)requests.get("remark"));//19
				
				List<String> productCodes = new ArrayList<String>();
				for(int i = 0; i < number; i++)
				{
					productCodes.add(left + zeros + (start + i) + right);
					
				}
					
				if(!singleService.inApplyBatchService(info, number, productCodes))//批量入库
				{
					System.out.println("singleInApplyFailed");
					return false;
				}
						
			}
			else
			{
				System.out.println("Not Match");
				return false;
			}
				
		}
		else
		{
			Matcher matcher1 = pattern.matcher(str1);
			Matcher matcher2 = pattern.matcher(str2);
			if(matcher1.matches() && matcher2.matches())
			{
				start = Integer.parseInt(matcher1.group(3));
				end = Integer.parseInt(matcher2.group(3));
				if(end < start)
					return false;
				String left1 = matcher1.group(1);
				String right1 = matcher1.group(4);
				String zeros1 = matcher1.group(2);
				String zeros2 = matcher2.group(2);
				String left2 = matcher2.group(1);
				String right2 = matcher2.group(4);
				if(!left1.equals(left2) || !right1.equals(right2) || //判断产品编号所示的产品数量跟用户填写的数量是否冲突
						((end - start + 1) < number) || (zeros1 + start).length() != (zeros2 + end).length())//以及判断产品编号是不是除了中间的数字其他都是一样的
					
				{
					System.out.println("start:" + start + ",end:" + end + ",number:" + number);
					System.out.println("输入错误");
					return false;
				}
				List<String> info = new ArrayList<String>();
				info.add((String) requests.get("contractId"));//0
				info.add("新入库");//1
				info.add((String) requests.get("name"));//2
				info.add((String) requests.get("model"));//3
				info.add((String) requests.get("wholeName"));//4
				info.add((String) requests.get("unit"));//5
				info.add((String) requests.get("measure"));//6
				info.add(requests.get("price") + "");//7
				info.add(requests.get("num") + "");//8
				info.add((String) requests.get("PMNM"));//9
				info.add((String) requests.get("manufacturer"));//10
				info.add((String) requests.get("keeper"));//11
				info.add((String) requests.get("location"));//12
				info.add((String) requests.get("maintainCycle"));//13
				info.add((String) requests.get("producedDate"));//14
				info.add(left1 + zeros1 + start + right1);//15productCode
				info.add((String)requests.get("batch"));//16
				info.add((String)requests.get("deviceNo"));//17
				info.add((String)requests.get("storageTime"));//18
				info.add((String)requests.get("remark"));//19
				List<String> productCodes = new ArrayList<String>();
				System.out.println("number:" + number);
				
				//下面开始构造产品编号
				String group2 = matcher1.group(2);
				for(int i = start; i < (start + number); i++)
				{
					String temp2 = "";
					if((i + "").length() > (start + "").length())
					{
						if(matcher1.group(2).length() < ((i + "").length() - (start + "").length()))
							System.out.println("输入的位数不够，或者是代码的范围太大了。");
						temp2 = group2.substring((i + "").length() - (start + "").length(), group2.length());
						productCodes.add(matcher1.group(1) + temp2 + i + matcher1.group(4));
					}
					else
						productCodes.add(matcher1.group(1) + group2 + i + matcher1.group(4));
					
				}
				for(int i = 0; i < number; i++)
				{
					System.out.println(productCodes.get(i) + ",number:" + number);
				}
					if(!singleService.inApplyBatchService(info, number, productCodes))//批量入库
					{
						System.out.println("singleInApplyFailed");
						return false;
					}
			}
			else
			{
				System.out.println("Not Match");
				return false;
			}
				
		}
		return true;
	}
	
	public boolean storeBatchDeviceNo(List<HashMap<String, String>> in)
	{
		ProductDAO dao = new ProductDAO();
		for(int i = 0; i < in.size(); i++)
		{
			String productModel = in.get(i).get("productModel");
			String start = in.get(i).get("startNo");
			String end = in.get(i).get("endNo");
			String contractId = in.get(i).get("contractId");
			String[] deviceNoStream = getDeviceNoStream(start, end);
			System.out.println("机号个数:" + deviceNoStream.length);
			String productName = in.get(i).get("productName");
			//String productUnit = in.get(i).get("productUnit");
			String productUnit = "";
			//检查输入deviceNo是否合法，这一步换到serlvet里面去检查了
			//检查deviceNo是否重复，如果重复返回true，这一步恨到servlet里面去检查了
			int[] ids = getProductIds//先找到可以修改的产品的id
					(productModel, contractId, productName, productUnit, deviceNoStream.length);
			
			for(int j = 0; j < ids.length; j++)
				System.out.println(ids[j]);
			if(!dao.storeDeviceNoBatch(ids, deviceNoStream))//如果任何一次操作失败，都返回失败
				return false;
		}
		return true;
	}
	
	private int[] getProductIds(String productModel, String contractId, 
			String productName, String productUnit, int number)
	{
		System.out.println("productModel:" + productModel + ",contractId:" + contractId + ",productName:"
	+ productName + ",productUnit" + ",number" + number); 
		ProductDAO dao = new ProductDAO();
		ArrayList<Integer> list = dao.
				getProductIdsByContractIdProductModelProductNameAndProductUnit
				(contractId, productModel, productUnit, productName, number);
		int[] ids = new int[number];
		for(int i = 0; i < number; i++)
			ids[i] = list.get(i);
		return ids;
	}
	
	public boolean checkDeviceNoAlreadyExist(String productModel, String[] deviceNoStream)
	{
		ProductDAO dao = new ProductDAO();
		return dao.checkDeviceNoAlreadyExist(productModel, deviceNoStream);
	}
	
	public String[] getDeviceNoStream(String startNoStr, String endNoStr)
	{
		if("".equals(startNoStr) || startNoStr == null)
			return null;
		if("".equals(endNoStr) || endNoStr == null)
		{
			String[] result = new String[1];
			result[0] = startNoStr;
			return result;
		}
		String patternStr = "([^0-9]*)(0*)([0-9]*)([^0-9]*)";
		Pattern pattern = Pattern.compile(patternStr);
		String startStr = "";
		String endStr;
		int startNo = 0;
		int endNo =0;
		String before1 = "";
		String zeros1 = "";
		String after1 = "";
		String before2 = "";
		String zeros2 = "";
		String after2 = "";
		String testStr = startNoStr;
		String testStr2 = endNoStr;
		Matcher matcher1 = pattern.matcher(testStr);
		if(matcher1.find())
		{
			//System.out.println(matcher1.group(3));
			before1 = matcher1.group(1);
			zeros1 = matcher1.group(2);
			startStr = matcher1.group(3);
			startNo = Integer.parseInt(startStr);
			after1 = matcher1.group(4);
		}
		else
		{
			System.out.println("nomatch");
			return null;
		}
		Matcher matcher2 = pattern.matcher(testStr2);
		if(matcher2.find())
		{
			//System.out.println(matcher2.group(2));
			before2 = matcher2.group(1);
			zeros2 = matcher2.group(2);
			after2 = matcher2.group(4);
			endStr = matcher2.group(3);
			endNo = Integer.parseInt(endStr);
		}
		else
		{
			System.out.println("nomatch");
			return null;
		}
			
		if(endNo >= startNo && before1.equals(before2) && after1.equals(after2))
		{
			String[] strs = new String[endNo - startNo + 1];
			for(int i = 0; i <= endNo - startNo; i++)
			{
				strs[i] = before1 + zeros1 + (i + startNo) + after1;
				if(strs[i].length() >= testStr.length())
				{
					int lenDis = strs[i].length() - testStr.length();
					if(zeros1.length() > lenDis)
						strs[i] = before1 + zeros1.substring(0, zeros1.length() - lenDis)
						+ (i + startNo) + after1;
					else
					{
						strs[i] = before1 + (i + startNo) + after1;
					}
				}
				//System.out.println(strs[i]);
			}
			return strs;	
		}
		return null;
	}
	
	public static void main(String[] args)
	{
		AddBatchInApplyService addBatchInApplyService = new AddBatchInApplyService();
		String[] test = addBatchInApplyService.getDeviceNoStream("huang001huhu", "huang110huhu");
	}
}
