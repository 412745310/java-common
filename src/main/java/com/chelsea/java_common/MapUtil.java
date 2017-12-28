package com.chelsea.java_common;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * map工具类
 * 
 */
public class MapUtil {
	
	private MapUtil(){
	}

	/**
	 * @Description:Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean -->
	 *                   Map
	 * @param obj
	 * @return
	 * @return Map<String,Object>
	 * @author:baojun
	 * @date:2017年2月22日 下午2:46:40
	 */
	public static Map<String, Object> transBean2Map(Object obj) {

		if (obj == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo
					.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();

				// 过滤class属性
				if (!key.equals("class")) {
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					Object value = getter.invoke(obj);
					if (value != null) {
						map.put(key, value);
					}
				}

			}
		} catch (Exception e) {
			System.out.println("transBean2Map Error " + e);
		}
		return map;
	}

	/**
	 * map计算 key,value相同的情况下，count计数累加
	 * 
	 * @param map
	 *            <key, Map<value, count>>
	 * @param key
	 * @param value
	 */
	@SuppressWarnings("unchecked")
	public static void mapCompute(Map<String, Object> map, String key,
			String value) {
		Object valueObj = map.get(key);
		if (valueObj == null) {
			Map<String, Object> valueMap = new HashMap<String, Object>();
			valueMap.put(value, 1);
			map.put(key, valueMap);
		} else {
			Map<String, Object> valueMap = (Map<String, Object>) valueObj;
			Object countObj = valueMap.get(value);
			if (countObj == null) {
				valueMap.put(value, 1);
			} else {
				Integer newCount = Integer.parseInt(countObj.toString()) + 1;
				valueMap.put(value, newCount);
			}
		}
	}
}
