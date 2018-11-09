package cn.com.leadu.cmsxc.data.base.repository;

import cn.com.leadu.cmsxc.common.entity.BaseEntity;
import cn.com.leadu.cmsxc.data.base.config.BaseDao;
import cn.com.leadu.cmsxc.common.entity.PageQuery;
import cn.com.leadu.cmsxc.common.entity.Entity;
import cn.com.leadu.cmsxc.common.util.ArrayUtil;
import cn.com.leadu.cmsxc.data.base.pojo.PageInfoExtend;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

//import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @ClassName: AbstractBaseRepository
 * @Description: 抽象仓库访问基类
 * @author qiaohao
 * @date 2017/11/2
 */
@Data
public abstract class AbstractBaseRepository<T extends BaseDao, K extends Entity> {

//	@Autowired
//	protected RedisRepository redisRepository;

//	@Autowired
//	protected RabbitTemplate rabbitTemplate;

	@Autowired
	protected T baseDao;

	/**
	 * @Fields : 分页默认显示条数
	 */
	@Value("${page.pageSize}")
	private int pageSize;

	protected K insert(K entity) {
		if(entity instanceof BaseEntity){
			BaseEntity baseEntity = (BaseEntity)entity;
			if(baseEntity.getCreateTime() == null){
				baseEntity.setCreateTime(new Date());
			}
			if(baseEntity.getUpdateTime() == null){
				baseEntity.setUpdateTime(baseEntity.getCreateTime());
			}
		}

		baseDao.insert(entity);
//		if (result != 1) {
//			throw new FBDataBaseOptException(DataBaseOptType.ADD, entity.getId(), "数据更新失败");
//		}
		return entity;
	}

	protected List<K> insertListByMapper(List<K> entityList){
		for(K entity : entityList){
			insert(entity);
		}
		return entityList;
	}

	protected K insertSelective(K entity) {
		baseDao.insertSelective(entity);
		return entity;
	}

//	protected List<K> insertListByMapper(List<K> entityList) {
//		int result = baseDao.insertList(entityList);
//		if (result < 1) {
//			throw new FBDataBaseOptException(DataBaseOptType.ADD, "集合数据更新失败");
//		}
//		return entityList;
//	}

	protected List<K> updateListByMapper(List<K> entityList){
		for(K entity : entityList){
			updateByPrimaryKey(entity);
		}
		return entityList;
	}

	protected void updateByPrimaryKey(K entity) {
		baseDao.updateByPrimaryKey(entity);
	}

	protected void updateByPrimaryKeySelective(K entity) {
		baseDao.updateByPrimaryKeySelective(entity);

	}

	protected void updateByExample(K entity, Example example) {
		baseDao.updateByExample(entity, example);
	}

	protected void updateByExampleSelective(K entity, Example example) {
		baseDao.updateByExampleSelective(entity, example);
	}

	protected void delete(Object id) {
		baseDao.deleteByPrimaryKey(id);
	}

	protected List<K> selectAll() {
		return baseDao.selectAll();
	}

	protected K selectOneByExample(Example example) {
		List results = baseDao.selectByExample(example);
		if (ArrayUtil.isNotNullAndLengthNotZero(results))
			return (K) results.get(0);
		return null;
	}

	protected K selectByPrimaryKey(Object id){
		return (K)baseDao.selectByPrimaryKey(id);
	}

	protected List<K> selectListByExample(Example example) {
		return baseDao.selectByExample(example);
	}

	protected K selectByPrimaryKey(String id) {
		return (K) baseDao.selectByPrimaryKey(id);
	}



	/**
	 * @Title: setPageQuery
	 * @Description: 设置分页
	 * @param pageQuery
	 * @return void
	 * @throws
	 *
	 * 			@author
	 *             qiaohao
	 * @date 2017/11/03 08:46:26
	 */
	protected void setPageQuery(PageQuery pageQuery) {
		int size = pageSize;
		if (pageQuery.getPageSize() != null && pageQuery.getPageSize()>0) {
			size = pageQuery.getPageSize();
		}
		PageHelper.startPage(pageQuery.getCurrenPage(), size);
	}

	/**
	 * @Title:
	 * @Description: 通用分页
	 * @param example,pageQuery
	 * @return
	 * @throws
	 * @author qiaomengnan
	 * @date 2018/01/09 04:49:17
	 */
	protected PageInfo<K> selectListByExamplePage(Example example,PageQuery pageQuery){
		PageInfo<K> pageInfo = PageHelper.startPage(pageQuery.getCurrenPage(),pageQuery.getPageSize()==null?pageSize:pageQuery.getPageSize())
				.doSelectPageInfo(new ISelect() {
					@Override
					public void doSelect() {
						baseDao.selectByExample(example);
					}
				});
		return pageInfo;
	}

	/**
	 * @Title:
	 * @Description: 通用分页
	 * @param example,pageQuery
	 * @return
	 * @throws
	 * @author qiaomengnan
	 * @date 2018/01/09 04:49:17
	 */
	protected PageInfoExtend<K> selectListByExamplePageInfo(Example example,PageQuery pageQuery){
		PageInfo<K> pageInfo = PageHelper.startPage(pageQuery.getCurrenPage(),pageQuery.getPageSize()==null?pageSize:pageQuery.getPageSize())
				.doSelectPageInfo(new ISelect() {
					@Override
					public void doSelect() {
						baseDao.selectByExample(example);
					}
				});
		PageInfoExtend<K> pageInfoExtend = new PageInfoExtend<K>();
		pageInfoExtend.setDraw(pageQuery.getDraw());
		pageInfoExtend.setData(pageInfo.getList());
		pageInfoExtend.setRecordsTotal(pageInfo.getTotal());
		pageInfoExtend.setRecordsFiltered(pageInfo.getTotal());
		return pageInfoExtend;
	}




}
