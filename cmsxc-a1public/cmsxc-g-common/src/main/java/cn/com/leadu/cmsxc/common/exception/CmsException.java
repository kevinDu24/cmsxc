package cn.com.leadu.cmsxc.common.exception;

/**
 * @author qiaomengnan
 * @ClassName: CmsException
 * @Description: 异常基类
 * @date 2018/1/7
 */
public class CmsException extends Exception {

    private static final long serialVersionUID = 5439915454935047937L;

    public CmsException(String msg){
        super(msg);
    }

    public CmsException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
