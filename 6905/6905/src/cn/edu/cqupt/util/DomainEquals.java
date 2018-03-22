package cn.edu.cqupt.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DomainEquals {
	public DomainEquals() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 比较两个BEAN的值是否相等
	 * @param source
	 * @param target
	 * @return
	 */
	public static boolean domainEquals(Object source, Object target) {
		if (source == null || target == null) {
			return false;
		}
		boolean rv = true;
		rv = classOfSrc(source, target, rv);
		return rv;
	}

	/**
	 * 利用反射匹配源对象和目标对象，看它们是否相等
	 * @param source
	 * @param target
	 * @param rv
	 * @return
	 */
	private static boolean classOfSrc(Object source, Object target, boolean rv) {
		Class<?> srcClass = source.getClass();
		Field[] fields = srcClass.getDeclaredFields();
		for (Field field : fields) {
			String nameKey = field.getName();
			String srcValue = getClassValue(source, nameKey) == null ? ""
					: getClassValue(source, nameKey).toString();
			String tarValue = getClassValue(target, nameKey) == null ? ""
					: getClassValue(target, nameKey).toString();
			if (!srcValue.equals(tarValue)) {
				rv = false;
				break;
			}
		}
		return rv;
	}

	/**
	 * 根据字段名称取值
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object getClassValue(Object obj, String fieldName) {
		if (obj == null) {
			return null;
		}
		try {
			Class beanClass = obj.getClass();
			Method[] ms = beanClass.getMethods();
			for (int i = 0; i < ms.length; i++) {
				// 非get方法不取
				if (!ms[i].getName().startsWith("get")) {
					continue;
				}
				Object objValue = null;
				try {
					objValue = ms[i].invoke(obj, new Object[] {});
				} catch (Exception e) {
					continue;
				}
				if (objValue == null) {
					continue;
				}
				if (ms[i].getName().toUpperCase().equals(
						fieldName.toUpperCase())
						|| ms[i].getName().substring(3).toUpperCase().equals(
								fieldName.toUpperCase())) {
					return objValue;
				} else if (fieldName.toUpperCase().equals("SID")
						&& (ms[i].getName().toUpperCase().equals("ID") || ms[i]
								.getName().substring(3).toUpperCase().equals(
										"ID"))) {
					return objValue;
				}
			}
		} catch (Exception e) {

		}
		return null;
	}

}
