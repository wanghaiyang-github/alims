package com.bazl.alims.controller.delegate;

import com.bazl.alims.common.Constants;
import com.bazl.alims.controller.BaseController;
import com.bazl.alims.model.DictItem;
import com.bazl.alims.model.LoaUserInfo;
import com.bazl.alims.model.PageInfo;
import com.bazl.alims.model.po.FugitivesInfo;
import com.bazl.alims.model.vo.FugitivesInfoVo;
import com.bazl.alims.service.FugitivesInfoService;
import com.bazl.alims.utils.DictUtil;
import com.bazl.alims.utils.DownloadFileUtils;
import com.bazl.alims.utils.ListUtils;
import com.bazl.alims.utils.LocalBeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author huawei
 * @date 2020/6/15.
 */
@Controller
@RequestMapping("/delegate")
public class FugitivesController extends BaseController {

    @Autowired
    FugitivesInfoService fugitivesInfoService;
    @Autowired
    DownloadFileUtils downloadFileUtils;

    /**
     * 在逃人员列表
     * @param request
     * @param query
     * @param pageInfo
     * @return
     */
    @RequestMapping("/fugitivesRegManage")
    public ModelAndView pending(HttpServletRequest request, FugitivesInfoVo query, PageInfo pageInfo){
        ModelAndView modelAndView = new ModelAndView();

        //人员类型
        DictItem dictItem = new DictItem();
        dictItem.setDictTypeCode(Constants.PERSON_TYPE);
        List<DictItem> personTypeList = DictUtil.getDictList(dictItem);

        //初始化条件
        query = resetQueryParams(query);

        List<FugitivesInfoVo> fugitivesInfoVoList = fugitivesInfoService.selectVOList(query, pageInfo);
        int totalCnt = fugitivesInfoService.selectVOCnt(query);

        modelAndView.addObject("query", query);
        modelAndView.addObject("personTypeList", personTypeList);
        modelAndView.addObject("fugitivesInfoVoList", fugitivesInfoVoList);
        modelAndView.addObject("pageInfo", pageInfo.updatePageInfo(totalCnt));
        modelAndView.setViewName("delegationReg/fugitivesRegManage");
        return modelAndView;
    }

    /**
     * 导入在逃人员
     * @param request
     * @param file
     * @return
     */
    @RequestMapping(value="/uploadFugitives",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> uploadFugitives(HttpServletRequest request, @RequestParam(value = "upFile") MultipartFile[] file){
        Map<String,Object> result = new HashMap<>();
        try {
            if (file.length > 0) {
                List<FugitivesInfo> allList = new ArrayList<>();
                for (int i = 0; i < file.length; i++) {
                    List<FugitivesInfo> fugitivesInfoList = downloadFileUtils.importFugitivesInfo(request, file[i]);
                    allList.addAll(fugitivesInfoList);
                }
                //遍历解析出来的list
                if (ListUtils.isNotNullAndEmptyList(allList)) {
                    List<FugitivesInfo> newFugitivesInfoList = new ArrayList<>();
                    List<FugitivesInfo> repeatFugitivesInfoList = new ArrayList<>();
                    //筛选出新导入和已经存在的在逃人员信息
                    for (FugitivesInfo fi : allList) {
                        List<FugitivesInfo> fugitivesInfos = fugitivesInfoService.selectInfoByPersonNameAndCard(fi.getPersonName(), fi.getPersonCard());
                        //如果为空，表示之前没有插入这条记录
                        if (ListUtils.isNullOrEmptyList(fugitivesInfos)) {
                            newFugitivesInfoList.add(fi);
                        }else {
                            //不为空，表示此人已经存在
                            repeatFugitivesInfoList.add(fi);
                        }
                    }
                    //批量插入在逃人员信息
                    if (ListUtils.isNotNullAndEmptyList(newFugitivesInfoList)) {
                        fugitivesInfoService.insertBatchFugitives(newFugitivesInfoList);
                    }
                    //重复人员在前台展示
                    if (ListUtils.isNotNullAndEmptyList(repeatFugitivesInfoList)) {
                        result.put("repeatFugitivesInfoList", repeatFugitivesInfoList);
                    }else {
                        result.put("repeatFugitivesInfoList", null);
                    }
                    result.put("success",true);
                }else {
                    result.put("success", "no");
                }
            }else {
                result.put("success",false);
            }
        }catch (Exception e){
            result.put("success",false);
            logger.error("uploadSampleTable error",e);
        }

        return result;
    }

    /**
     * 插入数据
     * @param fugitivesInfo
     */
    public void insertData (FugitivesInfo fugitivesInfo) throws Exception {
        if (fugitivesInfo != null) {
            fugitivesInfo.setId(UUID.randomUUID().toString());
            fugitivesInfo.setCreateDatetime(new Date());

            fugitivesInfoService.insert(fugitivesInfo);
        }
    }

    /**
     * 增加或修改在逃人员信息
     * @param request
     * @param fugitivesInfo
     * @return
     */
    @RequestMapping(value = "operateFugitives", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map<String, Object> operateFugitives(HttpServletRequest request, @RequestBody FugitivesInfo fugitivesInfo) {
        Map<String, Object> result = new HashMap<>();
        try {
            //获取当前登录人信息
            Subject subject = SecurityUtils.getSubject();
            LoaUserInfo operateUser = (LoaUserInfo) subject.getPrincipal();
            if(operateUser == null){
                result.put("success",false);
                result.put("","/login.html?timeoutFlag=1");
                return  result;
            }
            //判断是否已经存在此信息，不存在添加在逃人员信息，否则修改在逃人员信息
            String repeat = null;
            if (StringUtils.isBlank(fugitivesInfo.getId())) {
                fugitivesInfo.setId(UUID.randomUUID().toString());
                fugitivesInfo.setCreateDatetime(new Date());
                fugitivesInfo.setCreatePerson(operateUser.getLoginName());

                //判断此在逃人员是否已经存在
                List<FugitivesInfo> fugitivesInfoList = fugitivesInfoService.selectInfoByPersonNameAndCard(fugitivesInfo.getPersonName(), fugitivesInfo.getPersonCard());
                if (ListUtils.isNullOrEmptyList(fugitivesInfoList)) {
                    //插入在逃人员信息
                    fugitivesInfoService.insert(fugitivesInfo);
                }else {
                    repeat = "repeat";
                }
            }else {
                FugitivesInfo fugitives = fugitivesInfoService.selectByPrimaryKey(fugitivesInfo.getId());
                fugitives.setPersonName(fugitivesInfo.getPersonName());
                fugitives.setPersonType(fugitivesInfo.getPersonType());
                fugitives.setPersonGender(fugitivesInfo.getPersonGender());
                fugitives.setPersonCard(fugitivesInfo.getPersonCard());
                fugitives.setPersonAge(fugitivesInfo.getPersonAge());
                fugitives.setFugitiveNo(fugitivesInfo.getFugitiveNo());
                fugitives.setUpdateDatetime(new Date());
                fugitives.setUpdatePerson(operateUser.getLoginName());

                //更新在逃人员信息
                fugitivesInfoService.updateByPrimaryKey(fugitives);
            }
            result.put("repeat", repeat);
            result.put("success", true);
        } catch (Exception e) {
            e.getStackTrace();
            logger.error("添加失败!", e);
            result.put("success", false);
        }
        return result;
    }

    /**
     * 删除在逃人员信息
     * @param request
     * @param id
     * @return
     */
    @RequestMapping("/delFugitivesInfo")
    @ResponseBody
    public Map<String, Object> delFugitivesInfo(HttpServletRequest request,String id) {
        Map<String, Object> result = new HashMap<>();

        try {
            //获取当前登录人信息
            Subject subject = SecurityUtils.getSubject();
            LoaUserInfo operateUser = (LoaUserInfo) subject.getPrincipal();
            if(operateUser == null){
                result.put("success",false);
                result.put("","/login.html?timeoutFlag=1");
                return  result;
            }

            FugitivesInfo fugitivesInfo = new FugitivesInfo();
            fugitivesInfo.setId(id);
            fugitivesInfo.setDeleteFlag(Constants.DELETE_FLAG_1);
            fugitivesInfo.setDeleteDatetime(new Date());
            fugitivesInfo.setDeletePerson(operateUser.getLoginName());

            fugitivesInfoService.deleteFugitivesInfo(fugitivesInfo);
            result.put("success", true);
        }catch (Exception e) {
            e.getStackTrace();
            logger.error("删除失败！", e);
            result.put("success", false);
        }

        return result;
    }

    @RequestMapping("/fugitivesSearch")
    @ResponseBody
    public Map<String, Object> fugitivesSearch(HttpServletRequest request, String searchFugitives) {
        Map<String, Object> result = new HashMap<>();

        try {
            List<FugitivesInfoVo> fugitivesInfoVoList = fugitivesInfoService.selectFugitivesList(searchFugitives.trim());
            result.put("fugitivesInfoVoList", fugitivesInfoVoList);
            result.put("success", true);
        }catch (Exception e) {
            e.getStackTrace();
            logger.error("查询失败！", e);
            result.put("success", false);
        }

        return result;
    }

    /**
     * 初始化查询条件
     * @param query
     * @return
     */
    private FugitivesInfoVo resetQueryParams(FugitivesInfoVo query){
        if (StringUtils.isBlank(query.getEntity().getDeleteFlag())) {
            query.getEntity().setDeleteFlag(Constants.DELETE_FLAG_0);
        } else {
            query.getEntity().setDeleteFlag(query.getEntity().getDeleteFlag().trim());
        }

        if (StringUtils.isBlank(query.getEntity().getPersonName())) {
            query.getEntity().setPersonName(null);
        } else {
            query.getEntity().setPersonName(query.getEntity().getPersonName().trim());
        }

        if (StringUtils.isBlank(query.getEntity().getPersonCard())) {
            query.getEntity().setPersonCard(null);
        } else {
            query.getEntity().setPersonCard(query.getEntity().getPersonCard().trim());
        }

        if (StringUtils.isBlank(query.getEntity().getFugitiveNo())) {
            query.getEntity().setFugitiveNo(null);
        } else {
            query.getEntity().setFugitiveNo(query.getEntity().getFugitiveNo().trim());
        }

        return query;
    }
}
