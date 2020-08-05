package com.bazl.alims.dao;


import com.bazl.alims.model.DictItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DictItemMapper {


    List<DictItem> selectListByDictCode(String dictCode);

    /**
     * 查询所有字典项内容
     * @return
     */
    List<DictItem> selectAllCode();

    /**
     * 根据dictTypeCode查询出对应字典项内容
     * @param dictTypeCode
     * @return
     */
    List<DictItem> selectListByDictTypeCode(String dictTypeCode);
}