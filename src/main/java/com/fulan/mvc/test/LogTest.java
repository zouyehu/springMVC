package com.fulan.mvc.test;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.*;

public class LogTest {
    private static final Logger logger = LogManager.getLogger(LogTest.class);

    public static void main(String[] args) throws IOException {
        /**
         * java代码访问百度
         * 1.创建URL对象
         * 2.创建Http链接
         */
//        System.setProperty("http.proxySet", "true");
//        System.setProperty("http.proxyHost", "192.168.8.1290");
//        System.setProperty("http.proxyPort", "1002");
//
//        URL url = new URL("http://www.baidu.com");
//        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//        /**
//         * 3.设置请求方式
//         * 4.设施请求内容类型
//         */
//        httpURLConnection.setRequestMethod("GET");
//        httpURLConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
//        httpURLConnection.setDoInput(true);
//        httpURLConnection.setDoOutput(true);
//        /**
//         * 5.设置请求参数
//         * 6.使用输出流发送参数
//         */
//        //String content="username:";
//        //OutputStream outputStream = httpURLConnection.getOutputStream();
//        //outputStream.write(content.getBytes());
//        /**
//         * 7.使用输入流接受数据
//         */
//        InputStream inputStream = httpURLConnection.getInputStream();
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();//此处可以用Stringbuffer等接收
//        byte[] b = new byte[1024];
//        int len = 0;
//        while (true) {
//            len = inputStream.read(b);
//            if (len == -1) {
//                break;
//            }
//            byteArrayOutputStream.write(b, 0, len);
//        }
//        System.out.println(byteArrayOutputStream.toString());

//        System.setProperty("http.proxySet", "true");
//        System.setProperty("http.proxyHost", "192.168.8.1290");
//        System.setProperty("http.proxyPort", "1002");

        //直接访问目的地址
//        URL url = new URL("http://www.baidu.com");
//        URLConnection con = url.openConnection();
//        InputStreamReader isr = new InputStreamReader(con.getInputStream());
//        char[] cs = new char[1024];
//        int i = 0;
//        while ((i = isr.read(cs)) > 0) {
//            System.out.println(new String(cs, 0, i));
//        }
//        isr.close();


            URL url = new URL("http://www.baidu.com");
            // /创建代理服务器
            InetSocketAddress addr = new InetSocketAddress("192.168.0.254", 8080);
            // Proxy proxy = new Proxy(Proxy.Type.SOCKS, addr); // Socket 代理
            Proxy proxy = new Proxy(Proxy.Type.HTTP, addr); // http 代理
            Authenticator.setDefault(new MyAuthenticator("username", "password"));// 设置代理的用户和密码
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);// 设置代理访问
            InputStreamReader in = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(in);
            while (true) {
                String s = reader.readLine();
                if (s != null) {
                    System.out.println(s);
                }
            }
        }

        static class MyAuthenticator extends Authenticator {
            private String user = "";
            private String password = "";

            public MyAuthenticator(String user, String password) {
                this.user = user;
                this.password = password;
            }

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password.toCharArray());
            }
        }

    }


