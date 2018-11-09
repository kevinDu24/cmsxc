
package cn.com.leadu.cmsxc.common.exception;

/**
 * @author qiaomengnan
 * @ClassName: CmsServiceException
 * @Description: 逻辑层异常
 * @date 2018/1/7
 */
public class CmsServiceException extends CmsRuntimeException {

    private static final long serialVersionUID = 5439915454935047937L;

    public CmsServiceException(String msg){
        super(msg);
    }

    public CmsServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
