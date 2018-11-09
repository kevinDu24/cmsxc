package cn.com.leadu.cmsxc.common.util.push;

import cn.com.leadu.cmsxc.common.constant.Constants;
import cn.com.leadu.cmsxc.common.constant.enums.ClientTypeEnums;
import cn.com.leadu.cmsxc.common.constant.enums.PushCastEnum;
import cn.com.leadu.cmsxc.common.util.push.android.AndroidListcast;
import cn.com.leadu.cmsxc.common.util.push.android.AndroidUnicast;
import cn.com.leadu.cmsxc.common.util.push.ios.IOSListcast;
import cn.com.leadu.cmsxc.common.util.push.ios.IOSUnicast;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by qiaohao on 2017/2/21.
 */
public class UmengPushUtils {


    private String appkey = null;
    private String appMasterSecret = null;
    private PushClient client = new PushClient();

    private final String iosAlert = "{\"title\":\"标题\",\"body\":\"内容\"}";

    public UmengPushUtils(String key, String secret) {
        try {
            appkey = key;
            appMasterSecret = secret;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * 单播消息推送——安卓
     *
     * @param deviceToken  推送设备
     * @param messageTitle 消息标题
     * @param content 消息内容
     * @throws Exception
     */
    public int sendAndroidUnicast(String deviceToken, String messageTitle, String content) throws Exception {
        AndroidUnicast unicast = new AndroidUnicast(appkey,appMasterSecret);
        unicast.setDeviceToken(deviceToken); //设定token
        unicast.setTicker( "新消息"); //通知栏提示文字，暂未用到
        unicast.setTitle(messageTitle); //通知栏标题
        unicast.setText(content); //通知消息内容
        unicast.goAppAfterOpen(); //读取消息默认打开app
        unicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION); //消息类型:通知
        unicast.setProductionMode(); //推送环境:生产环境
        return client.send(unicast);
    }

    /**
     *  列播消息推送——安卓
     *
     * @param deviceTokens  deviceToken集合
     * @param messageTitle 消息标题
     * @param content 消息内容
     * @throws Exception
     */
    public int sendAndroidListcast(List<String> deviceTokens, String messageTitle, String content) throws Exception {
        AndroidListcast listcast = new AndroidListcast(appkey,appMasterSecret);
        StringBuilder deviceTokenStr = new StringBuilder();
        //遍历deviceToken集合，拼接成字符串，以","分割
        for(String deviceToken : deviceTokens){
            if(deviceTokenStr.length() != 0){
                deviceTokenStr.append(Constants.COMMA);
            }
            deviceTokenStr.append(deviceToken);
        }
        listcast.setDeviceToken(deviceTokenStr.toString());
        listcast.setTicker( "新消息"); //通知栏提示文字，暂未用到
        listcast.setTitle(messageTitle); //通知栏标题
        listcast.setText(content); //通知消息内容
        listcast.goAppAfterOpen(); //读取消息默认打开app
        listcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION); //消息类型:通知
        listcast.setProductionMode(); //推送环境:生产环境
        return client.send(listcast);
    }

    /**
     * 单播消息推送——IOS
     *
     * @param deviceToken  推送设备
     * @param messageTitle 消息标题
     * @param content 消息内容
     * @throws Exception
     */
    public int sendIOSUnicast(String deviceToken, String messageTitle, String content) throws Exception {
        IOSUnicast unicast = new IOSUnicast(appkey,appMasterSecret);
        unicast.setDeviceToken(deviceToken); //设定token
        //设定标题和内容
        String message = iosAlert.replace("标题",messageTitle).replace("内容",content);
        JSONObject object = new JSONObject(message);
        unicast.setAlert(object);
        unicast.setBadge( 0); //默认值
        unicast.setSound( "default"); //默认声音
        unicast.setProductionMode(); //推送环境:生产环境
        return client.send(unicast);
    }

    /**
     * 列播消息推送——IOS
     *
     * @param deviceTokens  deviceToken集合
     * @param messageTitle 消息标题
     * @param content 消息内容
     * @throws Exception
     */
    public int sendIOSListcast(List<String> deviceTokens, String messageTitle, String content) throws Exception {
        IOSListcast Listcast = new IOSListcast(appkey,appMasterSecret);
        StringBuilder deviceTokenStr = new StringBuilder();
        //遍历deviceToken集合，拼接成字符串，以","分割
        for(String deviceToken : deviceTokens){
            if(deviceTokenStr.length() != 0){
                deviceTokenStr.append(Constants.COMMA);
            }
            deviceTokenStr.append(deviceToken);
        }
        Listcast.setDeviceToken(deviceTokenStr.toString());
        //设定标题和内容
        String message = iosAlert.replace("标题",messageTitle).replace("内容",content);
        JSONObject object = new JSONObject(message);
        Listcast.setAlert(object);
        Listcast.setBadge( 0); //默认值
        Listcast.setSound( "default"); //默认声音
        Listcast.setProductionMode(); //推送环境:生产环境
        return client.send(Listcast);
    }

    /**
     * 推送共通方法
     * @param messageType "1":单播，"0":列播
     * @param clientType "1":ios,"0":安卓
     * @param deviceToken messageType为"1"时，不为空
     * @param deviceTokens messageType为"0"时，不为空
     * @param messageTitle 推送标题
     * @param content 推送内容
     */
    public static int push(String messageType, String clientType, String deviceToken,
                            List<String> deviceTokens, String messageTitle, String content)  throws Exception {
        UmengPushUtils umengPushUtils;
        //推送给ios设备
        if(ClientTypeEnums.IOS.getCode().equals(clientType)){
            umengPushUtils = new UmengPushUtils("5a97d3a1a40fa350d4000146", "pex2h4e4x9zz4ngdrstnzckurm76itze");
            //单播
            if(PushCastEnum.UNICAST.getCode().equals(messageType)){
                return umengPushUtils.sendIOSUnicast(deviceToken,messageTitle,content);
            }else if(PushCastEnum.LISTCAST.getCode().equals(messageType)){ //列播
                return umengPushUtils.sendIOSListcast(deviceTokens,messageTitle,content);
            }
        } else { //推送给安卓设备
            umengPushUtils = new UmengPushUtils("5a97d933f29d980e3f0001be", "lghll7dtfhblpltpi6chovkg7lemdme2");
            //单播
            if(PushCastEnum.UNICAST.getCode().equals(messageType)){
                return umengPushUtils.sendAndroidUnicast(deviceToken,messageTitle,content);
            }else if(PushCastEnum.LISTCAST.getCode().equals(messageType)){ //列播
                return umengPushUtils.sendAndroidListcast(deviceTokens,messageTitle,content);
            }
        }
        return 0;
    }
}
