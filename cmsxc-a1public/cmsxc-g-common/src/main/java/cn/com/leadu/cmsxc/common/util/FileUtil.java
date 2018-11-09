package cn.com.leadu.cmsxc.common.util;

import cn.com.leadu.cmsxc.common.exception.CmsServiceException;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FileUtils;
import java.io.*;

/**
 * Created by qiaohao on 2017/9/14.
 */
public class FileUtil {

    /**
     * 按行读取项目中文件，并且换行
     * @return
     */
    public static String readLineFileln(String filePath,Class clazz) throws Exception{
        InputStream inputStream = clazz.getResourceAsStream(filePath);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String lineTxt = null;
        StringBuffer result =new StringBuffer();
        while((lineTxt = bufferedReader.readLine()) != null){
            result.append(lineTxt+"\n");
        }
        return result.toString();
    }

    /**
     * 向指定目录输出指定文件
     * @param filePath
     * @param fileName
     * @param data
     */
    public static void outFile(String filePath,String fileName,String data){
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;

        try {
            File file = new File(filePath);
            if(!file.isDirectory())
                file.mkdirs();
            file = new File(filePath+"/"+fileName);
            if(!file.exists()){
                file.createNewFile();
            }
            fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(data);
            bufferedWriter.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            try {
                if (bufferedWriter != null)
                    bufferedWriter.close();
                if (fileWriter != null)
                    fileWriter.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    /**
     * 获取文件后缀名
     *
     * @param fileName
     * @return
     */
    public static String getFileSuffix(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }
    /**
     * 获取文件后缀名
     *
     * @param fileName
     * @return
     */
    public static String getFileName(String fileName){
        return fileName.replace(fileName.substring(fileName.lastIndexOf(".")),"");
    }
    /**
     * 保存文件
     * @param nameUUID
     * @param savePath  文件保存路径
     * @param file
     * @param serverPath  文件访问服务器路径
     * @return  文件访问路径
     */
    public static String saveFile(String nameUUID ,String savePath, MultipartFile file, String serverPath){
        if (file != null && !file.isEmpty()) {
            String fileName = nameUUID + FileUtil.getFileSuffix(file.getOriginalFilename());
            try {
                FileUtils.writeByteArrayToFile(new File(savePath + fileName), file.getBytes());
                return serverPath + fileName;
            } catch (Exception e) {
                e.printStackTrace();
                throw new CmsServiceException("上传失败");
            }
        }
        throw new CmsServiceException("文件为空,上传失败");
    }

}
