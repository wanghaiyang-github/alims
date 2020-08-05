package com.bazl.alims.service.impl;

import com.bazl.alims.dao.DictItemMapper;
import com.bazl.alims.model.DictItem;
import com.bazl.alims.service.DictItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;


/**
 * Created by hj on 2018/12/20.
 */
@Service
public class DictItemServiceImpl implements DictItemService {

    @Autowired
    DictItemMapper dictItemMapper;

    @Override
    public List<DictItem> selectListByDictCode(String dictCode) {

        return dictItemMapper.selectListByDictCode(dictCode);
    }

    @Override
    public List<DictItem> selectAllCode() {
        List<DictItem> listDictItem = dictItemMapper.selectAllCode();
        return listDictItem;
    }

    /**
     * 根据dictTypeCode查询出对应字典项内容
     * @param dictTypeCode
     * @return
     */
    @Override
    public List<DictItem> selectListByDictTypeCode(String dictTypeCode) {
        return dictItemMapper.selectListByDictTypeCode(dictTypeCode);
    }
}
