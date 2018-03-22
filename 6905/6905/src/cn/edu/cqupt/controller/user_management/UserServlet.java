package cn.edu.cqupt.controller.user_management;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.edu.cqupt.beans.Log;
import cn.edu.cqupt.beans.Role;
import cn.edu.cqupt.beans.User;
import cn.edu.cqupt.log.UserLogService;
import cn.edu.cqupt.service.user_management.RoleService;
import cn.edu.cqupt.service.user_management.UserService;
import cn.edu.cqupt.util.CurrentUser;
import cn.edu.cqupt.util.GetError;
import cn.edu.cqupt.util.RSAUtil;

public class UserServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9145294729866150668L;

	/**
	 * Constructor of the object.
	 */
	public UserServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */

	// curFolder表示根据版本的不同，进入网页所在的文件夹不同
	private String curFolder = "";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 全局变量获取当前版本
		ServletContext sctx = this.getServletContext();
		String version = (String) sctx.getInitParameter("version");
		// version==1表示是企业版
		if (version.equals("1")) {
			curFolder = "qy";
			forwardByOperation(request, response);
		}
		// version==2表示是军代室版
		if (version.equals("2")) {
			curFolder = "jds";
			forwardByOperation(request, response);
		}
		// version==3表示是军代局版
		if (version.equals("3")) {
			curFolder = "jdj";
			forwardByOperation(request, response);
		}
		// version==4表示是指挥局版
		if (version.equals("4")) {
			curFolder = "zhj";
			forwardByOperation(request, response);
		}
	}

	// 获取用户的操作函数，决定进入那一个页面
	public void forwardByOperation(HttpServletRequest request,
			HttpServletResponse response) {
		String operation = request.getParameter("operation");
		String subOperation = request.getParameter("subOperation");

		UserService us = new UserService();
		RoleService rs = new RoleService();

		// 当前用户选择的是个人用户，进入peruserinfo.jsp查看个人用户信息
		if (operation.equals("peruserinfo")) {
			PerUserInfo(request, response);
		}

		// 当前用户选择的是修改密码
		else if (operation.equals("updatePwd")) {
			UpdatePwd(request, response, us);
		}
		
		// 当前是验证旧密码
		else if(operation.equals("validateOldPwd")){
			validateOldPwd(request,response);
		}

		// 当前用户选择的是管理员用户，进行权限判定
		else if (operation.equals("manager")) {

			// subOperation是三级模块的判定
			//判断当前用户是不是用户管理员  UserManager
			if (CurrentUser.isUserManage(request)) {
				// 如果用户点击了添加用户,则执行添加用户
				if (subOperation.equals("addUser")) {
					AddUser(request, response, us);
				}
				// 如果用户按条件点击了查询用户，则执行查询匹配条件的用户
				else if (subOperation.equals("searchLikeUser")) {
					searchLikeUser(request, response, us);
				}
				// 如果用户点击了角色管理，则跳转到角色管理列表roleinfo.jsp页面
				else if (subOperation.equals("roleInfo")) {
					RoleInfoByPage(request, response, rs);
				}
				// 如果用户按条件点击了查询角色，则执行查询匹配条件的角色
				else if (subOperation.equals("searchLikeRole")) {
					searchLikeRole(request, response, rs);
				}
				// 如果用户点击了添加角色页面的添加按钮，则获取需添加的角色信息
				else if (subOperation.equals("addRole")) {
					AddRole(request, response, rs);
				}
				// 如果用户在用户管理信息页面点击了删除按钮，再点击了确认，则删除选择用户
				else if (subOperation.equals("deleteUser")) {
					DeleteUser(request, response, us);
				}
				// 如果用户在角色管理信息页面点击了删除按钮，再点击了确认，则删除选择角色
				else if (subOperation.equals("deleteRole")) {
					DeleteRole(request, response, rs);
				}
				// 如果用户点击了修改用户页面的修改按钮，则获取需修改的用户信息
				else if (subOperation.equals("updateUser")) {
					UpdateUser(request, response, us);
				}
				// 如果用户点击了修改角色页面的修改按钮，则获取需修改的角色信息
				else if (subOperation.equals("updateRole")) {
					UpdateRole(request, response, rs);
				}
				// 默认管理员用户的首页面是显示用户管理列表userinfo.jsp页面，此时subOperation=searchAllUser
				else if (subOperation.equals("searchAllUser")) {
					UserInfoByPage(request, response, us);
				}
			} else {
				//不是有权限用户
				GetError.limitVisit(request, response);
			}
		}
	}

	// 获取用户的个人信息
	void PerUserInfo(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String identifyNum = (String) session.getAttribute("identifyNum");
		User user = new UserService().getUserByIdentifyNum(identifyNum);
		request.setAttribute("user", user);
		try {
			request.getRequestDispatcher(
					"jsp/" + curFolder + "/user_management/peruserinfo.jsp")
					.forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 修改密码
	void UpdatePwd(HttpServletRequest request, HttpServletResponse response,
			UserService us) {
		// 获取当前用户账号和修改的密码
		String identifyNum = request.getParameter("identifyNum");
		String password = request.getParameter("password");
		password=RSAUtil.string2RSA(password);
		boolean updateFlag = us.updatePwd(password, identifyNum);
		if (updateFlag) {
			request.setAttribute("updateFlag", 1);
			// 记录用户修改密码到日志
			Log log = new Log();
			log.setOperateTime(new Date());
			log.setUserName((String) request.getSession().getAttribute(
					"username"));
			log.setOperateType("用户" + identifyNum + "修改密码");
			UserLogService.SaveOperateLog(log);
		} else {
			request.setAttribute("updateFlag", 0);
		}
		// 跳转到UserServlet
		try {
			request.getRequestDispatcher("UserServlet?operation=peruserinfo")
					.forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//验证旧密码输入是否正确
	void validateOldPwd(HttpServletRequest request,HttpServletResponse response){
		String inputOldPwd=request.getParameter("inputOldPwd");
		String oldPwd=(String)request.getSession().getAttribute("password");
		inputOldPwd=RSAUtil.string2RSA(inputOldPwd);
		String conflag=inputOldPwd.equals(oldPwd)?"1":"0";
		try {
			response.getWriter().write(conflag);
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	// 查询匹配条件的当前页的用户
	void searchLikeUser(HttpServletRequest request,
			HttpServletResponse response, UserService us) {
		// 获取前台传来的模糊查询操作(subOperation)的参数(主要是为了forward回去，用户再点其他页时保证是模糊查询的结果)
		String subOperation = request.getParameter("subOperation");
		// 获取前台传来的请求页码和每页显示条数
		int curPageNum = Integer.parseInt(request.getParameter("curPageNum"));
		int pageSize = Integer.parseInt(request.getParameter("pageSize"));

		// 获取用户选择查询的类型和条件
		// 由于页面跳转后得不到用户的模糊查询条件，所以前台url传参控制
		String searchType = request.getParameter("searchType");
		String searchStr = request.getParameter("searchStr");

		// 查询满足条件的用户的总个数
		long likeUserSum = us.getLikeUserSum(searchType, searchStr);
		// 因为模糊查询和全部查询共用同一个页面，所以共用userSum这个键
		request.setAttribute("userSum", likeUserSum);

		ArrayList<User> userLikeList = us.searchUserLikeByPage(searchType,
				searchStr, curPageNum, pageSize);
		if (userLikeList != null) {
			// 查询成功
			request.setAttribute("curUserList", userLikeList);
		} else {
			// 查询失败
		}

		// 查询成功或失败均跳到模糊查询结果用户信息页面
		// curPageNum当前页码，pageSize每页多少条显示，subOperation模糊查询还是全部查询
		try {
			String url = "jsp/" + curFolder
					+ "/user_management/userinfo.jsp?curPageNum=" + curPageNum
					+ "&pageSize" + pageSize + "&subOperation=" + subOperation
					+ "&searchType=" + searchType + "&searchStr=" + searchStr;
			request.getRequestDispatcher(url).forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 获取当前页的用户信息
	void UserInfoByPage(HttpServletRequest request,
			HttpServletResponse response, UserService us) {
		// 获取全部查询数据的个数
		long userSum = us.getUserSum();
		request.setAttribute("userSum", userSum);

		// 记录前台传来的查询类型是全部查询还是模糊查询,再传回前台
		String subOperation = request.getParameter("subOperation");

		// 获得前台的传来的当前页码和按多少条分页的参数，获取当前页的数据
		int curPageNum = Integer.parseInt(request.getParameter("curPageNum"));
		int pageSize = Integer.parseInt(request.getParameter("pageSize"));
		ArrayList<User> curUserList = us.searchUserByPage(curPageNum, pageSize);
		if (curUserList != null) {
			request.setAttribute("curUserList", curUserList);
		} else {
			// 查询结果为空
		}
		try {
			String url = "jsp/" + curFolder
					+ "/user_management/userinfo.jsp?curPageNum=" + curPageNum
					+ "&pageSize=" + pageSize + "&subOperation=" + subOperation;
			request.getRequestDispatcher(url).forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 添加一个用户
	void AddUser(HttpServletRequest request, HttpServletResponse response,
			UserService us) {
		// 获取用户输入的信息,并封装成User对象
		String identifyNum = request.getParameter("identifyNum");
		String password = request.getParameter("password");
		password=RSAUtil.string2RSA(password);
		String username = request.getParameter("username");
		String role = request.getParameter("role");
		String duty = request.getParameter("duty");
		String ownedUnit = request.getParameter("ownedUnit");
		String authorityUnit = request.getParameter("authorityUnit");
		User user = new User();
		user.setIdentifyNum(identifyNum);
		user.setPassword(password);
		user.setName(username);
		user.setRole(role);
		user.setDuty(duty);
		user.setOwnedUnit(ownedUnit);
		user.setAuthorityUnit(authorityUnit);

		// 是否存在重复记录
		boolean repeatFlag = us.repeatUser(identifyNum, ownedUnit);

		// 获得前台的按多少条分页的参数
		int pageSize = Integer.parseInt(request.getParameter("pageSize"));
		long userSum = us.getUserSum();
		long curPageNum = userSum % pageSize == 0 ? userSum / pageSize
				: (userSum / pageSize + 1);
		request.setAttribute("userSum", userSum);
		// 获取当前页用户
		ArrayList<User> curUserList = us.searchUserByPage((int) curPageNum,
				pageSize);
		request.setAttribute("curUserList", curUserList);

		boolean addFlag = false;
		if (!repeatFlag) {
			addFlag = us.addUser(user);
			if (addFlag) {
				// 添加成功的标识
				request.setAttribute("addFlag", 1);
				// 重新计算分页
				userSum = userSum + 1;
				curPageNum = userSum % pageSize == 0 ? userSum / pageSize
						: (userSum / pageSize + 1);
				request.setAttribute("userSum", userSum);
				// 重新获取当前页用户
				curUserList = us.searchUserByPage((int) curPageNum, pageSize);
				request.setAttribute("curUserList", curUserList);
				// 记录用户添加用户的操作到日志
				Log log = new Log();
				log.setUserName((String) request.getSession().getAttribute(
						"username"));
				log.setOperateTime(new Date()); // 记录当前用户操作的时间
				log.setOperateType("添加用户,用户账号：" + identifyNum);
				UserLogService.SaveOperateLog(log); // 记录当前用户操作到数据库
			} else {
				request.setAttribute("addFlag", 0);
			}
		} else {
			request.setAttribute("repeatFlag", 1);
		}

		// 成功或失败都返回返回到用户列表信息页面，页面给出成功或失败信息
		try {
			request
					.getRequestDispatcher(
							"jsp/"
									+ curFolder
									+ "/user_management/userinfo.jsp?operation=manager&subOperation=searchAllUser&curPageNum="
									+ curPageNum + "&pageSize=" + pageSize)
					.forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 获取全部角色信息（角色因为少，不分页）
	void RoleInfoByPage(HttpServletRequest request,
			HttpServletResponse response, RoleService rs) {
		ArrayList<Role> allRoleList = rs.searchAllRole();
		if (allRoleList != null) {
			request.setAttribute("allRoleList", allRoleList);
		} else {
			// 获取角色失败
		}
		try {
			request.getRequestDispatcher(
					"./jsp/" + curFolder + "/user_management/roleinfo.jsp")
					.forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 添加一个角色
	void AddRole(HttpServletRequest request, HttpServletResponse response,
			RoleService rs) {
		// 获取页面添加角色的信息
		Role role = new Role();
		role.setRole(request.getParameter("roleName"));
		// 是否选择了多选框用0或者1赋值
		if (request.getParameter("contractManage") != null)
			role.setContractManage(1);
		else
			role.setContractManage(0);
		if (request.getParameter("queryBusiness") != null)
			role.setQueryBusiness(1);
		else
			role.setQueryBusiness(0);
		if (request.getParameter("borrowUpdate") != null)
			role.setBorrowUpdate(1);
		else
			role.setBorrowUpdate(0);
		if (request.getParameter("storeMantain") != null)
			role.setStoreMantain(1);
		else
			role.setStoreMantain(0);
		if (request.getParameter("warehouseManage") != null)
			role.setWarehouseManage(1);
		else
			role.setWarehouseManage(0);
		if (request.getParameter("statistics") != null)
			role.setStatistics(1);
		else
			role.setStatistics(0);
		if (request.getParameter("fareManage") != null)
			role.setFareManage(1);
		else
			role.setFareManage(0);
		if (request.getParameter("qualificationManage") != null)
			role.setQualificationManage(1);
		else
			role.setQualificationManage(0);
		if (request.getParameter("systemManage") != null)
			role.setSystemManage(1);
		else
			role.setSystemManage(0);
		if (request.getParameter("userManage") != null)
			role.setUserManage(1);
		else
			role.setUserManage(0);
		// 判读是否重复提交
		boolean repeatFlag = rs.repeatRole(role.getRole());
		boolean addFlag = false;
		if (!repeatFlag) {
			addFlag = rs.addRole(role);
			if (addFlag) {
				// 添加角色成功的标识
				request.setAttribute("addFlag", 1);
				// 记录用户添加角色操作到数据库
				Log log = new Log();
				log.setUserName((String) request.getSession().getAttribute(
						"username"));
				log.setOperateTime(new Date()); // 记录当前用户操作的时间
				log.setOperateType("添加角色,角色名：" + role.getRole());
				UserLogService.SaveOperateLog(log); // 记录当前用户操作到数据库
			} else {
				// 添加失败
				request.setAttribute("addFlag", 0);
			}
		} else {
			request.setAttribute("repeatFlag", 1);
		}
		// 成功或失败都返回到角色列表信息页面，页面给出成功或失败信息
		try {
			ArrayList<Role> allRoleList = rs.searchAllRole();
			request.setAttribute("allRoleList", allRoleList);
			request.getRequestDispatcher(
					"jsp/" + curFolder + "/user_management/roleinfo.jsp")
					.forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 删除一个用户
	void DeleteUser(HttpServletRequest request, HttpServletResponse response,
			UserService us) {
		String identifyNum = request.getParameter("identifyNum");
		boolean flag = us.deleteUser(identifyNum);
		// 删除成功或失败，均给出友好提示，跳转到用户信息列表userinfo.jsp页面
		long userSum = us.getUserSum();
		request.setAttribute("userSum", userSum);

		// 获得前台的传来的当前页码和按多少条分页的参数,重新获取当前页的数据
		int curPageNum = Integer.parseInt(request.getParameter("curPageNum"));
		int pageSize = Integer.parseInt(request.getParameter("pageSize"));
		// 删除一条记录时，如果是删的最后一条记录，则当前上一页为最后一页
		int tempPageNum = (int) (userSum % pageSize == 0 ? userSum / pageSize
				: (userSum / pageSize + 1));
		if (tempPageNum == curPageNum - 1)
			curPageNum = tempPageNum;
		if (flag) {
			ArrayList<User> curUserList = us.searchUserByPage(curPageNum,
					pageSize);
			request.setAttribute("curUserList", curUserList);
			// 删除成功的标识
			request.setAttribute("deleteFlag", 1);
			// 记录用户删除用户操作到数据库
			Log log = new Log();
			log.setUserName((String) request.getSession().getAttribute(
					"username"));
			log.setOperateTime(new Date()); // 记录当前用户操作的时间
			log.setOperateType("删除用户，用户账号：" + identifyNum);
			UserLogService.SaveOperateLog(log); // 记录当前用户操作到数据库
		} else
			request.setAttribute("deleteFlag", 0);

		String url = "";
		String searchStr = "";
		String searchType = "";

		// 获取前台是在全部查询下删除还是模糊查询下删除,以便跳回不同页面
		if (request.getParameter("searchStr") != null) {
			searchStr = request.getParameter("searchStr");
			searchType = request.getParameter("searchType");
			url = "UserServlet?operation=manager&subOperation=searchLikeUser&curPageNum="
					+ curPageNum
					+ "&pageSize="
					+ pageSize
					+ "&searchType="
					+ searchType + "&searchStr=" + searchStr;
		} else {
			url = "UserServlet?operation=manager&subOperation=searchAllUser&curPageNum="
					+ curPageNum + "&pageSize=" + pageSize;
		}

		try {
			request.getRequestDispatcher(url).forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 删除一个角色
	void DeleteRole(HttpServletRequest request, HttpServletResponse response,
			RoleService rs) {
		long roleId = Long.parseLong(request.getParameter("roleId"));
		boolean flag = rs.deleteRole(roleId);
		// 删除成功或失败，均给出友好提示，跳转到角色信息列表roleinfo.jsp页面
		if (flag) {
			// 删除成功，重新获取角色列表信息
			ArrayList<Role> allRoleList = rs.searchAllRole();
			request.setAttribute("allRoleList", allRoleList);
			// 删除成功的标识
			request.setAttribute("deleteFlag", 1);
			// 记录用户删除角色操作到数据库
			Log log = new Log();
			log.setUserName((String) request.getSession().getAttribute(
					"username"));
			log.setOperateTime(new Date()); // 记录当前用户操作的时间
			log.setOperateType("删除角色，角色编号：" + roleId);
			UserLogService.SaveOperateLog(log); // 记录当前用户操作到数据库
		} else
			request.setAttribute("deleteFlag", 0);
		try {
			request.getRequestDispatcher(
					"jsp/" + curFolder + "/user_management/roleinfo.jsp")
					.forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 查询匹配条件的角色
	void searchLikeRole(HttpServletRequest request,
			HttpServletResponse response, RoleService rs) {
		// 获取用户选择查询的类型和条件
		String searchType = request.getParameter("searchType");
		String searchStr = request.getParameter("searchStr");
		ArrayList<Role> roleLikeList = rs.searchRoleLike(searchType, searchStr);
		if (roleLikeList != null) {
			// 查询成功
			request.setAttribute("allRoleList", roleLikeList);
		} else {
			// 查询失败
		}
		// 查询成功或失败均跳到用户信息页面
		try {
			request.getRequestDispatcher(
					"jsp/" + curFolder + "/user_management/roleinfo.jsp")
					.forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 修改用户
	void UpdateUser(HttpServletRequest request, HttpServletResponse response,
			UserService us) {
		// 获取前台传来的当前页和多少条分页参数
		int curPageNum = Integer.parseInt(request.getParameter("curPageNum"));
		int pageSize = Integer.parseInt(request.getParameter("pageSize"));
		// 获取前台传来的其他必要参数
		String searchType = request.getParameter("searchType");
		String searchStr = request.getParameter("searchStr");
		String subOperation = "";
		if (searchStr != "" || searchType != "")
			subOperation = "searchLikeUser";
		else
			subOperation = "searchAllUser";
		// 获取前台的修改用户信息,并封装成User对象
		String identifyNum = request.getParameter("identifyNum");
		String username = request.getParameter("username");
		String role = request.getParameter("role");
		String duty = request.getParameter("duty");
		String ownedUnit = request.getParameter("ownedUnit");
		String authorityUnit = request.getParameter("authorityUnit");
		User user = new User();
		user.setIdentifyNum(identifyNum);
		user.setName(username);
		user.setRole(role);
		user.setDuty(duty);
		user.setOwnedUnit(ownedUnit);
		user.setAuthorityUnit(authorityUnit);
		boolean updateFlag = us.updateuser(user);
		// 修改用户成功或失败，返回修改标识
		if (updateFlag) {
			request.setAttribute("updateFlag", 1);
			// 记录用户更新角色信息到日志
			Log log = new Log();
			log.setUserName((String) request.getSession().getAttribute(
					"username"));
			log.setOperateTime(new Date()); // 记录当前用户操作的时间
			log.setOperateType("更新用户，角色账号：" + identifyNum);
			UserLogService.SaveOperateLog(log); // 记录当前用户操作到数据库
		} else {
			request.setAttribute("updateFlag", 0);
		}
		// 跳转到当前修改用户所在页面
		try {
			request.getRequestDispatcher(
					"UserServlet?operate=manager&curPageNum=" + curPageNum
							+ "&pageSize=" + pageSize + "&subOperation="
							+ subOperation + "&searchStr=" + searchStr
							+ "&searchType=" + searchType).forward(request,
					response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 修改角色
	void UpdateRole(HttpServletRequest request, HttpServletResponse response,
			RoleService rs) {
		// 获取页面修改角色的信息
		Role role = new Role();
		long roleId = Long.parseLong(request.getParameter("roleId"));
		role.setRoleId(roleId);
		role.setRole(request.getParameter("roleName"));
		// 是否选择了多选框用0或者1赋值
		if (request.getParameter("contractManage") != null)
			role.setContractManage(1);
		else
			role.setContractManage(0);
		if (request.getParameter("queryBusiness") != null)
			role.setQueryBusiness(1);
		else
			role.setQueryBusiness(0);
		if (request.getParameter("borrowUpdate") != null)
			role.setBorrowUpdate(1);
		else
			role.setBorrowUpdate(0);
		if (request.getParameter("storeMantain") != null)
			role.setStoreMantain(1);
		else
			role.setStoreMantain(0);
		if (request.getParameter("warehouseManage") != null)
			role.setWarehouseManage(1);
		else
			role.setWarehouseManage(0);
		if (request.getParameter("statistics") != null)
			role.setStatistics(1);
		else
			role.setStatistics(0);
		if (request.getParameter("fareManage") != null)
			role.setFareManage(1);
		else
			role.setFareManage(0);
		if (request.getParameter("qualificationManage") != null)
			role.setQualificationManage(1);
		else
			role.setQualificationManage(0);
		if (request.getParameter("systemManage") != null)
			role.setSystemManage(1);
		else
			role.setSystemManage(0);
		if (request.getParameter("userManage") != null)
			role.setUserManage(1);
		else
			role.setUserManage(0);
		boolean flag = rs.updateRole(role);
		if (flag) {
			// 更新成功，给出友好提示,跳转到roleinfo.jsp页面
			ArrayList<Role> allRoleList = rs.searchAllRole();
			request.setAttribute("allRoleList", allRoleList);
			// 更新角色成功的标识
			request.setAttribute("updateFlag", 1);
			// 记录用户更新角色操作到数据库
			Log log = new Log();
			log.setUserName((String) request.getSession().getAttribute(
					"username"));
			log.setOperateTime(new Date()); // 记录当前用户操作的时间
			log.setOperateType("更新角色，角色编号：" + roleId);
			UserLogService.SaveOperateLog(log); // 记录当前用户操作到数据库
		} else {
			// 更新失败
			request.setAttribute("updateFlag", 0);
		}
		// 成功或失败都返回返回到角色列表信息页面，页面给出成功或失败信息
		try {
			request.getRequestDispatcher(
					"jsp/" + curFolder + "/user_management/roleinfo.jsp")
					.forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void init() throws ServletException {
		// Put your code here
	}

}
