package com.ljming.file.project.service;

import com.alibaba.fastjson.JSON;
import com.ljming.file.project.bean.FileProperties;
import com.ljming.file.project.bean.UploadProperties;
import com.ljming.file.project.utils.CheckNull;
import com.ljming.file.project.utils.PathFormat;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;

import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Title:FileUploadService
 * <p>
 * Description:
 * </p >
 * Author Jming.L
 * Date 2022/4/13 12:53
 */
@Slf4j
@EnableConfigurationProperties(UploadProperties.class)
@Service
public class FileUploadService {

    private static Logger log = LoggerFactory.getLogger(FileUploadService.class);

    @Autowired
    private UploadProperties uploadProperties;

    private String[] filepaths;


    //给压缩后的图片加前缀
    private static final String THUMB = "thumb_";

    public static final String[] OFFICE_POSTFIXS = {".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx"};

    private static int port = 8200;

    public FileProperties upload(FileProperties fileProperties, InputStream in) {
        log.debug(String.format("开始上传文件,文件参数为：%s", JSON.toJSONString(fileProperties)));

        try {
            //文件名，如：80939a83c2de45ec43165d51147e1190.jpg
            String fileName = fileProperties.getFileName();
            if (fileProperties.getNeedRename()) {
                fileName = reNameFile(fileName);
            }

            //根据控制层输入目标路径和时间重新生成目标路径，如：/article/2521+ /2017/09/11/164230_019267
            StringBuffer destPathBuffer = new StringBuffer(fileProperties.getDestPath());
            if (fileProperties.getPathFormat()) { // 需要做路径格式化
                destPathBuffer.append(PathFormat.parse(PathFormat.PATH_FORMAT_DEFAULT));
            }

            String destination = getFileSaveFullPath(destPathBuffer.toString(), fileName, fileProperties.getPathFormat());
            String visitPath = getVisitPath(destPathBuffer.toString(), fileName, fileProperties.getPathFormat());

            File saveFile = new File(destination);
            // create parent folder
            if (!saveFile.getParentFile().exists()) {
                saveFile.getParentFile().mkdirs();
            }

            log.debug(String.format("文件保存路径为:%s,文件参数为：%s", destination, JSON.toJSONString(fileProperties)));
            fileProperties.setAbsolutePath(destination);
            fileProperties.setHttpPath(visitPath);
            //是否是图片,如果是图片，对图片进行压缩上传，否则直接上传
            if (isImage(fileProperties.getFileName())) {

                Thumbnails.of(in).scale(1f).outputQuality(0.5f).toFile(saveFile);//压缩比例0.5
            } else {
                FileUtils.copyInputStreamToFile(in, saveFile);
            }

            //如果文件为PDF，查看是否需要转换为SWF
            if (fileProperties.getFileName().toUpperCase().endsWith(".PDF") && !fileProperties.isConvertToSWF()) {

                filepaths = getInputFilePath(fileProperties, in, ".swf");
                if (filepaths == null && filepaths.length == 0) {
                    return fileProperties;
                }

                File swfFile = new File(filepaths[0]);
                boolean flag = convert2SWF(saveFile, swfFile);
                if (flag) {
                    fileProperties.setAbsoluteSwfPath(filepaths[0]);
                    fileProperties.setHttpSwfPath(filepaths[1]);
                }
            }

            //如果文件为Office中的Word,Excel，PPT文件，查看是否需要转换为PDF
            String extenstion = getFileExtenstion(fileProperties.getFileName()).toLowerCase();
            boolean isOffice = Arrays.asList(OFFICE_POSTFIXS).contains(extenstion);
            if (isOffice && !fileProperties.isConvertToPDF()) {

                //生成PDF文件路径
                filepaths = getInputFilePath(fileProperties, in, ".pdf");
                if (filepaths == null && filepaths.length == 0) {
                    return fileProperties;
                }

                String pdfFilePath = filepaths[0];
                File pdfFile = new File(filepaths[0]);
                boolean pdfFlag = convert2PDF(saveFile, pdfFile);
                if (!pdfFlag) {
                    return fileProperties;
                }

                fileProperties.setAbsolutePdfPath(filepaths[0]);
                fileProperties.setHttpPdfPath(filepaths[1]);

                //如果文件为PDF，查看是否需要转换为SWF
                if (pdfFilePath.toUpperCase().endsWith(".PDF") && !fileProperties.isConvertToSWF()) {

                    filepaths = getInputFilePath(fileProperties, in, ".swf");
                    if (filepaths == null && filepaths.length == 0) {
                        return fileProperties;
                    }

                    File swfFile = new File(filepaths[0]);
                    boolean flag = convert2SWF(pdfFile, swfFile);
                    if (flag) {
                        fileProperties.setAbsoluteSwfPath(filepaths[0]);
                        fileProperties.setHttpSwfPath(filepaths[1]);
                    }

                }

            }

            return fileProperties;
        } catch (Exception e) {
            log.error("保存文件出错，参数为：" + JSON.toJSONString(fileProperties));
        }
        log.debug("上传文件结束");
        return null;
    }

    public InputStream download(String physicalPath) {
        log.debug(String.format("开始下载文件,文件存储路径参数为：%s", physicalPath));
        try {
            return new FileInputStream(physicalPath);
        } catch (IOException e) {
            log.error("下载文件时，在文件系统中没有找到文件，文件路径为：" + physicalPath);
        }
        return null;
    }


    /**
     * 判断是否是图片
     */
    private boolean isImage(InputStream inputStream) {
        if (inputStream == null) {
            return false;
        }
        Image img;
        try {
            img = ImageIO.read(inputStream);
            return !(img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 根据文件后缀判断是否是图片
     */
    private boolean isImage(String fileName) {
        String fileNameExtension = fileName.contains(".") ? fileName.substring(fileName.indexOf(".") + 1, fileName.length()) : null;
        String[] extensions = {"bmp", "jpg", "gif", "jpeg", "tiff", "tga", "svg", "pcx", "psd"};
        long length = Arrays.stream(extensions).filter(i -> i.equalsIgnoreCase(fileNameExtension)).count();
        if (length > 0) {
            return true;
        }
        return false;
    }

    /**
     * 获得文件访问路径
     *
     * @param destPath /user/icon
     * @param fullName 342343.jpg
     * @return http://res.mmednet.com/static/media/user/icon/342343.jpg
     */
    private String getVisitPath(String destPath, String fullName, boolean pathFormated) {
        StringBuffer visitPathBuffer = new StringBuffer();
        visitPathBuffer.append(uploadProperties.getResourceVisitDomain()).append(uploadProperties.getResourceVisitUrlPrefix())
                .append(destPath);
        if (pathFormated) {
            visitPathBuffer.append(fullName);
        } else {
            visitPathBuffer.append(File.separator).append(fullName);
        }

        return visitPathBuffer.toString();
    }

    /**
     * 获得保存文件的物理路径
     *
     * @param destPath     如 /user/icon
     * @param fileFullName 如 342343.jpg
     */
    private String getFileSaveFullPath(String destPath, String fileFullName, boolean pathFormated) {
        StringBuffer filePath = getPhysicalDirPath(destPath);
        if (pathFormated) {
            filePath.append(fileFullName);
        } else {
            filePath.append(File.separator).append(fileFullName);
        }
        return filePath.toString();
    }

    /**
     * 重命名文件
     *
     * @param fileName 342343
     * @param fileName jpg
     */
    private String reNameFile(String fileName) {
        return UUID.randomUUID().toString() + getFileExtenstion(fileName);
    }

    /**
     * 获得文件名称后缀
     *
     * @param fileName testfile.jpg
     * @return .jpg
     */
    private String getFileExtenstion(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."), fileName.length());
    }

    /**
     * 获得存储路径
     *
     * @param destPath /user/icon
     */
    private StringBuffer getPhysicalDirPath(String destPath) {
        StringBuffer pathbuffer = new StringBuffer(uploadProperties.getBasePhysicalPath());
        if (destPath.startsWith("/")) {
            pathbuffer.append(destPath);
        } else {
            pathbuffer.append(File.separator).append(destPath);
        }
        return pathbuffer;
    }

    private boolean convert2PDF(File inputFile, File pdfFile) {

        log.debug("Office文档转换PDF...");
        ArrayList<String> office_Formats = new ArrayList<String>();
        Collections.addAll(office_Formats, OFFICE_POSTFIXS);
        if (office_Formats.contains(getFileExtenstion(inputFile.getPath()))) {
            // 启动office服务
            OfficeManager officeManager = startService();

            if (!CheckNull.isNull(officeManager)) {
                Date start = new Date();
                try {
                    log.debug("进行文档转换:" + inputFile.getName());
                    OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
                    converter.convert(inputFile, pdfFile);

                } catch (Exception e) {
                    return false;
                } finally {
                    stopService(officeManager);
                }

                long l = (start.getTime() - System.currentTimeMillis());
                long day = l / (24 * 60 * 60 * 1000);
                long hour = (l / (60 * 60 * 1000) - day * 24);
                long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
                long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
                log.debug("生成pdf文件" + pdfFile.getName() + "耗费：" + min + "分" + s + "秒");
                if (pdfFile.exists()) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    private OfficeManager startService() {

        log.debug("准备启动服务....");
        OfficeManager officeManager = null;
        DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();

        try {
            String osName = System.getProperty("os.name");
            String sys_office_home = "";
            if (Pattern.matches("Linux.*", osName))

            {
                sys_office_home = uploadProperties.getOpenOfficeHome4Linux();
            } else if (Pattern.matches("Windows.*", osName)) {
                sys_office_home = uploadProperties.getOpenOfficeHome4Win();
            }
            configuration.setOfficeHome(sys_office_home);// 设置OpenOffice.org安装目录
            configuration.setPortNumbers(port); // 设置转换端口，默认为8100
            configuration.setMaxTasksPerProcess(3);// 设置进程
            configuration.setTaskExecutionTimeout(1000 * 60 * 3L);// 设置任务执行超时分钟
            configuration.setTaskQueueTimeout(1000 * 60 * 60 * 24L);// 设置任务队列超时小时
            officeManager = configuration.buildOfficeManager();
            officeManager.start(); // 启动服务
            log.debug("OFFICE服务启动成功!");
        } catch (Exception ce) {
            log.debug("office服务启动失败! officeManager:" + officeManager);
            ce.printStackTrace();
        }
        return officeManager;
    }

    private static void stopService(OfficeManager officeManager) {
        log.debug("关闭OFFICE服务....");
        if (officeManager != null) {
            try {
                officeManager.stop();
            } catch (Exception e) {
                log.debug("关闭OFFICE服务失败! officeManager:" + officeManager);
                e.printStackTrace();
            }
            log.debug("成功关闭OFFICE服务!");
        }

    }


    /**
     * <p>
     * 生成输入文件的物理路径和访问路径
     * </p>
     *
     * @param fileProperties 上传文件属性
     * @param in             输入流
     * @param extenstionName 文件扩展名
     * @return String[]
     * @author Dylan.Xu
     * @date 2017年09月11日 上午11:07:27
     */
    private String[] getInputFilePath(FileProperties fileProperties, InputStream in, String extenstionName) {

        // 输入文件的物理路径
        String destination = replaceExtenstionName(fileProperties.getAbsolutePath(), extenstionName);
        // 生成输入文件的访问路径
        String visitPath = replaceExtenstionName(fileProperties.getHttpPath(), extenstionName);

        try {
            File saveFile = new File(destination);
            if (!saveFile.getParentFile().exists()) {
                saveFile.getParentFile().mkdirs();
            }

            log.debug(String.format("生成输入文件的物理路径为:%s,访问路径为：%s", destination, visitPath));
            FileUtils.copyInputStreamToFile(in, saveFile);
            String[] Filepaths = new String[2];
            Filepaths[0] = destination;
            Filepaths[1] = visitPath;
            return Filepaths;
        } catch (IOException e) {
            log.error(String.format("保存输入文件出错，参数为", destination, visitPath));
            return null;
        }
    }

    /**
     * <p>
     * PDF转换成SWF的处理
     * </p>
     *
     * @param pdfFile 上传文件属性
     * @param swfFile 保存的swf路径
     * @author Dylan.Xu
     * @date 2017年09月11日 上午11:07:27
     */
    private boolean convert2SWF(File pdfFile, File swfFile) {

        String osName = System.getProperty("os.name");
        String xpdfPath = "";
        String pdf2swf = "";
        String command = "";
        String pdfFilePath = pdfFile.getAbsolutePath();
        String swfFilePath = swfFile.getAbsolutePath();
        if (Pattern.matches("Linux.*", osName)) {
            pdf2swf = uploadProperties.getPdf2swf4Linux();
            xpdfPath = uploadProperties.getXpdfPath4Linux();
            command = pdf2swf + " -s languagedir=" + xpdfPath + " -T 9 -s poly2bitmap -s zoom=150 -s flashversion=9 "
                    + pdfFilePath + " -o " + swfFilePath;

        } else if (Pattern.matches("Windows.*", osName)) {
            pdf2swf = uploadProperties.getPdf2swf4Win();
            xpdfPath = uploadProperties.getXpdfPath4Win();
            command = pdf2swf + " -o " + swfFilePath + " -t " + pdfFilePath
                    + " -T 9 -s languagedir=" + xpdfPath;
        }

        try {
            log.debug("开始转换文档: " + pdfFile.getName());
            Process exec = Runtime.getRuntime().exec(command);
            /*BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
            while (bufferedReader.readLine() != null) {

			}*/
            //exec.waitFor();
            log.debug("PDF成功转换为SWF文件！");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("PDF转换为SWF文件失败！");
            return false;
        }

    }

    /**
     * <p>
     * 替换文件名的后缀
     * </p>
     *
     * @param fileName       文件名称
     * @param extenstionName 扩展名
     * @author Dylan.Xu
     * @date 2017年09月11日 上午11:07:27
     */
    private String replaceExtenstionName(String fileName, String extenstionName) {

        if (fileName.indexOf(".") >= 0) {
            fileName = fileName.substring(0, fileName.lastIndexOf(".")).concat(extenstionName);
        }
        return fileName;
    }

}