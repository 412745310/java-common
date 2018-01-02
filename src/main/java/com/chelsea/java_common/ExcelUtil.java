package com.chelsea.java_common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * excel工具类
 * 
 */
public class ExcelUtil {

	private static Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);

	/***
	 * 
	 * @param tmpName
	 *            excel模板路径
	 * @param listMap
	 *            数据
	 * @param fileName
	 *            excel生成路径
	 * @return excel文件
	 */
	public static File generateExcelFile(String tmpName,
			Map<String, List<?>> listMap, String fileName) {
		File f = new File(fileName);
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(new File(tmpName));
			XLSTransformer transformer = new XLSTransformer();
			// 根据excel模板和action的属性生成excel文件
			Workbook workbook = transformer.transformXLS(in, listMap);
			out = new FileOutputStream(f);
			workbook.write(out);
			out.flush();
		} catch (Exception e) {
			LOGGER.error("生成Excel文件时候出错", e);
			throw new RuntimeException("生成Excel文件时候出错！", e);
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return f;
	}

}
