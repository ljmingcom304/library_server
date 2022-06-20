package com.ljming.file.project.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Title:FileProperties
 * <p>
 * Description:文件上传下载相关属性
 * </p >
 * Author Jming.L
 * Date 2022/4/13 11:24
 */
@ConfigurationProperties(prefix = "spring.upload")
@Component
public class UploadProperties {

    /**
     * 存储物理路径base
     */
    private String basePhysicalPath;

    /**
     * 访问路径域名
     */
    private String resourceVisitDomain;

    /**
     * 资源访问 PATH
     */
    private String resourceVisitUrlPrefix;

    /**
     * openOffice在Win环境下面的安装路径
     */
    private String openOfficeHome4Win;

    /**
     * openOffice在Linux环境下面的安装路径
     */
    private String openOfficeHome4Linux;

    /**
     * swf在Win环境下面的安装路径
     */
    private String pdf2swf4Win;

    /**
     * swf在Linux环境下面的安装路径
     */
    private String pdf2swf4Linux;

    /**
     * swf2pdf转swf时在Win下面的字符集
     */
    private String xpdfPath4Win;

    /**
     * swf2pdf转swf时在Linux下面的字符集
     */
    private String xpdfPath4Linux;

    public String getBasePhysicalPath() {
        return basePhysicalPath;
    }

    public void setBasePhysicalPath(String basePhysicalPath) {
        this.basePhysicalPath = basePhysicalPath;
    }

    public String getResourceVisitDomain() {
        return resourceVisitDomain;
    }

    public void setResourceVisitDomain(String resourceVisitDomain) {
        this.resourceVisitDomain = resourceVisitDomain;
    }

    public String getResourceVisitUrlPrefix() {
        return resourceVisitUrlPrefix;
    }

    public void setResourceVisitUrlPrefix(String resourceVisitUrlPrefix) {
        this.resourceVisitUrlPrefix = resourceVisitUrlPrefix;
    }

    public String getOpenOfficeHome4Win() {
        return openOfficeHome4Win;
    }

    public void setOpenOfficeHome4Win(String openOfficeHome4Win) {
        this.openOfficeHome4Win = openOfficeHome4Win;
    }

    public String getOpenOfficeHome4Linux() {
        return openOfficeHome4Linux;
    }

    public void setOpenOfficeHome4Linux(String openOfficeHome4Linux) {
        this.openOfficeHome4Linux = openOfficeHome4Linux;
    }

    public String getPdf2swf4Win() {
        return pdf2swf4Win;
    }

    public void setPdf2swf4Win(String pdf2swf4Win) {
        this.pdf2swf4Win = pdf2swf4Win;
    }

    public String getPdf2swf4Linux() {
        return pdf2swf4Linux;
    }

    public void setPdf2swf4Linux(String pdf2swf4Linux) {
        this.pdf2swf4Linux = pdf2swf4Linux;
    }

    public String getXpdfPath4Win() {
        return xpdfPath4Win;
    }

    public void setXpdfPath4Win(String xpdfPath4Win) {
        this.xpdfPath4Win = xpdfPath4Win;
    }

    public String getXpdfPath4Linux() {
        return xpdfPath4Linux;
    }

    public void setXpdfPath4Linux(String xpdfPath4Linux) {
        this.xpdfPath4Linux = xpdfPath4Linux;
    }

}
