package com.bazl.alims.dao;


import com.bazl.alims.model.po.PersonDetail;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonDetailMapper {

    void updatePersonDetail1(PersonDetail personDetail);

    void deleteByDetailId(String personDetailId);

    PersonDetail selectByDetailId(String personDetailId);
}