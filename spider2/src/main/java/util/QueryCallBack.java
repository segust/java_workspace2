package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 与Query.executeQueryTemplate 配合的回掉接口
 * @author Jarence
 *
 */
public interface QueryCallBack {
	public Object doExecute(ResultSet rs);
}
