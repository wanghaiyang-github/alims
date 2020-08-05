package com.bazl.alims.dao;

import com.bazl.alims.model.po.MobileNews;

import java.util.HashMap;
import java.util.List;

public interface MobileNewsMapper {
    int deleteByPrimaryKey(String id);

    int insert(MobileNews record);

    MobileNews selectByPrimaryKey(String id);

    List<MobileNews> selectAll();

    int updateByPrimaryKey(MobileNews record);

    HashMap getUserMobileNewsNumber(String id);

    List<MobileNews> selectPageAll(MobileNews mobileNews);

    int selectPageCount(MobileNews mobileNews);

    int selectCount(MobileNews mobileNews);
}