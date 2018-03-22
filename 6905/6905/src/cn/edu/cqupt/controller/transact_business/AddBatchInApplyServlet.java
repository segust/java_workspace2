package cn.edu.cqupt.controller.transact_business;

import java.io.IOException;
import java.net.HttpRetryException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import cn.edu.cqupt.service.transact_business.AddBatchInApplyService;
import cn.edu.cqupt.util.CurrentUser;
import cn.edu.cqupt.util.MyDateFormat;

public class AddBatchInApplyServlet extends HttpServlet {

	private AddBatchInApplyService service;
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletContext context = getServletContext();
		this.service = new AddBatchInApplyService();
		String operate = request.getParameter("operate");
		String version = (String) this.getServletContext().getInitParameter("version");
		if("1".equals(version)) {
			if(CurrentUser.isContractManage(request)) {
				if(operate.equalsIgnoreCase("addBatchInApply"))
				{
					String name = request.getParameter("productName");
					if(name == null)
						name = "";
					String model = request.getParameter("productModel");
					if(model == null)
						model = "";
					String wholeName = request.getParameter("wholeName");
					if(wholeName == null)
						wholeName = "";
					String unit = request.getParameter("productUnit");
					if(unit == null)
						unit = "";
					String measure = request.getParameter("measureUnit");
					if(measure == null)
						measure = "";
					String price = request.getParameter("productPrice");
					if(price == null)
						price = "";
					String num = request.getParameter("num");
					if(num == null)
						num = "";
					String PMNM = request.getParameter("PMNM");
					if(PMNM == null)
						PMNM = "";
					String manufacturer = request.getParameter("manufacturer");
					if(manufacturer == null)
						name = "";
					String keeper = request.getParameter("keeper");
					if(keeper == null)
						keeper = "";
					String location = request.getParameter("location");
					if(location == null)
						location = "";
					String maintainCycle = request.getParameter("maintainCycle");
					if(maintainCycle == null)
						maintainCycle = "";
					String producedDate = request.getParameter("producedDate");
					if(producedDate == null)
						producedDate = "";
					String productCode1 = request.getParameter("productCode1");
					if(productCode1 == null)
						productCode1 = "";
					String productCode2 = request.getParameter("productCode2");
					if(productCode2 == null)
						productCode2 = "";
					String contractId = request.getParameter("contractId");
					if(contractId == null)
						contractId = "";
					String batch = request.getParameter("batch");
					if(batch == null)
						batch = "";
					String deviceNo = request.getParameter("deviceNo");
					if(deviceNo == null)
						deviceNo = "";
					String storageTime = request.getParameter("storageTime");
					if(storageTime == null) {
						storageTime = "";
					}
					String remark = request.getParameter("remark");
					if(remark == null)
						remark ="";
					HashMap<String, Object> requests = new HashMap<String, Object>();
					requests.put("contractId", contractId);
					requests.put("name", name);
					requests.put("model", model);
					requests.put("wholeName", wholeName);
					requests.put("unit", unit);
					requests.put("measure", measure);
					requests.put("price", Double.parseDouble(price));
					requests.put("num", Integer.parseInt(num));
					requests.put("PMNM", PMNM);
					requests.put("manufacturer", manufacturer);
					requests.put("keeper", keeper);
					requests.put("location", location);
					requests.put("maintainCycle", maintainCycle);
					requests.put("productCode1", productCode1);
					requests.put("productCode2", productCode2);
					requests.put("producedDate", producedDate);
					requests.put("batch", batch);
					requests.put("deviceNo", deviceNo);
					requests.put("storageTime", storageTime);
					requests.put("remark",remark);
					boolean flag = service.storeBatchInApply(requests);
					int cou = 0;
					if(flag) {
						cou = 1;
					}
					request.setAttribute("flag", cou);
					request.setAttribute("contractid", contractId);
					request.setAttribute("isAddBatch", false);
					RequestDispatcher disp = context.getRequestDispatcher("/jsp/qy/transact_business/batchApply.jsp");
					disp.forward(request, response);
				}
				
				else if(operate.equalsIgnoreCase("addBatchDeviceNo"))
				{
					response.setContentType("text/plain,charset=utf-8");
					AddBatchInApplyService addBatchInApplyService = new AddBatchInApplyService();
					String data = request.getParameter("data");
					System.out.println("data:"+data);
					List<HashMap<String, String>> in = new ArrayList<HashMap<String, String>>();
//					System.out.println("OK:" + data.substring(1, data.length() - 1));
					String[] oneLine = data.substring(1, data.length() - 1).split("],");
//					System.out.println(oneLine.length);
					String message = "";
					for (int i = 0; i < oneLine.length; i++) {
						
						if(i == oneLine.length - 1)
							oneLine[i] = oneLine[i].substring(1, oneLine[i].length() - 1);
						else
							oneLine[i] = oneLine[i].substring(1, oneLine[i].length());
//						System.out.println("第" + i + "行，" + oneLine[i]);
						HashMap<String, String> piece = new HashMap<String, String>();
						String[] elems = oneLine[i].split(",");
//						for(int j = 0 ; j < elems.length; j++)
//							System.out.println("elems" + j + ":" + elems[j].substring(1, elems[j].length() - 1));
						piece.put("contractId", elems[0].substring(1, elems[0].length() - 1));
						piece.put("productModel", elems[1].substring(1, elems[1].length() - 1).trim());
						// edit by liuyutian 20151209
//						System.out.println("elem2:" + elems[2].substring(1, elems[2].length() - 1));
						//String[] productInfo = elems[2].substring(1, elems[2].length() - 1).split("——");
//						System.out.println("productInfo[0]:" + productInfo[0] + ",productInfo[1]:" + productInfo[1]);
						//piece.put("productName", productInfo[0]);
						//piece.put("productUnit", productInfo[1]);
						
						piece.put("productName", elems[2].substring(1, elems[2].length() - 1));
						piece.put("startNo", elems[12].substring(1, elems[12].length() - 1).trim());
						String endNo = elems[13].substring(1, elems[13].length() - 1).trim();
						if(endNo.equals("不可用"))
							endNo = "";
						piece.put("endNo", endNo);
						in.add(piece);
//						System.out.println("呵呵，" + elems[0].substring(1, elems[0].length() - 1) + "," + elems[1].substring(1, elems[1].length() - 1) + 
//								"," + productInfo[0] + "," + productInfo[1] + "," +
//								elems[11].substring(1, elems[11].length() - 1) + "," + elems[12].substring(1, elems[12].length() - 1));
						String[] devideNos = addBatchInApplyService.getDeviceNoStream(piece.get("startNo"), piece.get("endNo"));
						
						
						if(devideNos == null)
						{
							message = "机号输入有误，前后两个机号不匹配！";
							response.getWriter().write(message);
							return;//机号输入有误
						}
						System.out.println("机号输入无误:");
//						for(int k = 0; k < devideNos.length; k++)
//							System.out.println("jihao:" + devideNos[k]);
						boolean deviceNoFlag = addBatchInApplyService.checkDeviceNoAlreadyExist(piece.get("productModel"), devideNos);
						if(deviceNoFlag)
						{
							message = "机号重复！";
							response.getWriter().write(message);
							return;//机号重复
						}
						System.out.println("机号不重复");
					}
					boolean flag = addBatchInApplyService.storeBatchDeviceNo(in);
					if(flag)
					{
						message = "成功！";
						response.getWriter().write(message);
						return;//正确
					}
						
					else
					{
						message = "插入失败！";
						response.getWriter().write(message);
						return;//错误
					}
						
					
				}
				
				else
				{
					
				}
			}
		}
		
	}
	
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}
	
	public void service(ServletRequest request, ServletResponse response)
			throws ServletException, IOException
	{
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		this.doGet(req, resp);
	}
}
