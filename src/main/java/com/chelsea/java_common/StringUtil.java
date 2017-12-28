package com.chelsea.java_common;

/**
 * 字符串工具类
 * 
 * @author shevchenko
 *
 */
public class StringUtil {

	/**
	 * @Description:根据参数格式化字符串
	 * @param character
	 * @param params
	 * @return
	 * @return String
	 */
	public static String stringFormat(String character, String... params) {
		if (params == null || character == null) {
			return "";
		}
		for (int i = 0; i < params.length; i++) {
			character = character.replaceAll("\\{" + i + "\\}",
					params[i] == null ? " " : params[i]);
		}
		return character;
	}
	
}
