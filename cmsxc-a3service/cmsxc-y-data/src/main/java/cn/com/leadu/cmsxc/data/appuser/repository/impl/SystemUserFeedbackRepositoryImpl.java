package cn.com.leadu.cmsxc.data.appuser.repository.impl;

import cn.com.leadu.cmsxc.data.appuser.dao.SystemUserFeedbackDao;
import cn.com.leadu.cmsxc.data.appuser.repository.SystemUserFeedbackRepository;
import cn.com.leadu.cmsxc.data.base.repository.AbstractBaseRepository;
import cn.com.leadu.cmsxc.pojo.appuser.entity.SystemUserFeedback;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by yuanzhenxia on 2018/2/28.
 *
 * 用户意见反馈
 */
@Component
public class SystemUserFeedbackRepositoryImpl extends AbstractBaseRepository<SystemUserFeedbackDao,SystemUserFeedback> implements SystemUserFeedbackRepository {
    /**
     * 登录用户意见反馈表
     * @param systemUserFeedback
     */
    public void insertOne(SystemUserFeedback systemUserFeedback){
        super.insert(systemUserFeedback);
    }

    /**
     * 根据条件批量获取数据
     * @param example
     * @return
     */
    public List<SystemUserFeedback> selectByExampleList(Example example){
        return super.selectListByExample(example);
    }
}
