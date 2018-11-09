package cn.com.leadu.cmsxc.common.exception;

/**
 * @ClassName: CmsRuntimeException
 * @Description: 运行时异常
 * @author qiaohao
 * @date 2017/10/27
 */
public class CmsRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 5439915454935047937L;

    public CmsRuntimeException(String msg){
        super(msg);
    }

    public CmsRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
