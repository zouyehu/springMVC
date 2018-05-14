package com.fulan.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Controller
@RequestMapping("file")
public class FileUpController {

    @RequestMapping(value = "fileUpload", method = RequestMethod.POST)
    @ResponseBody
    public String fileUpload(@RequestParam("policy")MultipartFile file){
        String path = "E:\\download";
        File resultFile = new File(path + File.separator + file.getOriginalFilename());
        if (!resultFile.getParentFile().exists()){
            resultFile.getParentFile().mkdirs();
        }
        if (null != file){
            System.out.println("文件名：" + file.getName());
            System.out.println(file.getOriginalFilename());
            try {
                file.transferTo(resultFile);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return "Success";
    }
}
