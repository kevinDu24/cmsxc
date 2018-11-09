package cn.com.leadu.cmsxc.appuser.service.impl;

import cn.com.leadu.cmsxc.appuser.service.MessageService;
import cn.com.leadu.cmsxc.appuser.service.SystemUserService;
import cn.com.leadu.cmsxc.common.exception.CmsServiceException;
import cn.com.leadu.cmsxc.common.util.StringUtil;
import cn.com.leadu.cmsxc.data.appbusiness.repository.MessageLogRepository;
import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.pojo.appbusiness.entity.MessageLog;
import cn.com.leadu.cmsxc.pojo.system.entity.SystemUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sun.net.www.protocol.http.HttpURLConnection;
import cn.com.leadu.cmsxc.appuser.config.EtonenetParam;
import cn.com.leadu.cmsxc.common.redis.RedisRepository;

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
    private RedisRepository redisRepository;
    @Autowired
    private MessageLogRepository messageLogRepository;
    @Autowired
    private EtonenetParam etonenetParam;
    @Autowired
    private SystemUserService systemUserService;
    /**
     * Hex编码字符组
     */
    private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    /**
     * 注册时获取验证码，验证码3分钟有效
     *
     * @param userId 手机号码
     * @param projectName 工程名称
     * @param serviceName 服务名称
     * @param classFunctionName 方法路径
     * @return
     */
    public ResponseEntity<RestResponse> sendSingleMt(String userId, String projectName, String serviceName, String classFunctionName) {
        if(StringUtil.isNull(userId)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","手机号为空"),
                    HttpStatus.OK);
        }
        // 验证用户是否存在，如果存在提醒直接登录，不再发送验证码
        SystemUser sysUser = systemUserService.selectSystemUserByUserId(userId);
        if(sysUser != null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该账户已注册，请直接登录"),
                    HttpStatus.OK);
        }
        // 发送验证码，有效时间为3分钟
        return sendCode(userId,180,projectName,serviceName,classFunctionName);
    }

    /**
     * 找回密码时获取验证码，验证码3分钟有效
     *
     * @param userId 手机号码
     * @param projectName 工程名称
     * @param serviceName 服务名称
     * @param classFunctionName 方法路径
     * @return
     */
    public ResponseEntity<RestResponse> sendMt(String userId, String projectName, String serviceName, String classFunctionName) {
        if(StringUtil.isNull(userId)){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","手机号为空"),
                    HttpStatus.OK);
        }
        // 验证用户是否存在，如果不存在，提醒不可找回密码，不再发送验证码
        SystemUser sysUser = systemUserService.selectSystemUserByUserId(userId);
        if(sysUser == null){
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","该账户不存在，请注册登录！"),
                    HttpStatus.OK);
        }
        return sendCode(userId,180,projectName,serviceName,classFunctionName);
    }
    /**
     * 发送短信验证码
     *
     * @param userId 手机号码
     * @param saveTime 验证码在redis中保存时间
     * @param projectName 工程名称
     * @param serviceName 服务名称
     * @param classFunctionName 方法路径
     * @return
     */
    public ResponseEntity<RestResponse> sendCode(String userId, Integer saveTime,String projectName,String serviceName,String classFunctionName){
        //发送http请求，并接收http响应
        try{
            String resStr = doGetRequest(makeSmsUrl(userId, saveTime,projectName,serviceName,classFunctionName));
            // 获取验证码失败
            if(resStr.contains("Http request error code")){
                return new ResponseEntity<RestResponse>(
                        RestResponseGenerator.genResponse(ResponseEnum.FAILURE,"","获取验证码失败"),
                        HttpStatus.OK);
            }
            //解析响应字符串
            parseResStr(resStr);
            return new ResponseEntity<RestResponse>(
                    RestResponseGenerator.genResponse(ResponseEnum.SUCCESS,"",""),
                    HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("短信内容编码error",e);
            throw new CmsServiceException("获取验证码失败");
        }
    }

    /**
     * 发送短信
     *
     * @param userId 手机号码
     * @param plate 车牌号
     * @param projectName 工程名称
     * @param serviceName 服务名称
     * @param classFunctionName 方法路径
     * @return
     */
    public String sendMessage(String userId, String plate,String projectName,String serviceName,String classFunctionName) throws Exception{
        try{
            //发送http请求，并接收http响应
            String resStr = doGetRequest(makeMessageUrl(userId, plate,projectName,serviceName,classFunctionName));
            //解析响应字符串
            parseResStr(resStr);
            return resStr;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("短信内容编码error",e);
            throw e;
        }
    }


    @Override
    public String sendContentMessage(String userId, String messageContent, String projectName, String serviceName, String classFunctionName) throws Exception {
        try{
            // 登录到短信日志表
            saveMessageLog(userId,messageContent,projectName,serviceName,classFunctionName);
            //发送http请求，并接收http响应
            String resStr = doGetRequest(makeMessageContentUrl(userId, messageContent,projectName,serviceName,classFunctionName));
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
     * @param userId 手机号码
     * @param saveTime 验证码在redis中保存时间
     * @param projectName 工程名称
     * @param serviceName 服务名称
     * @param classFunctionName 方法路径
     * @return
     */
    private String makeSmsUrl(String userId, Integer saveTime,String projectName,String serviceName,String classFunctionName) throws Exception{
        //目标号码，必填参数
        String da = "86"+ userId;
        //下行内容以及编码格式，必填参数
        int dc = 15;
        // 生成验证码
        String code = "" + (int)(Math.random()*9000+1000);
        // 短信内容
        String content = "【赏金寻车】亲爱的会员，您的短信验证码为:" + code + "，验证码有效时长为3分钟，请及时输入！";
        String sm = encodeHexStr(dc,content);//下行内容进行Hex编码，此处dc设为15，即使用GBK编码格式
        // 登录到短信日志表
        saveMessageLog(userId,content,projectName,serviceName,classFunctionName);
        //组成url字符串
        String smsUrl = etonenetParam.getMtUrl() + "?command=" + etonenetParam.getCommand() + "&spid=" + etonenetParam.getSpid()
                + "&sppassword=" + etonenetParam.getSppassword() + "&spsc=" + etonenetParam.getSpsc() + "&sa=" + etonenetParam.getSa()
                + "&da=" + da + "&sm=" + sm + "&dc=" + dc;
        try {
            // 将验证码保存到redis中
            redisRepository.save(userId, code, saveTime);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("构造发送短信Urlerror",ex);
            throw ex;
        }
        return smsUrl;
    }

    /**
     * 构造发送短信Url
     *
     * @param userId 手机号码
     * @param plate 车牌号
     * @param projectName 工程名称
     * @param serviceName 服务名称
     * @param classFunctionName 方法路径
     * @return
     */
    private String makeMessageUrl(String userId,String plate, String projectName,String serviceName,String classFunctionName) throws Exception{
        //目标号码，必填参数
        String da = "86"+userId;
        //下行内容以及编码格式，必填参数
        int dc = 15;
        // 短信内容
        String content = "【赏金寻车】您查看过的" + plate + "有新的线索，请及时查看！";
        String sm = encodeHexStr(dc,content);//下行内容进行Hex编码，此处dc设为15，即使用GBK编码格式
        // 登录到短信日志表
        saveMessageLog(userId,content,projectName,serviceName,classFunctionName);
        //组成url字符串
        String smsUrl = etonenetParam.getMtUrl() + "?command=" + etonenetParam.getCommand() + "&spid=" + etonenetParam.getSpid()
                + "&sppassword=" + etonenetParam.getSppassword() + "&spsc=" + etonenetParam.getSpsc() + "&sa=" + etonenetParam.getSa()
                + "&da=" + da + "&sm=" + sm + "&dc=" + dc;
        return smsUrl;
    }

    /**
     * 构造发送短信Url
     *
     * @param userId 手机号码
     * @param messageContent 短信内容
     * @param projectName 工程名称
     * @param serviceName 服务名称
     * @param classFunctionName 方法路径
     * @return
     */
    private String makeMessageContentUrl(String userId, String messageContent, String projectName, String serviceName, String classFunctionName) throws Exception {
        //目标号码，必填参数
        String da = "86"+userId;
        //下行内容以及编码格式，必填参数
        int dc = 15;
        // 短信内容
        String sm = encodeHexStr(dc,messageContent);//下行内容进行Hex编码，此处dc设为15，即使用GBK编码格式
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
     * @param userId 手机号码
     * @param projectName 工程名称
     * @param serviceName 服务名称
     * @param classFunctionName 方法路径
     */
    private void saveMessageLog(String userId,String content,String projectName,String serviceName,String classFunctionName){
        Date nowDate = new Date();
        MessageLog messageLog = new MessageLog();
        messageLog.setPhone(userId);
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
