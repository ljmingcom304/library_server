package com.ljming.file.project.controller;


import com.ljming.file.project.config.MinioConfig;
import com.ljming.file.project.utils.MinioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * Title:MinioController
 * <p>
 * Description:
 * </p >
 * Author Jming.L
 * Date 2022/4/13 12:04
 */
@RestController
public class MinioController {

    @Autowired
    MinioConfig minioConfig;

    /**
     * 上传文件到minio服务
     */
    @RequestMapping("upload")
    public String upload(@RequestParam("fileName") MultipartFile file, String bucketName) {
        try {
            MinioUtil instance = MinioUtil.getInstance(minioConfig.getMinioClient());
            instance.putObject(bucketName, file.getOriginalFilename(), file.getInputStream());
            String objectUrl = instance.presignedGetObject(bucketName, file.getOriginalFilename(), 100000);
            return objectUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return "上传失败";
        }
    }

    @RequestMapping("getUrl")
    public String getUrl(String bucketName, String fileName) {
        try {
            MinioUtil instance = MinioUtil.getInstance(minioConfig.getMinioClient());
            String objectUrl = instance.getObjectUrl(bucketName, fileName);
            return objectUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return "获取URL失败";
        }
    }

    @RequestMapping("download")
    public String download(HttpServletResponse response, String bucketName, String fileName) {
        try {
            MinioUtil instance = MinioUtil.getInstance(minioConfig.getMinioClient());
            instance.downloadFile(bucketName, fileName, response);
            return "下载完成";
        } catch (Exception e) {
            e.printStackTrace();
            return "下载失败";
        }
    }
}
