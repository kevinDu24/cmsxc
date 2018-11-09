package cn.com.leadu.cmsxc.webclient.system.controller;

import cn.com.leadu.cmsxc.common.exception.CmsRpcException;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.pojo.system.vo.ClueListParamVo;
import cn.com.leadu.cmsxc.webclient.system.rpc.ClueInfoRpc;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author qiaomengnan
 * @ClassName: ClueInfoController
 * @Description: 线索扫描信息导出
 * @date 2018/1/9
 */
@RestController
@RequestMapping("clueinfo")
public class ClueInfoController {
    @Autowired
    private ClueInfoRpc clueInfoRpc;
    /**
     * @Title:
     * @Description: 线索扫描信息列表
     * @param clueListParamVo
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/01/09 02:21:22
     */
    @RequestMapping(value="findClueInfoListByPage",method = RequestMethod.GET)
    public ResponseEntity<RestResponse<String>> saveSysUser(ClueListParamVo clueListParamVo) {
        Map clueListParamVoMap = clueListParamVo == null?null:(Map) JSON.toJSON(clueListParamVo);
        return clueInfoRpc.findClueInfoListByPage(clueListParamVoMap);
    }
}
