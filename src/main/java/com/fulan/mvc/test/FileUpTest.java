package com.fulan.mvc.test;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;

public class FileUpTest {

    public static void main(String[] args) {
        File file = new File("E:\\download\\2018042000000007-4.pdf");
        // http://192.168.8.57:8080/mac.service/fileUpload
        // http://localhost:8080/52receive/receive/fileUpload
        String remote_url = "http://localhost:8088/springMVC/file/fileUpload";
        try {
            FileInputStream fis = new FileInputStream(file);
            System.out.println(file.getName());
            MultipartFile multi = new MockMultipartFile("123456.pdf", fis);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            // String fileName = multi.getName();
            HttpPost httpPost = new HttpPost(remote_url);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody("policy", multi.getInputStream(), ContentType.MULTIPART_FORM_DATA, "file.pdf");// 文件流
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
//            httpPost.setHeader("Content-Type", "multipart/form-data");
            HttpResponse response = httpClient.execute(httpPost);// 执行提交
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                // 将响应内容转换为字符串
                String result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
                System.out.println(result);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
