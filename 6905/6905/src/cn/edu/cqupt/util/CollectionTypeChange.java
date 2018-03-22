package cn.edu.cqupt.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class CollectionTypeChange {
	public static <T> Collection<?> ClearSameObject(List<T> allList) {
		if(allList.size() > 0) {
			List<T> list = new ArrayList<T>();
			Set<T> set=new HashSet<T>();
			set.addAll(allList);
			list.addAll(set);
			return list;
		}else {
			return null;
		}
	}
}
