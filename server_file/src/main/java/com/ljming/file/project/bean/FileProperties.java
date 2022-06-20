package com.ljming.file.project.bean;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * Title:FileProperties
 * <p>
 * Description:文件上传下载
 * </p >
 * Author Jming.L
 * Date 2022/4/13 12:45
 */
public class FileProperties implements Serializable {

    /**
     * serialVersionUID
     **/
    private static final long serialVersionUID = 1L;

    /**
     * 文件上传目标地址 如 /user/icon
     */
    private String destPath;

    /**
     * 上传文件名称 testm.jpg
     **/
    private String fileName;

    /**
     * 上传文件类型 jpeg/jpg
     **/
    private String fileContentType;

    /**
     * 上传文件大小
     **/
    private long fileSize;

    /**
     * 文件保存地址
     **/
    private String absolutePath;

    /**
     * 访问地址
     **/
    private String httpPath;

    /**
     * 压缩后图片的路径 如果不是图片为空
     */
    private String HttpThumbPath;

    /**
     * 状态:1成功,0失败
     **/
    private Integer state;

    /**
     * 状态描述
     */
    private String des;

    /**
     * 是否需要重命名
     */
    //@JSONField(serialize = false)
    private boolean needRename = true;

    /**
     * 是否需要PathFormat
     */
    //@JSONField(serialize = false)
    private boolean pathFormat = true;

    /**
     * PDF转换成SWF文件开关
     **/
    private boolean convertToSWF = false;

    /**
     * Office转换成PDF文件开关
     **/
    private boolean convertToPDF = false;

    /**
     * 访问地址
     **/
    private FileProperties swfFileProperties;

    /**
     * 文件保存Swf地址
     **/
    private String absoluteSwfPath;

    /**
     * 访问Swf地址
     **/
    private String httpSwfPath;

    /**
     * 文件保存PDF地址
     **/
    private String absolutePdfPath;

    /**
     * 访问PDF地址
     **/
    private String httpPdfPath;

    public FileProperties() {

    }

    public FileProperties(MultipartFile file) {

        this.fileName = file.getOriginalFilename();
        this.fileContentType = file.getContentType();
        this.fileSize = file.getSize();

    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getHttpPath() {
        return httpPath;
    }

    public void setHttpPath(String httpPath) {
        this.httpPath = httpPath;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDestPath() {
        return destPath;
    }

    public void setDestPath(String destPath) {
        this.destPath = destPath;
    }

    public boolean getNeedRename() {
        return needRename;
    }

    public void setNeedRename(boolean needRename) {
        this.needRename = needRename;
    }

    public boolean getPathFormat() {
        return pathFormat;
    }

    public void setPathFormat(boolean pathFormat) {
        this.pathFormat = pathFormat;
    }

    public boolean isConvertToSWF() {
        return convertToSWF;
    }

    public void setConvertToSWF(boolean convertToSWF) {
        this.convertToSWF = convertToSWF;
    }

    public FileProperties getSwfFileProperties() {
        return swfFileProperties;
    }

    public void setSwfFileProperties(FileProperties swfFileProperties) {
        this.swfFileProperties = swfFileProperties;
    }

    public boolean isConvertToPDF() {
        return convertToPDF;
    }

    public void setConvertToPDF(boolean convertToPDF) {
        this.convertToPDF = convertToPDF;
    }

    public String getAbsoluteSwfPath() {
        return absoluteSwfPath;
    }

    public void setAbsoluteSwfPath(String absoluteSwfPath) {
        this.absoluteSwfPath = absoluteSwfPath;
    }

    public String getHttpSwfPath() {
        return httpSwfPath;
    }

    public void setHttpSwfPath(String httpSwfPath) {
        this.httpSwfPath = httpSwfPath;
    }

    public String getAbsolutePdfPath() {
        return absolutePdfPath;
    }

    public void setAbsolutePdfPath(String absolutePdfPath) {
        this.absolutePdfPath = absolutePdfPath;
    }

    public String getHttpPdfPath() {
        return httpPdfPath;
    }

    public void setHttpPdfPath(String httpPdfPath) {
        this.httpPdfPath = httpPdfPath;
    }


    public String getHttpThumbPath() {
        return HttpThumbPath;
    }

    public void setHttpThumbPath(String httpThumbPath) {
        HttpThumbPath = httpThumbPath;
    }

    @Override
    public String toString() {
        return "FileProperties{" +
                "destPath='" + destPath + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileContentType='" + fileContentType + '\'' +
                ", fileSize=" + fileSize +
                ", absolutePath='" + absolutePath + '\'' +
                ", httpPath='" + httpPath + '\'' +
                ", HttpThumbPath='" + HttpThumbPath + '\'' +
                ", state=" + state +
                ", des='" + des + '\'' +
                ", needRename=" + needRename +
                ", pathFormat=" + pathFormat +
                ", convertToSWF=" + convertToSWF +
                ", convertToPDF=" + convertToPDF +
                ", swfFileProperties=" + swfFileProperties +
                ", absoluteSwfPath='" + absoluteSwfPath + '\'' +
                ", httpSwfPath='" + httpSwfPath + '\'' +
                ", absolutePdfPath='" + absolutePdfPath + '\'' +
                ", httpPdfPath='" + httpPdfPath + '\'' +
                '}';
    }
}
