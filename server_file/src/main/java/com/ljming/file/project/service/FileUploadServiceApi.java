package com.ljming.file.project.service;


import com.ljming.file.project.bean.FileProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;

/**
 * Title:FileUploadServiceApi
 * <p>
 * Description:文件上传—如头像上传等
 * </p >
 * Author Jming.L
 * Date 2022/4/13 13:49
 */
@Service
public class FileUploadServiceApi implements UploadServiceAPI {

    @Autowired
    FileUploadService uploadService;

    /**
     * 下载httpPath 文件 并上传至本地服务器 返回访问地址
     */
    @Override
    public String uploadForHttpPath(FileProperties fileProperties) {
        try {
            if (StringUtils.isBlank(fileProperties.getHttpPath())) {
                throw new Exception("httpPath cannot be empty!");
            }
            URL url = new URL(fileProperties.getHttpPath());
            InputStream download = url.openStream();
            FileProperties path = uploadService.upload(fileProperties, download);
            return path.getHttpPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public FileProperties upload(FileProperties fileProperties, InputStream in) {
        return uploadService.upload(fileProperties, in);
    }

    @Override
    public InputStream download(String physicalPath) {
        return uploadService.download(physicalPath);
    }


}