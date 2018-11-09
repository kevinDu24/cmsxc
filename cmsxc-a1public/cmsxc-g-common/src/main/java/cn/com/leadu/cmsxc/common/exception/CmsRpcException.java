package cn.com.leadu.cmsxc.common.exception;

import org.springframework.http.HttpStatus;

/**
 * @author qiaomengnan
 * @ClassName: CmsRpcException
 * @Description: Rpc微服务调用异常
 * @date 2018/1/7
 */

public class CmsRpcException extends CmsException {

	private HttpStatus httpStatus;

	/**
	 * @Fields 自定义的响应码类型 :
	 */
	private String responseCode;

	public CmsRpcException(String msg) {
		super(msg);
	}

	public CmsRpcException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public CmsRpcException(String msg, String responseCode) {
		this(HttpStatus.OK,msg);
		this.responseCode = responseCode;
	}

	public CmsRpcException(String msg, String responseCode, Throwable cause) {
		super(msg, cause);
		this.responseCode = responseCode;
	}

	public CmsRpcException(HttpStatus httpStatus, String msg) {
		super(msg);
		this.httpStatus = httpStatus;
	}

	public CmsRpcException(HttpStatus httpStatus, String msg, Throwable cause) {
		super(msg, cause);
		this.httpStatus = httpStatus;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public String getResponseCode() {
		return responseCode;
	}
}
