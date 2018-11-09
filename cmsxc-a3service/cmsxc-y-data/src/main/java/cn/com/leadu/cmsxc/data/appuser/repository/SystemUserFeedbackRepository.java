package cn.com.leadu.cmsxc.data.appuser.repository;

import cn.com.leadu.cmsxc.pojo.appuser.entity.SystemUserFeedback;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/2/28.
 *
 * 用户意见反馈
 */
public interface SystemUserFeedbackRepository {
    /**
     * 登录意见反馈表
     * @param systemUserFeedBack
     */
    void insertOne(SystemUserFeedback systemUserFeedBack);
    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
    List<SystemUserFeedback> selectByExampleList(Example example);
}
