package cqupt.zyk.db;

import java.sql.ResultSet;

public interface QueryCallBack {

	public Object doExecute(ResultSet rs);
}