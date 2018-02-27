package com.chelsea.java_common;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.net.telnet.TelnetClient;

/**
 * telnet客户端工具类
 * 
 * @author shevchenko
 *
 */
public class TelnetClientUtil {
    private TelnetClient telnet;
    /**
     * 输入流,接收返回信息
     */
    private BufferedInputStream in;
    /**
     * 向服务器写入 命令
     */
    private PrintStream out;

    private static List<Character> chars = Arrays.asList(new Character[] {':' , '>', '$', '#', 'H'});

    private static final String FAILED = "Login Failed";

    /**
     * @param termtype 协议类型：VT100、VT52、VT220、VTNT、ANSI
     */
    public TelnetClientUtil(String termtype) {
        telnet = new TelnetClient(termtype);
    }

    public TelnetClientUtil() {
        telnet = new TelnetClient();
    }

    /**
     * 登录到目标主机
     * 
     * @param ip
     * @param port
     * @param username
     * @param password
     */
    public boolean login(String ip, int port, String username, String password) {
        try {
            telnet.connect(ip, port);
            in = new BufferedInputStream(telnet.getInputStream());
            out = new PrintStream(telnet.getOutputStream());
//            readUntil(null);
            write(username);
            readUntil(null);
            write(password);
            String rs = readUntil(null);
            if (rs != null && rs.contains(FAILED)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 读取分析结果
     * 
     * @param pattern 匹配到该字符串时返回结果
     * @return
     */
    public String readUntil(String pattern) {
        StringBuilder result = new StringBuilder();
        try {
            char ch;
            int code = -1;
            while ((code = in.read()) != -1) {
                ch = (char) code;
                result.append(ch);
                if (chars.contains(ch) && !(in.available() > 1)) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }


    /**
     * 发送命令
     * 
     * @param value
     */
    public void write(String value) {
        try {
            out.println(value);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送命令,返回执行结果
     * 
     * @param command
     * @return
     */
    public String sendCommand(String command) {
        try {
            write(command);
            String rs = readUntil(null);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭连接
     */
    public void distinct() {
        try {
            write("exit");
            if (telnet != null && !telnet.isConnected()) {
                telnet.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 是否连接到服务器
     * @return
     */
    public boolean isConnected(){
        try {
            return telnet.isConnected();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) throws InterruptedException {
        // Windows,用VT220,否则会乱码
        TelnetClientUtil telnet = new TelnetClientUtil("VT220");
        boolean loginstatus = telnet.login("119.23.250.138", 23, "dev1", "dev1");
        if (loginstatus) {
            String rs = telnet.sendCommand("ls");
            try {
                // 转一下编码
                rs = new String(rs.getBytes("ISO-8859-1"), "GBK");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println(rs);
        }
        telnet.distinct();
    }

}
