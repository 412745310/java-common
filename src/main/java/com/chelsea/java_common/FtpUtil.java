package com.chelsea.java_common;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 * ftp工具类
 * 
 * @author baojun
 *
 */
public class FtpUtil {

	private static FTPClient ftp = new FTPClient();

	private FtpUtil() {
	}

	/**
	 * 创建ftp连接
	 * 
	 * @param addr
	 * @param port
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static int create(String ip, int port, String username,
			String password) throws Exception {
		if (!ftp.isConnected()) {
			ftp.connect(ip, port);
			ftp.login(username, password);
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftp.enterLocalPassiveMode();
		}
		int reply = ftp.getReplyCode();
		return reply;
	}

	/**
	 * 上传文件到ftp
	 * 
	 * @param addr
	 * @param port
	 * @param username
	 * @param password
	 * @param path
	 * @param filename
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public static boolean upload(String ip, int port, String username,
			String password, String path, String filename, InputStream input)
			throws Exception {
		boolean result = false;
		try {
			int reply = create(ip, port, username, password);
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return result;
			}
			ftp.changeWorkingDirectory(path);
			ftp.storeFile(filename, input);

			input.close();
			ftp.logout();
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return result;
	}

}
