package cn.com.leadu.cmsxc.system.service.impl;

import cn.com.leadu.cmsxc.common.util.StringUtil;
import cn.com.leadu.cmsxc.system.config.EtonenetParam;
import cn.com.leadu.cmsxc.system.service.MessageService;
import cn.com.leadu.cmsxc.data.appbusiness.repository.MessageLogRepository;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.MessageLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by yuanzhenxia on 2018/1/15.
 *
 * 短信获取验证码
 */
@Service
public class MessageServiceImpl implements MessageService{
    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);
    @Autowired
    private MessageLogRepository messageLogRepository;
    @Autowired
    private EtonenetParam etonenetParam;
    /**
     * Hex编码字符组
     */
    private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     * 发送短信
     *
     * @param phoneNum 手机号码
     * @param insiderPhone 内勤人员手机号
     * @param plate 车牌号
     * @param outTimeDate 授权失效时间
     * @param projectName 工程名称
     * @param serviceName 服务名称
     * @param classFunctionName 方法路径
     * @return
     */
    public String sendMessage(String phoneNum, String insiderPhone, String plate,String outTimeDate,String projectName,String serviceName,String classFunctionName) throws Exception{
        try{
            //发送http请求，并接收http响应
            String resStr = doGetRequest(makeMessageUrl(phoneNum, insiderPhone, plate,outTimeDate,projectName,serviceName,classFunctionName));
            //解析响应字符串
            parseResStr(resStr);
            return resStr;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("短信内容编码error",e);
            throw e;
        }
    }

    /**
     * 构造发送短信Url
     *
     * @param phoneNum 获得授权的用户的手机号码
     * @param insiderPhone 获得授权的用户所在公司内勤的手机号码
     * @param plate 车牌号
     * @param outTimeDate 授权失效时间
     * @param projectName 工程名称
     * @param serviceName 服务名称
     * @param classFunctionName 方法路径
     * @return
     */
    private String makeMessageUrl(String phoneNum,String insiderPhone,String plate, String outTimeDate,String projectName,String serviceName,String classFunctionName) throws Exception{
        //如果内勤人员手机号为空，则是给申请人员发短信，否则是给内勤人员发短信
        String phone = StringUtil.isNull(insiderPhone) ? phoneNum : insiderPhone;
        //目标号码，必填参数
        String da = "86"+phone;
        //下行内容以及编码格式，必填参数
        int dc = 15;
        // 短信内容 恭喜您已获得车牌号为皖A44444的车辆授权，授权截止日期：2018.1.9 21:00，请在该日期之前完成收车。
        String content = "";
        if(StringUtil.isNull(insiderPhone)){
            content = "【赏金寻车】您已获得悬赏任务【" + plate + "】的短期授权，请及时处理，授权截止日期：" + outTimeDate; //给申请人员发送短信
        } else {
            content = "【赏金寻车】您公司已获得悬赏任务【" + plate + "】的短期授权，请及时处理，授权截止日期：" + outTimeDate; //给内勤人员发送短信
        }
        String sm = encodeHexStr(dc,content);//下行内容进行Hex编码，此处dc设为15，即使用GBK编码格式
        // 登录到短信日志表
        saveMessageLog(phone,content,projectName,serviceName,classFunctionName);
        //组成url字符串
        String smsUrl = etonenetParam.getMtUrl() + "?command=" + etonenetParam.getCommand() + "&spid=" + etonenetParam.getSpid()
                + "&sppassword=" + etonenetParam.getSppassword() + "&spsc=" + etonenetParam.getSpsc() + "&sa=" + etonenetParam.getSa()
                + "&da=" + da + "&sm=" + sm + "&dc=" + dc;
        return smsUrl;
    }


    /**
     * 短信内容编码
     *
     * @param dataCoding
     * @param realStr
     * @return
     */
    private String encodeHexStr(int dataCoding, String realStr) throws Exception{
        String hexStr = null;
        if (realStr != null) {
            byte[] data = null;
            try {
                if (dataCoding == 15) {
                    data = realStr.getBytes("GBK");
                } else if ((dataCoding & 0x0C) == 0x08) {
                    data = realStr.getBytes("UnicodeBigUnmarked");
                } else {
                    data = realStr.getBytes("ISO8859-1");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                logger.error("短信内容编码error",e);
                throw e;
            }
            if (data != null) {
                int len = data.length;
                char[] out = new char[len << 1];
                for (int i = 0, j = 0; i < len; i++) {
                    out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
                    out[j++] = DIGITS[0x0F & data[i]];
                }
                hexStr = new String(out);
            }
        }
        return hexStr;
    }

    /**
     * 发送http GET请求，并返回http响应字符串
     *
     * @param urlstr 完整的请求url字符串
     * @return
     */
    public static String doGetRequest(String urlstr) throws Exception{
        String res = null;
        try {
            URL url = new URL(urlstr);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");
            httpConn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            System.setProperty("sun.net.client.defaultConnectTimeout", "5000");//jdk1.4换成这个,连接超时
            System.setProperty("sun.net.client.defaultReadTimeout", "10000"); //jdk1.4换成这个,读操作超时
            httpConn.setDoInput(true);
            int rescode = httpConn.getResponseCode();
            if (rescode == 200) {
                BufferedReader bfw = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                res = bfw.readLine();
            } else {
                res = "Http request error code :" + rescode;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("发送http GET请求error",e);
            throw e;
        }
        return res;
    }

    /**
     * 将 短信下行 请求响应字符串解析到一个HashMap中
     * @param resStr
     * @return
     */
    public static HashMap parseResStr(String resStr) throws Exception{
        HashMap pp = new HashMap();
        try {
            String[] ps = resStr.split("&");
            for (int i = 0; i < ps.length; i++) {
                int ix = ps[i].indexOf("=");
                if (ix != -1) {
                    pp.put(ps[i].substring(0, ix), ps[i].substring(ix + 1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("将 短信下行 请求响应字符串解析到一个HashMap中error",e);
            throw e;
        }
        return pp;
    }
    /**
     * 保存发送短信log
     *
     * @param phoneNum 手机号码
     * @param projectName 工程名称
     * @param serviceName 服务名称
     * @param classFunctionName 方法路径
     */
    private void saveMessageLog(String phoneNum,String content,String projectName,String serviceName,String classFunctionName){
        Date nowDate = new Date();
        MessageLog messageLog = new MessageLog();
        messageLog.setPhone(phoneNum);
        messageLog.setContent(content);
        messageLog.setProjectName(projectName);
        messageLog.setServiceName(serviceName);
        messageLog.setClassFunctionName(classFunctionName);
        messageLog.setSendTime(nowDate);
        messageLog.setCreateTime(nowDate);
        messageLog.setUpdateTime(nowDate);
        messageLogRepository.insertOne(messageLog);
    }
}
