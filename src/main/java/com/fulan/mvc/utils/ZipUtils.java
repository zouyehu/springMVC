package com.fulan.mvc.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.*;

public class ZipUtils {

    private static final Logger logger = LogManager.getLogger(ZipUtils.class);
    private static final String PATH_SEP = System.getProperty("file.separator");
    public static final int BUFFER = 20480;

    /**
     * 对文件流解压
     *
     * @param inputStream
     * @param fileExtractPath
     * @return 解压的文件名称
     */
    public static String[] unzipFilesToPathWithChecksum(InputStream inputStream, final String fileExtractPath
    ) throws IOException {
        List<String> fileNames = new ArrayList<String>();
        final CheckedInputStream checkStream = new CheckedInputStream(inputStream, new CRC32());
        final ZipInputStream zis = new ZipInputStream(new BufferedInputStream(checkStream));
        Integer fileCount = 0;
        try {
            ZipEntry entry = null;
            while ((entry = zis.getNextEntry()) != null) {
                long size = entry.getSize();
                if (size == 0) {
                    continue;
                }
                String fileName = entry.getName();
                logger.info("zipEntryFileName:{}", fileName);
                //针对macOS系统下产生的一些文件不解压。
                if (fileName.contains("MACOSX") || fileName.contains("DS_Store")) {
                    logger.info("当前文件:{}不解压，直接跳过！", fileName);
                    continue;
                }
                fileCount++;
                final FileOutputStream fos = new FileOutputStream(fileExtractPath + PATH_SEP + entry.getName());
                final BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
                int b = -1;
                while ((b = zis.read()) != -1) {
                    dest.write(b);
                }
                if (StringUtils.isNotEmpty(fileName)) {
                    fileNames.add(fileName);
                }
                dest.flush();
                dest.close();
                fos.flush();
                fos.close();
            }
        } finally {
            zis.close();
            inputStream.close();
            checkStream.close();
        }
        return fileNames.toArray(new String[fileNames.size()]);
    }


    /**
     * 文件压缩成zip包
     *
     * @param srcPathName 需要打包的文件目录或文件
     * @param zipFile     生成的zip包文件名称
     */
    public static void compress(String srcPathName, String zipFile) {
        File file = new File(srcPathName);
        if (!file.exists())
            throw new RuntimeException(srcPathName + "不存在！");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream, new CRC32());
            ZipOutputStream out = new ZipOutputStream(cos);
            String basedir = "";
            compress(file, out, basedir);
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void compress(File file, ZipOutputStream out, String basedir) {
        /* 判断是目录还是文件 */
        if (file.isDirectory()) {
            compressDirectory(file, out, basedir);
        } else {
            compressFile(file, out, basedir);
        }
    }

    /**
     * 压缩一个目录
     */
    private static void compressDirectory(File dir, ZipOutputStream out, String basedir) {
        if (!dir.exists())
            return;

        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            /* 递归 */
            compress(files[i], out, basedir + dir.getName() + "/");
        }
    }

    /**
     * 压缩一个文件
     */
    private static void compressFile(File file, ZipOutputStream out, String basedir) {
        if (!file.exists()) {
            return;
        }
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(basedir + file.getName());
            out.putNextEntry(entry);
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = bis.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            bis.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        // 文件压缩测试
        ZipUtils.compress("D:\\test\\spring1\\testxml.xml", "D:\\test\\spring1\\test.zip");

    }
}
