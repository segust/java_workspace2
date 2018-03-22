package cn.edu.cqupt.controller.qualification_management;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import cn.edu.cqupt.service.qualification_management.InfoService;
import cn.edu.cqupt.util.CurrentUser;
import cn.edu.cqupt.util.GetError;

public class InfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private boolean flag = false;
	private String url = null;
	private String nextLevel = null;
	private String curLevel = null; // 要查询的层次
	private String operate = null; // 操作类型
	private String curLevelName = null; // 1.在查询名称中，指查询层次的上一级名称；2.在查询所有信息中，指查询层次的名称
	private InfoService is = null;

	public InfoServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	private String curFolder = ""; // curFolder 当前版本的页面所在文件夹
	private String rootLevel = ""; // rootLevel 当前版本根目录的级别
	private String version = ""; // version 当前版本

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		is = new InfoService();
		curLevel = req.getParameter("curLevel");
		nextLevel = req.getParameter("nextLevel");
		operate = req.getParameter("operate");

		// 获取当前版本（全局变量）
		ServletContext sctx = this.getServletContext();
		version = (String) sctx.getInitParameter("version");
		req.setAttribute("version", version);

		if (CurrentUser.isQualificationManage(req)) {
			if (version.equals("2")) {
				rootLevel = "jds";
				curFolder = "jds";
				forwardByOperate(req, resp);
			} else if (version.equals("3")) {
				rootLevel = "jdj";
				curFolder = "jdj";
				forwardByOperate(req, resp);
			} else if (version.equals("4")) {
				rootLevel = "zhj";
				curFolder = "zhj";
				forwardByOperate(req, resp);
			}
		} else {
			// 当前用户没有权限
			GetError.limitVisit(req, resp);
		}

	}

	// 根据操作实现不同跳转
	private void forwardByOperate(HttpServletRequest req,
			HttpServletResponse resp) throws IOException, ServletException {
		if (operate.equals("searchContent")) {
			// 导航栏一进入就是当前版本的根目录
			int nextLevelContentSize = ifExistNextLevelContent(req, resp);
			resp.getWriter().print(nextLevelContentSize);
		}
		if (operate.equals("searchContentAndInfo")) {
			// 代储企业下面没有子目录,nextLevel:null
			if (nextLevel == null) {
				String curLevelInfoStr = searchCurLevelInfo(req, resp);
				// 把当前部门信息封装成JSON返回
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("curLevelInfoStr", curLevelInfoStr);
				resp.getWriter().print(jsonObj.toString());
			} else if (version.equals("2")) {
				// nextLevel==jds 表示从导航栏进来
				if (nextLevel.equals("jds")) {
					flag = searchRootLevelContent(req, resp);
					// 查询结果为空
					if (!flag) {
						req.setAttribute("searchflag", 0);
					}
					// 只有从导航栏进来的时候有一个跳转,其他都是qualify.info页面异步刷新
					RequestDispatcher dispatcher = req
							.getRequestDispatcher(url);
					dispatcher.forward(req, resp);
				}
				// nextLevel:company
				else {
					String nextLevelContentStr = searchNextLevelContent(req,
							resp);
					String curLevelInfoStr = searchCurLevelInfo(req, resp);
					// 把下级目录和当前部门信息封装成JSON返回
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("nextLevelContentStr", nextLevelContentStr);
					jsonObj.put("curLevelInfoStr", curLevelInfoStr);
					resp.getWriter().print(jsonObj.toString());
				}
			} else if (version.equals("3")) {
				// nextLevel==jdj 表示从导航栏进来
				if (nextLevel.equals("jdj")) {
					flag = searchRootLevelContent(req, resp);
					// 查询结果为空
					if (!flag) {
						req.setAttribute("searchflag", 0);
					}
					// 只有从导航栏进来的时候有一个跳转,其他都是qualify.info页面异步刷新
					RequestDispatcher dispatcher = req
							.getRequestDispatcher(url);
					dispatcher.forward(req, resp);
				}
				// nextLevel:jds或company
				else {
					String nextLevelContentStr = searchNextLevelContent(req,
							resp);
					String curLevelInfoStr = searchCurLevelInfo(req, resp);
					// 把下级目录和当前部门信息封装成JSON返回
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("nextLevelContentStr", nextLevelContentStr);
					jsonObj.put("curLevelInfoStr", curLevelInfoStr);
					resp.getWriter().print(jsonObj.toString());
				}
			} else if (version.equals("4")) {
				// nextLevel==zhj 表示从导航栏进来
				if (nextLevel.equals("zhj")) {
					flag = searchRootLevelContent(req, resp);
					// 查询结果为空
					if (!flag) {
						req.setAttribute("searchflag", 0);
					}
					// 只有从导航栏进来的时候有一个跳转,其他都是qualify.info页面异步刷新
					RequestDispatcher dispatcher = req
							.getRequestDispatcher(url);
					dispatcher.forward(req, resp);
				}
				// nextLevel:jdj或jds或company
				else {
					String nextLevelContentStr = searchNextLevelContent(req,
							resp);
					String curLevelInfoStr = searchCurLevelInfo(req, resp);
					// 把下级目录和当前部门信息封装成JSON返回
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("nextLevelContentStr", nextLevelContentStr);
					jsonObj.put("curLevelInfoStr", curLevelInfoStr);
					resp.getWriter().print(jsonObj.toString());
				}
			}
		} else if (operate.equals("update")) {
			flag = updateQualify(req, resp);
			if (flag) {
				resp.getWriter().print("1");
			} else {
				resp.getWriter().print("0");
			}
		} else if (operate.equals("delete")) {
			flag = deleteInfo(req, resp);
			if (flag) {
				resp.getWriter().print("1");
			} else {
				resp.getWriter().print("0");
			}
		} else if (operate.equals("add")) {
			flag = addInfo(req, resp);
			if (flag) {
				resp.getWriter().print("1");
			} else {
				resp.getWriter().print("0");
			}
		} else if (operate.equals("getId")) {
			long curId = searchIdByName(req, resp);
			if (curId != 0)
				resp.getWriter().print(curId);
		}
	}

	private boolean updateQualify(HttpServletRequest req,
			HttpServletResponse resp) {
		long infoId = Long.parseLong(req.getParameter("infoId"));
		String updateLevelName = req.getParameter("updateLevelName");
		String ownedLevelName = req.getParameter("ownedLevelName");
		String manager = req.getParameter("manager");
		String leader = req.getParameter("leader");
		flag = is.updateInfo(infoId, updateLevelName, ownedLevelName, manager,
				leader, curLevel);
		return flag;
	}

	private boolean addInfo(HttpServletRequest req, HttpServletResponse resp) {
		String addLevelName = req.getParameter("addLevelName");
		String ownedLevelName = req.getParameter("ownedLevelName");
		String manager = req.getParameter("manager");
		String leader = req.getParameter("leader");
		flag = is.addInfo(addLevelName, ownedLevelName, manager, leader,
				curLevel);
		return flag;
	}

	private boolean deleteInfo(HttpServletRequest req, HttpServletResponse resp) {
		curLevelName = req.getParameter("curLevelName");
		flag = is.deleteInfo(curLevel, curLevelName);
		return flag;
	}

	public boolean searchRootLevelContent(HttpServletRequest req,
			HttpServletResponse resp) {
		// ArrayList<String> allRootLevelContentList = new ArrayList<String>();
		// allRootLevelContentList = is.searchRootLevelContent(rootLevel);
		// String allRootLevelContentStr = "";
		// for (int i = 0; i < allRootLevelContentList.size(); i++) {
		// if (i == allRootLevelContentList.size() - 1) {
		// allRootLevelContentStr += allRootLevelContentList.get(i);
		// break;
		// }
		// allRootLevelContentStr += allRootLevelContentList.get(i) + ",";
		// }
		String ownedUnit = (String) req.getSession().getAttribute("ownedUnit");

		req.setAttribute("allRootLevelContentStr", ownedUnit);
		flag = true;
		// if (allRootLevelContentList.size() != 0) {
		// flag = true;
		// } else {
		// flag = false;
		// }
		url = "jsp/" + curFolder + "/qualification_management/qualifyinfo.jsp";
		return flag;
	}

	// 查询下级目录的部门名字
	public String searchNextLevelContent(HttpServletRequest req,
			HttpServletResponse resp) {
		ArrayList<String> curOtherNameList = new ArrayList<String>();
		curLevelName = req.getParameter("curLevelName");
		curOtherNameList = is.searchNextLevelContent(nextLevel, curLevelName);
		String curOtherNameStr = "";
		for (int i = 0; i < curOtherNameList.size(); i++) {
			if (i == curOtherNameList.size() - 1) {
				curOtherNameStr += curOtherNameList.get(i);
				break;
			}
			curOtherNameStr += curOtherNameList.get(i) + ",";
		}
		return curOtherNameStr;
	}

	// 查询是否有下级部门
	public int ifExistNextLevelContent(HttpServletRequest req,
			HttpServletResponse resp) {
		boolean existFlag = false;
		curLevelName = req.getParameter("curLevelName");
		existFlag = is.ifExistNextLevelContent(nextLevel, curLevelName);
		if (existFlag)
			return 1;
		else
			return 0;
	}

	private String searchCurLevelInfo(HttpServletRequest req,
			HttpServletResponse resp) {
		ArrayList<String> curLevelInfoList = new ArrayList<String>();
		curLevelName = req.getParameter("curLevelName");
		curLevelInfoList = is.searchCurLevelInfo(curLevel, curLevelName);
		// 将当前信息List转为String
		String curLevelInfoStr = "";
		for (int i = 0; i < curLevelInfoList.size(); i++) {
			if (i == curLevelInfoList.size() - 1) {
				curLevelInfoStr += curLevelInfoList.get(i);
				break;
			}
			curLevelInfoStr += curLevelInfoList.get(i) + ",";
		}
		return curLevelInfoStr;
	}

	private long searchIdByName(HttpServletRequest req, HttpServletResponse resp) {
		long curId = 0;
		String curLevel = req.getParameter("curLevel");
		String curLevelName = req.getParameter("curLevelName");
		curId = is.searchIdByName(curLevel, curLevelName);
		return curId;
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}