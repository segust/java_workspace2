package util;

import java.util.Collections;
import java.util.List;

public class AlgorithmUtil {
	
	/**
	 * 比较a集合b集合是否相等
	 * @param a
	 * @param b
	 * @return
	 */
	public static <T extends Comparable<T>> boolean compare(List<T> a, List<T> b) {
		  if(a.size() != b.size())
		    return false;
		  Collections.sort(a);
		  Collections.sort(b);
		  for(int i=0;i<a.size();i++){
		    if(!a.get(i).equals(b.get(i)))
		      return false;
		  }
		  return true;
		}
	
	/**
	 * 判断a集合是否包含b集合
	 * @param a
	 * @param b
	 * @return
	 */
	public static <T extends Comparable<T>> boolean contain(List<T> a, List<T> b) {
		if(a.size() < b.size()) {
			return false;
		}
		Collections.sort(a);
		Collections.sort(b);
		if(!a.containsAll(b)) {
			return false;
		}
		return true;
	}
}
