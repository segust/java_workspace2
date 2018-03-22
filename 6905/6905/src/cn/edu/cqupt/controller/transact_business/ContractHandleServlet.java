package cn.edu.cqupt.controller.transact_business;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.ss.formula.ptg.Pxg3D;

import cn.edu.cqupt.beans.Attach;
import cn.edu.cqupt.beans.Contract;
import cn.edu.cqupt.beans.Log;
import cn.edu.cqupt.log.UserLogService;
import cn.edu.cqupt.service.fare_management.AttachService;
import cn.edu.cqupt.service.transact_business.ContractHandleService;
import cn.edu.cqupt.util.CollectionTypeChange;
import cn.edu.cqupt.util.CurrentUser;
import cn.edu.cqupt.util.MyDateFormat;
import cn.edu.cqupt.util.RSAUtil;

public class ContractHandleServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9017366345958064699L;

	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		String operate = request.getParameter("operate");
		String message = "";
		String path = "";
		// 获取当前版本（全局变量）
		ServletContext sctx = this.getServletContext();
		String version = (String) sctx.getInitParameter("version");

		// version==1表示是企业版
		if ("1".equals(version)) {
			// 举例：判断当前用户是否具有 业务办理 权限
			if (CurrentUser.isContractManage(request)) {
				if ("addcontract".equals(operate)) {
					boolean isAdd = true;
					// 增加操作日志
					boolean isUpdate = false;
					request.setAttribute("isAdd", isAdd);
					request.setAttribute("isUpdate", isUpdate);
					path = "/jsp/qy/transact_business/addContract.jsp";
				} else if ("ajaxUpload".equals(operate)) {
					boolean isAdd = true;
					message = this.addContract(request, response, isAdd).trim();
					this.saveLog(request, response, "新增合同", 0, "合同编号为："
							+ request.getParameter("contractid"));
					response.getWriter().write(message);
					path = "";
				} else if ("querycontract".equals(operate)) {
					List<Contract> contracts = new ArrayList<Contract>();
					// 这里还需要传回查询条件
					ContractHandleService service = new ContractHandleService();
					Map<String, String> condition = getCondition(request);
					int curPageNum = 0;
					int pageSize = 0;
					if (request.getParameter("curPageNum") == null
							|| request.getParameter("pageSize") == null) {
						curPageNum = 1;
						pageSize = 10;
					} else {
						curPageNum = Integer.parseInt(request
								.getParameter("curPageNum"));
						pageSize = Integer.parseInt(request
								.getParameter("pageSize"));
					}
					contracts = this.queryContract(request, response,
							curPageNum, pageSize);
					if (condition.size() == 0) {
						request.getAttribute("condition");
					}
					int count = service.querySum(condition);
					double contractPriceSum = this.getContractPriceSum(request,
							response);
					request.setAttribute("sum", count);
					request.setAttribute("condition", condition);
					request.setAttribute("curPageNum", curPageNum);
					request.setAttribute("pageSize", pageSize);
					request.setAttribute("contracts", contracts);
					// 计算当前页合同总金额
					double curPageContractPrice = 0;
					for (Contract contract : contracts) {
						curPageContractPrice += contract.getContractPrice();
					}
					request.setAttribute("curPageContractPrice",
							curPageContractPrice);
					request.setAttribute("contractPriceSum", contractPriceSum);
					path = "/jsp/qy/transact_business/queryContract.jsp?curPageNum="
							+ curPageNum + "&pageSize=" + pageSize;
				} else if ("updatecontract".equals(operate)) {
					boolean isUpdate = true;
					boolean isAdd = false;
					String contractId = request.getParameter("contractId");
					Contract contract = new Contract();
					ContractHandleService service = new ContractHandleService();
					contract = service.queryContractById(contractId);
					request.setAttribute("isAdd", isAdd);
					request.setAttribute("isUpdate", isUpdate);
					request.setAttribute("contract", contract);
					path = "/jsp/qy/transact_business/addContract.jsp";
				} else if ("updateform".equals(operate)) {
					ContractHandleService service = new ContractHandleService();
					message = this.updateContract(request, response, service);
					String contractId = request.getParameter("contractid");
					Contract contract = service.queryContractById(contractId);
					// 增加操作日志
					this.saveLog(request, response, "编辑合同", 0, contract.toString());
					response.setContentType("text/plain;charset=UTF-8");
					response.getWriter().write(message);
					path = "";
				} else if ("deleteAttach".equals(operate)) {
					String data = "0";
					String basePath = getServletContext().getRealPath("/");
					String contractId = request.getParameter("contractid");
					ContractHandleService service = new ContractHandleService();
					Contract contract = service.queryContractById(contractId);
					String save = service.findAttachByContractId(contractId);
					String hh = basePath + "\\upload\\" + save;
					File file = new File(hh);
					if (file.exists()) {
						if (file.delete()) {
							boolean flag = service.deleteAttah(contractId);
							if (flag) {
								data = "1";
								// 增加操作日志
								this.saveLog(request, response, "编辑合同附件", 0, contract.toString());
							}
						} else {
							int i = 0;
							while (i < 5) {
								System.gc();
								i++;
							}
							if (file.delete()) {
								boolean flag = service.deleteAttah(contractId);
								if (flag) {
									data = "1";
									// 增加操作日志
									this.saveLog(request, response, "编辑合同附件", 0, contract.toString());
								}
							} else {
								data = "2";
							}
						}
					}
					response.setContentType("text/plain;charset=UTF-8");
					response.getWriter().write(data);
				} else if ("checkpwd".equals(operate)) {
					String data = "删除失败";
					String pwd = request.getParameter("pwd");
					pwd = RSAUtil.string2RSA(pwd);
					HttpSession session = request.getSession();
					String myPwd = (String) session.getAttribute("password");
					if (myPwd.equals(pwd)) {
						String contractId = request.getParameter("contractid");
						ContractHandleService service = new ContractHandleService();
						Contract contract = service.queryContractById(contractId);
						boolean flag = service.DeleteContract(contractId);
						if (flag) {
							data = "删除成功";
							// 增加操作日志
							this.saveLog(request, response, "删除合同", 0, contract.toString());
						}
					}
					response.setContentType("text/plain;charset=UTF-8");
					response.getWriter().write(data);
				} else if ("gotoUpload".equals(operate)) {
					String contractId = request.getParameter("contractid");
					request.setAttribute("contractid", contractId);
					path = "/jsp/qy/transact_business/addattachment.jsp";
				}else if("isExistcId".equals(operate)) {
					String cId = request.getParameter("contractid");
					ContractHandleService cService = new ContractHandleService();
					boolean flag = cService.isExistCid(cId);
					//response.setContentType("text/plain;charset=UTF-8");
					if(flag) 
						response.getWriter().write("1");
					else
						response.getWriter().write("0");
				}else if("uploadAttach".equals(operate)) {
					boolean isAdd = false;
					message = this.addContract(request, response, isAdd);
					if ("1".equals(message)) {
						path = "jsp/qy/transact_business/uploadSuccess.jsp";
						// 增加操作日志
						this.saveLog(request, response, "上传合同附件", 0, "合同编号为："
								+ request.getParameter("contractid"));
					} else if ("0".equals(message)) {
						path = "";
					}
				} else {
					path = "/jsp/qy/transact_business/addContract.jsp";
					request.setAttribute("isAdd", true);
					request.setAttribute("isUpdate", false);
				}
				if (!"".equals(path)) {
					request.getRequestDispatcher(path).forward(request,
							response);
				} else {
					// response.sendRedirect("ContractHandleServlet?operate=querycontract");
				}
			} else {

			}
		} else {

		}
	}

	private String updateContract(HttpServletRequest request,
			HttpServletResponse response, ContractHandleService service) {
		String message = "";
		Contract contract = new Contract();
		contract.setContractId(request.getParameter("contractid"));
		if (request.getParameter("totalnumber") != null) {
			contract.setTotalNumber(Integer.parseInt(request
					.getParameter("totalnumber")));
		}
		if (request.getParameter("contractprice") != null) {
			contract.setContractPrice(Double.parseDouble(request
					.getParameter("contractprice")));
		}
		contract.setJDS(request.getParameter("jds"));
		if (request.getParameter("signdate") != null) {
			contract.setSignDate(MyDateFormat.changeStringToDate(request
					.getParameter("signdate")));
		}
		contract.setBuyer(request.getParameter("buyer"));
		boolean flag = service.UpdateContract(contract);
		if (flag) {
			message = "1";
		} else {
			message = "0";
		}
		return message;
	}

	private String addContract(HttpServletRequest request,
			HttpServletResponse response, boolean isAdd) {
		String message = "";
		boolean flag = isAdd;
		try {
			Contract contract = new Contract();
			contract.setContractId(request.getParameter("contractid"));
			String tNum = request.getParameter("totalnumber");
			if (tNum != null && !"".equals(tNum)) {
				contract.setTotalNumber(Integer.parseInt(tNum));
			}
			String price = request.getParameter("contractprice");
			if (price != null && !"".equals(price)) {
				contract.setContractPrice(Double.parseDouble(price));
			}
			contract.setJDS(request.getParameter("jds"));
			contract.setSignDate(MyDateFormat.changeStringToDate(request
					.getParameter("signdate")));
			contract.setBuyer(request.getParameter("buyer"));
			contract.setOwnUnit((String) request.getSession().getAttribute(
					"ownedUnit"));
			// 得到文件上传目录，将上传的文件保存至WEB-INF目录下，不允许外界访问，保证上传文件的安全
			String savePath = request.getSession().getServletContext()
					.getRealPath("/upload");
			// System.out.println(savePath);
			File file = new File(savePath);
			// 判断文件保存目录是否存在
			if (!file.exists() && !file.isDirectory()) {
				System.out.println(savePath + " 目录不存在，需要重新创建");
				file.mkdir();
			}
			// 使用Apache组件上传文件的步骤：
			// 1.创建一个DiskFileItemFactory
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// 2.创建一个文件上传解析器
			ServletFileUpload upload = new ServletFileUpload(factory);
			// 监听文件上传
			/*
			 * upload.setProgressListener(new ProgressListener() { public void
			 * update(long pBytesRead, long pContentLength, int arg2) {
			 * System.out.println("文件大小为" + pContentLength + ",当前已处理：" +
			 * pBytesRead); } });
			 */
			// 解决中文乱码
			upload.setHeaderEncoding("UTF-8");
			// 3.判断提交的数据是否合法
			if (!upload.isMultipartContent(request)) {
				// 按照传统方法获取
				return null;
			}
			// 4.使用解析器上传数据，解析返回的是List<FileItem>,FileItem对应一个form表单
			List<FileItem> list = upload.parseRequest(request);
			for (FileItem item : list) {
				// 如果封装的是普通项输入数据
				if (item.isFormField()) {
					// 如果是普通项
					if ("contractid".equals(item.getFieldName())) {
						contract.setContractId(item.getString("UTF-8"));
					}
					if ("totalnumber".equals(item.getFieldName())) {
						contract.setTotalNumber(Integer.parseInt(item
								.getString("UTF-8")));
					}
					if ("jds".equals(item.getFieldName())) {
						contract.setJDS(item.getString("UTF-8"));
					}
					if ("contractprice".equals(item.getFieldName())) {
						contract.setContractPrice(Double.parseDouble(item
								.getString("UTF-8")));
					}
					if ("signdate".equals(item.getFieldName())) {
						contract.setSignDate(MyDateFormat
								.changeStringToDate(item.getString("UTF-8")));
					}
					if ("buyer".equals(item.getFieldName())) {
						contract.setBuyer(item.getString("UTF-8"));
					}
				} else {
					// 如果封装的上传文件
					String fileName = item.getName();
					System.out.println(fileName);
					if (fileName == null || "".equals(fileName.trim())) {
						continue;
					}
					// 不同浏览器提交的文件名是不一样的，有的是带路径的
					// 处理文件路径部分，只保留文件名
					fileName = fileName
							.substring(fileName.lastIndexOf("\\") + 1);
					fileName = makeFileName(fileName);
					String realSavePath = makePath(fileName, savePath);
					/*
					 * //限制上传的文件只能是word String fileExtName =
					 * fileName.substring(fileName.lastIndexOf(".")+1);
					 * //如果需要限制上传的文件类型，那么可以通过文件的扩展名来判断上传的文件类型是否合法
					 * System.out.println("上传的文件的扩展名是："+fileExtName);
					 */
					// 获取上传文件的输入流
					InputStream in = item.getInputStream();
					// 创建一个文件输出流
					FileOutputStream out = new FileOutputStream(realSavePath
							+ "\\" + fileName);
					// 创建一个缓冲区
					byte buff[] = new byte[1024];
					// 判断输入流中数据是否读完标识
					int len = 0;
					// 循环将输入流读入缓冲区中
					while ((len = in.read(buff)) > 0) {
						out.write(buff, 0, len);
					}
					if (fileName != null) {
						// 修改文件保存地址
						// String path1 =
						// realSavePath.substring(realSavePath.lastIndexOf("\\"),realSavePath.length());
						contract.setAttachment(fileName);
					}
					in.close();
					out.close();
					// 删除临时文件
					item.delete();
				}
			}
			ContractHandleService service = new ContractHandleService();
			// 如果是增加合同
			if (isAdd) {
				contract.setOwnUnit((String) request.getSession().getAttribute(
						"ownedUnit"));
				flag = service.saveContract(contract);
			} else {
				// 如果只增加附件
				flag = service.uploadAttach(contract.getContractId(),
						contract.getAttachment());
			}

			if (flag) {
				message = "1";
			} else {
				message = "0";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return message;
	}

	private String makePath(String fileName, String savePath) {
		// 得到hashcode值
		int hashcode = fileName.hashCode();
		int dir1 = hashcode & 0xf; // 0--15
		int dir2 = (hashcode & 0xf0) >> 4;// 0--15
		// 构建新的保存目录
		// String dir = savePath + "\\" + dir1 +"\\" + dir2;
		String dir = savePath;
		// File既可以是文件也可以是目录
		File file = new File(dir);
		if (!file.exists()) {
			file.mkdirs();
		}
		return dir;
	}

	/**
	 * 设置一个不重复的文件名 uuid + 文件名
	 * 
	 * @return
	 */
	private String makeFileName(String fileName) {
		return UUID.randomUUID().toString() + "_" + fileName;
	}

	private List<Contract> queryContract(HttpServletRequest request,
			HttpServletResponse response, int curPageNum, int pageSize) {
		List<Contract> result = new ArrayList<Contract>();
		ContractHandleService service = new ContractHandleService();
		Map<String, String> condition = getCondition(request);
		result = service.queryContract(condition, curPageNum, pageSize);
		return result;
	}

	private double getContractPriceSum(HttpServletRequest request,
			HttpServletResponse response) {
		double contractPriceSum = 0.0;
		ContractHandleService service = new ContractHandleService();
		Map<String, String> condition = getCondition(request);
		contractPriceSum = service.getContractPriceSum(condition);
		return contractPriceSum;
	}

	private Map<String, String> getCondition(HttpServletRequest request) {
		Map<String, String> condition = new HashMap<String, String>();
		String contractId = request.getParameter("contractid");
		String productModel = request.getParameter("productmodel");
		String unitName = request.getParameter("unitname");
		String signDate = request.getParameter("signdate");
		if (!"".equals(contractId) && contractId != null
				&& !"null".equals(contractId)) {
			condition.put("c.contractId", contractId);
		}
		if (!"".equals(productModel) && productModel != null
				&& !"null".equals(productModel)) {
			condition.put("productModel", productModel);
		}
		if (!"".equals(unitName) && unitName != null
				&& !"null".equals(unitName)) {
			condition.put("productUnit", unitName);
		}
		if (!"".equals(signDate) && signDate != null
				&& !"null".equals(signDate)) {
			String[] temp = signDate.split(" ");
			condition.put("signDate", temp[0]);
		}
		return condition;
	}

	/**
	 * 记录日志
	 * 
	 * @param operateType
	 * @return
	 */
	private boolean saveLog(HttpServletRequest request,
			HttpServletResponse response, String operateType, long pId,
			String remark) {
		boolean flag = false;
		HttpSession session = request.getSession();
		Log log = new Log();
		log.setUserName((String) session.getAttribute("username"));
		log.setOperateTime(new Date());
		log.setOperateType(operateType);
		log.setProductId(pId);
		log.setRemark(remark);
		flag = UserLogService.SaveOperateLog(log);
		return flag;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}

}
