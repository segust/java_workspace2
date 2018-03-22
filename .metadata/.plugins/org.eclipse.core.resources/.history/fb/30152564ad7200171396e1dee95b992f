package sqoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.sqoop.Sqoop;
import org.apache.sqoop.tool.SqoopTool;
import org.apache.sqoop.util.OptionsFileUtil;

public class SqoopTestServiceImpl implements SqoopTestService  
{  
  //测试代码  
  public static void main(String[] args)  
  {  
    try {  
        new WordCountServiceImpl().importDataFromOracle();  
    } catch (Exception e){  
       e.printStackTrace();  
    }  
  }  
  
  @Override  
  public int importDataFromOracle() {  
    String[] args = new String[]{  
        "--connect", "jdbc:oracle:thin:@192.168.10.12:1521:ftmsdev",  
        "--username", "ftms",  
        "--password", "ftms",  
        "--table", "SYSTEM_USER",  
        "--columns", "LOGIN_NAME,PASSWORD",  
        "--split-by", "USER_ID",  
        "--target-dir", "ftms/tables/system_user"  
    };  
  
    String[] expandedArgs = null;  
    try {  
        expandedArgs = OptionsFileUtil.expandArguments(args);  
    } catch (Exception e){  
       System.err.println(ex.getMessage());  
       System.err.println("Try 'sqoop help' for usage.");  
    }  
  
    com.cloudera.sqoop.tool.SqoopTool tool = (com.cloudera.sqoop.tool.SqoopTool) SqoopTool.getTool("import");  
    //com.cloudera.sqoop.tool.SqoopTool tool = new ImportTool();  
  
    Configuration conf = new Configuration();  
    conf.set("fs.default.name", "hdfs://192.168.242.128:9000");//设置hadoop服务地址  
    Configuration pluginConf = SqoolTool.loadPlugins(conf);  
  
    Sqoop sqoop = new Sqoop(tool, pluginConf);  
    return Sqoop.runSqoop(sqoop, expandedArgs);  
  }  
  
} 