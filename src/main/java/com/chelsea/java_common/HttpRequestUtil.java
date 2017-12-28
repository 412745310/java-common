package com.chelsea.java_common;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * http请求工具类
 * @author baojun
 *
 */
public class HttpRequestUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestUtil.class);
	
	/**
	 * map对象转换成string
	 * @param map
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings("rawtypes")
	private static String mapToString(Map<String, Object> map) throws UnsupportedEncodingException {
		StringBuffer sb=new StringBuffer();
          //构建请求参数
          if(map!=null&&map.size()>0){
              Iterator it=map.entrySet().iterator(); //定义迭代器
              while(it.hasNext()){
                 Map.Entry er= (Entry) it.next();
                 sb.append(er.getKey());
                 sb.append("=");
                 sb.append(URLEncoder.encode(String.valueOf(er.getValue()), "utf-8"));
                 sb.append("&");
             }
          }
		return sb.toString();
	}

    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param paramMap
     *            请求参数，请求参数应该是 name1:value1,name2:value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, Map<String, Object> paramMap) {
        String result = "";
        String line;
        StringBuffer sb=new StringBuffer();
        BufferedReader in = null;
        try {
        	String param = mapToString(paramMap);
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性 设置请求格式
            conn.setRequestProperty("contentType", "UTF-8"); 
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            //设置超时时间
            conn.setConnectTimeout(60);
            conn.setReadTimeout(60);
            // 建立实际的连接
            conn.connect();
            // 定义 BufferedReader输入流来读取URL的响应,设置接收格式
            in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(),"UTF-8"));
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            result=sb.toString();
        } catch (Exception e) {
        	LOGGER.error("发送 GET请求出现异常!",e);
        	throw new RuntimeException("发送 GET请求出现异常!", e);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
            	LOGGER.error("发送 GET请求出现异常!",e2);
            	throw new RuntimeException("发送 GET请求出现异常!", e2);
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1:value1,name2:value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String actionUrl, Map<String, Object> paramMap) {
    	OutputStream out = null;
    	DataInputStream in = null;
    	String result = "";
    	try {
    		String params = mapToString(paramMap);
    		actionUrl = actionUrl + "?" + params;
			URL url = new URL(actionUrl);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");  
			conn.setRequestProperty("Content-Type","text/html");  
			conn.setRequestProperty("Cache-Control","no-cache");  
			conn.setRequestProperty("Charsert", "UTF-8");   
			conn.connect();  
			conn.setConnectTimeout(10000);  
			out =conn.getOutputStream();  
	   		out.flush();
	   		BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(),"UTF-8"));
	   		String line = "";
	   		StringBuffer sb = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString();
	   		conn.disconnect();
		} catch (Exception e) {
			LOGGER.error("http post请求失败", e);
			throw new RuntimeException("http post请求失败", e);
		}finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
            	LOGGER.error("http post请求失败", ex);
    			throw new RuntimeException("http post请求失败", ex);
            }
        }
    	return result;
    }
    
    /**
     * 发送post body请求
     * 
     * @param actionUrl
     * @param paramMap
     * @return
     */
    public static String sendPostBody(String actionUrl, Map<String, Object> paramMap){
    	OutputStreamWriter out = null;
    	DataInputStream in = null;
    	String result = "";
    	try {
    		JSONObject paramsJson = JSONObject.fromObject(paramMap);
			URL url = new URL(actionUrl);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true); 
			conn.setUseCaches(false);
			conn.setInstanceFollowRedirects(true); 
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept","application/json");
			conn.setRequestProperty("Content-Type","application/json");
			conn.setRequestProperty("Cache-Control","no-cache");  
			conn.setRequestProperty("Charsert", "UTF-8");   
			conn.connect();  
			conn.setConnectTimeout(10000);  
			out = new OutputStreamWriter(conn.getOutputStream(),"UTF-8");
			out.append(paramsJson.toString());  
            out.flush();  
            out.close();
	   		BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(),"UTF-8"));
	   		String line = "";
	   		StringBuffer sb = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString();
	   		conn.disconnect();
		} catch (Exception e) {
			LOGGER.error("http post body请求失败", e);
			throw new RuntimeException("http post body请求失败", e);
		}finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
            	LOGGER.error("http post body请求失败", ex);
    			throw new RuntimeException("http post body请求失败", ex);
            }
        }
    	return result;
    }
    
    /**
     * 发送https post body请求
     * @param actionUrl
     * @param paramMap
     * @return
     */
    public static String sendHttpsPostBody(String actionUrl, Map<String, Object> paramMap){
    	OutputStreamWriter out = null;
    	DataInputStream in = null;
    	String result = "";
    	try {
    		//创建SSLContext  
    	    SSLContext sslContext=SSLContext.getInstance("SSL");  
    	    TrustManager[] tm={new MyX509TrustManager()};  
    	    //初始化  
    	    sslContext.init(null, tm, new java.security.SecureRandom()); 
    	    //获取SSLSocketFactory对象  
    	    SSLSocketFactory ssf=sslContext.getSocketFactory();
    		JSONObject paramsJson = JSONObject.fromObject(paramMap);
			URL url = new URL(actionUrl);
			HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true); 
			conn.setUseCaches(false);
			conn.setInstanceFollowRedirects(true); 
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept","application/json");
			conn.setRequestProperty("Content-Type","application/json");
			conn.setRequestProperty("Cache-Control","no-cache");  
			conn.setRequestProperty("Charsert", "UTF-8");
			//设置当前实例使用的SSLSoctetFactory  
		    conn.setSSLSocketFactory(ssf);  
		    conn.setHostnameVerifier(new HostnameVerifier() {
	            @Override
	            public boolean verify(String arg0, SSLSession arg1) {
	                return true;
	            }
	        });
			conn.connect();  
			conn.setConnectTimeout(10000);  
			out = new OutputStreamWriter(conn.getOutputStream(),"UTF-8");
			out.append(paramsJson.toString());  
            out.flush();  
            out.close();
	   		BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(),"UTF-8"));
	   		String line = "";
	   		StringBuffer sb = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString();
	   		conn.disconnect();
		} catch (Exception e) {
			LOGGER.error("https post body请求失败", e);
			throw new RuntimeException("https post body请求失败", e);
		}finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
            	LOGGER.error("https post body请求失败", ex);
    			throw new RuntimeException("https post body请求失败", ex);
            }
        }
    	return result;
    }
    
    /**
     * 上传文件
     * @param actionUrl 服务器接口地址
     * @param file 文件
     */
    public static String uploadFile(String actionUrl, Map<String, Object> paramMap, File file){
    	OutputStream out = null;
    	DataInputStream in = null;
    	String result = "";
    	try {
    		String params = mapToString(paramMap);
    		actionUrl = actionUrl + "?" + params;
			URL url = new URL(actionUrl);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");  
			conn.setRequestProperty("Content-Type","text/html");  
			conn.setRequestProperty("Cache-Control","no-cache");  
			conn.setRequestProperty("Charsert", "UTF-8");   
			conn.connect();  
			conn.setConnectTimeout(10000);  
			out =conn.getOutputStream();  
			in = new DataInputStream(new FileInputStream(file));
			int bytes = 0;  
			byte[] buffer = new byte[1024];  
			while ((bytes = in.read(buffer)) != -1) {  
				out.write(buffer, 0, bytes);  
			}
	   		out.flush();  
	   		BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(),"UTF-8"));
	   		String line = "";
	   		StringBuffer sb = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString();
	   		conn.disconnect();
		} catch (Exception e) {
			throw new RuntimeException("上传文件失败"+ e);
		}finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
    	return result;
    }
    
}
