package cn.com.leadu.cmsxc.webclient.system.controller;
import cn.com.leadu.cmsxc.pojo.system.vo.ClueListParamVo;
import cn.com.leadu.cmsxc.pojo.system.vo.ClueListVo;
import cn.com.leadu.cmsxc.pojo.system.vo.SysUserSearchListVo;
import cn.com.leadu.cmsxc.pojo.system.vo.SysUserSearchVo;
import cn.com.leadu.cmsxc.webclient.system.rpc.ClueInfoRpc;
import cn.com.leadu.cmsxc.webclient.system.rpc.SysUserRpc;
import cn.com.leadu.cmsxc.webclient.system.service.ExcelService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 导出报表入口
 * Created by qiaohao on 2017/3/8.
 */
@RestController
@RequestMapping(value = "excel")
public class ExportExcelController {
    private static final Logger logger = LoggerFactory.getLogger(ExportExcelController.class);

    @Autowired
    private ExcelService excelService;
    @Autowired
    private SysUserRpc sysUserRpc;
    @Autowired
    private ClueInfoRpc clueInfoRpc;



    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(simpleDateFormat, true));
    }

    /**
     * 导出账号注册列表
     *
     * @param sysUserSearchVo 画面信息
     * @param response
     */
    @RequestMapping(value = "exportSysUserExcel", method = RequestMethod.GET)
    public void sysUserExcel(SysUserSearchVo sysUserSearchVo, HttpServletResponse response) {
        logger.info("请求返回账号注册列表导出start");
        Map sysUserVoMap = sysUserSearchVo == null?null:(Map) JSON.toJSON(sysUserSearchVo);
        List<SysUserSearchListVo> sysUserSearchList = sysUserRpc.findSysUserList(sysUserVoMap);
        try {
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.addHeader("Content-Disposition", "attachment; filename=" + new String(new String("账号注册列表".getBytes("gb2312"), "iso8859-1") + ".xlsx"));
            excelService.exportList("账号注册列表", sysUserSearchList, SysUserSearchListVo.class, response.getOutputStream());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("请求返回账号注册列表导出end");
    }
    /**
     * 导出线索扫描列表
     *
     * @param clueListParamVo 画面信息
     * @param response
     */
    @RequestMapping(value = "exportClueInfoExcel", method = RequestMethod.GET)
    public void clueInfoExcel(ClueListParamVo clueListParamVo, HttpServletResponse response) {
        logger.info("请求返回线索扫描列表导出start");
        Map clueListParamVoMap = clueListParamVo == null?null:(Map) JSON.toJSON(clueListParamVo);
        List<ClueListVo> clueList = clueInfoRpc.findClueInfoList(clueListParamVoMap);
        try {
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.addHeader("Content-Disposition", "attachment; filename=" + new String(new String("线索扫描列表".getBytes("gb2312"), "iso8859-1") + ".xlsx"));
            excelService.exportList("线索扫描列表", clueList, ClueListVo.class, response.getOutputStream());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("请求返回线索扫描列表导出end");
    }

}

