package com.fulan.mvc.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

public class FileUtils {
    private static final Logger logger = LogManager.getLogger(FileUtils.class);

    /**
     * base64 加密
     *
     * @Description: 根据图片地址转换为base64编码字符串
     * @Author: Hu
     */
    public static String getEncode(String imgFile) {
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imgFile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
        } catch (IOException e) {
            logger.error("读文件异常：" + e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                    ;
                } catch (Exception e) {
                    logger.error("关闭文件流异常：" + e);
                }
            }
        }
        // 加密
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

    /**
     * base64 解密
     *
     * @param imgStr base64编码字符串
     * @param path   图片路径-具体到文件
     * @Description: 将base64编码字符串转换为图片
     * @Author: Hu
     */
    public static boolean getDecode(String imgStr, String path) {
        if (imgStr == null)
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        OutputStream out = null;
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        // 父目录或文件不存在则创建
        if (!file.getParentFile().exists()) {
            boolean mkdir = file.getParentFile().mkdirs();
            if (!mkdir) {
                logger.error("创建目录失败");
                return false;
            }
        }
        try {
            // 解密
            byte[] b = decoder.decodeBuffer(imgStr);
            // 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            // 判断图片大小
            int suffixIndex = path.lastIndexOf(".");
            String suffix = path.substring(suffixIndex);
            if (".jpg".equals(suffix)) {
                if (file.length() > 500 * 1024) {
                    logger.error("生成图片文件过大！");
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            logger.error("文件写入异常：" + e);
            return false;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                logger.error("文件流关闭异常：" + e);
                return false;
            }
        }
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     * @throws Exception
     */
    public static boolean copyFile(String oldPath, String newPath) {
        logger.info("---老文件路径:" + oldPath + "---新文件路径:" + newPath);
        boolean flag = false;
        File oldfile = new File(oldPath);
        if (!oldfile.exists()) {
            return flag;
        }
        File newFile = new File(newPath);
        File parentFile = newFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        if (oldfile.equals(newFile)) {
            return true;
        }
        InputStream in = null;
        FileOutputStream fs = null;
        try {
            in = new FileInputStream(oldPath); // 读入老文件
            fs = new FileOutputStream(newPath);
            byte[] buffer = new byte[5 * 1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                fs.write(buffer, 0, len);
                fs.flush();
            }
            flag = true;
        } catch (Exception e) {
            flag = false;
            logger.error("文件写入发生异常：" + e);
        } finally {
            try {
                if (in != null){
                    in.close();
                }
                if (fs != null){
                    fs.close();
                }
            }catch (IOException e){
                flag = false;
                logger.error("文件流关闭异常：" + e);
            }
        }
        return flag;
    }

    /**
     * 复制整个文件夹内容
     *
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     * @return boolean
     * @throws FileNotFoundException
     */
    public static boolean copyFolder(String oldPath, String newPath) {
        boolean flag = false;
        File parent = new File(newPath);// 如果新文件夹不存在 则建立新文件夹
        if (! parent.exists()) {
            parent.mkdirs();
        }
        File a = new File(oldPath);
        String[] file = a.list();
        File temp = null;
        for (int i = 0; i < file.length; i++) {
            if (oldPath.endsWith(File.separator)) {
                temp = new File(oldPath + file[i]);
            } else {
                temp = new File(oldPath + File.separator + file[i]);
            }

            if (temp.isFile()) {
                FileInputStream in = null;
                FileOutputStream out = null;
                try {
                    in = new FileInputStream(temp);
                    out = new FileOutputStream(newPath + "/" + (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len = 0;
                    while ((len = in.read(b)) != -1) {
                        out.write(b, 0, len);
                    }
                    out.flush();
                    flag = true;
                } catch (Exception e) {
                    flag = false;
                    logger.error("文件写入异常：" + e);
                } finally {
                   try {
                       if (out != null){
                           out.close();
                       }
                       if (in != null){
                           in.close();
                       }
                   }catch (Exception e){
                       flag = false;
                       logger.error("文件流关闭异常：" + e);
                   }
                }
            }
            if (temp.isDirectory()) {// 如果是子文件夹
                //copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                continue;
            }
        }
        return flag;
    }

    public static void main(String[] args) {
        // 1,Base64 加解密
        File image = new File("D:\\test\\image\\1.jpg");
        String path = "D:\\test\\image\\1.jpg";
        try {
            String data = FileUtils.getEncode(path);
            System.out.println("Base64加密：" + data);
            Boolean flag = FileUtils.getDecode(data, "D:\\test\\hu\\1-2.jpg");
            System.out.println("Base64解密：" + flag);
        } catch (Exception e) {
            logger.error("异常：" + e);
        }
    }
}