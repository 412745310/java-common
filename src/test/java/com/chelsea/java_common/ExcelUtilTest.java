package com.chelsea.java_common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;

public class ExcelUtilTest {

	@Test
	public void test() {
		File file = new File("C:/Users/Administrator/Desktop/log_cpu_index.log");
		String result = file2String(file);
		JSONObject json = JSONObject.fromObject(result);
		JSONArray jsonArray = json.getJSONObject("hits").getJSONArray("hits");
		int size = jsonArray.size();
		System.out.println(size);
		List<JSONObject> list = new ArrayList<JSONObject>();
		for (int i = 0; i < size; i++) {
			JSONObject obj = jsonArray.getJSONObject(i)
					.getJSONObject("_source");
			list.add(obj);
		}
		String tmpNamePath = "C:/Users/Administrator/Desktop/cpu-template.xlsx";
		String fileName = "C:/Users/Administrator/Desktop/cpu.xlsx";
		Map<String, List<?>> listMap = new HashMap<String, List<?>>();
		listMap.put("listResult", list);
		ExcelUtil.generateExcelFile(tmpNamePath, listMap, fileName);
	}

	/**
	 * 读取txt文件的内容
	 * 
	 * @param file
	 *            想要读取的文件对象
	 * @return 返回文件内容
	 */
	public static String file2String(File file) {
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String s = null;
			while ((s = br.readLine()) != null) {
				result.append(s);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

}
