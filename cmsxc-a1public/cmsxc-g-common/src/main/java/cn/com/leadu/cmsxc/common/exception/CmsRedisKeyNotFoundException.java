package cn.com.leadu.cmsxc.common.exception;

/**
 * @ClassName: CmsRedisKeyNotFoundException
 * @Description: redis未加载到key异常
 * @author qiaohao
 * @date 2017/10/27
 */
public class CmsRedisKeyNotFoundException extends CmsRuntimeException {

	private String redisKey;

	public CmsRedisKeyNotFoundException(String msg) {
		super(msg);
	}

	public CmsRedisKeyNotFoundException(String msg, String rediskey) {
		super(msg);
        this.redisKey = redisKey;
    }

    public String getRedisKey() {
        return redisKey;
    }
}
