package cn.edu.cqupt.service.sys_management;

import cn.edu.cqupt.beans.Parameter_Configuration;
import cn.edu.cqupt.dao.ParameterDAO;

public class Parameter_configurationService {
    private static ParameterDAO pDao = null;
    static {
    	pDao = new ParameterDAO();
	}
    
    /**
	 * 添加修改参数配置记录
	 * @param qy_parameter_configuration
	 * @return
	 */
    public boolean addParameter_Configuration(Parameter_Configuration parameter_configuration,String version){
		boolean flag=pDao.insertParameter(parameter_configuration,version);
		return flag;
	}
    
    /**
     * 获取参数配置信息
     * */
    public Parameter_Configuration SelectParameter(String version){
    	Parameter_Configuration T=new Parameter_Configuration();
    	T=pDao.selectParameter(version);
    	return T;
    }
}
