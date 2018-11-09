package cn.com.leadu.cmsxc.extend.util;

import cn.com.leadu.cmsxc.extend.response.ResponseEnum;
import cn.com.leadu.cmsxc.extend.response.ResponseFailEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.common.exception.CmsServiceException;
import cn.com.leadu.cmsxc.common.exception.CmsRpcException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Created by qiaohao on 2017/10/23.
 */
public class ResponseEntityUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ResponseEntityUtil.class);


	/**
	 * @Title: getRestResponseData
	 * @Description:调用微服务获取返回对象
	 * @param responseEntity
	 * @return T
	 * @throws CmsRpcException
	 *             微服务调用异常
	 *
	 * @author qiaohao
	 * @date 2017/11/13 10:27:14
	 */
	public static <T> T getRestResponseData(ResponseEntity<RestResponse<T>> responseEntity) throws CmsRpcException {
		if (responseEntity == null) {
			throw new CmsRpcException("RPC调用异常,未获取响应对象");
		}
		if (responseEntity.getStatusCode() != HttpStatus.OK) {
			throw new CmsRpcException(responseEntity.getStatusCode(), "RPC调用HTTP状态码异常");
		}
		if (!StringUtils.equals(responseEntity.getBody().getCode(), ResponseEnum.SUCCESS.getCode())) {
			String errorMsg = responseEntity.getBody().getMessage();
			if(StringUtils.equals(responseEntity.getBody().getCode(), ResponseFailEnum.BIZ_CHECK_ERROR.getCode())){
				throw new CmsServiceException(errorMsg);
			}
			if (StringUtils.isNotBlank(errorMsg)) {
				LOGGER.error("the rpc error is {}", errorMsg);
				throw new CmsRpcException(errorMsg, responseEntity.getBody().getCode());
			}
			throw new CmsRpcException("RPC调用状态异常", responseEntity.getBody().getCode());
		}
		return responseEntity.getBody().getData();
	}

}
