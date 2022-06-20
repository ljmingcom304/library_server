package com.ljming.file.project.service;

import com.ljming.file.project.bean.FileProperties;

import java.io.InputStream;

public interface UploadServiceAPI {

    /**
     * 上傳服務
     */
    FileProperties upload(FileProperties fileProperties, InputStream in);

    /**
     * 下载httpPath 文件 并上传至本地服务器 返回访问地址
     */
    String uploadForHttpPath(FileProperties fileProperties);

    /**
     * 下載服務
     */
    InputStream download(String physicalPath);

}