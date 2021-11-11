package com.yan.mybatis.dao;


import com.yan.mybatis.po.AppraisalAddress;

public interface AppraisalAddressMapper {

    AppraisalAddress selectById(Integer id);

    AppraisalAddress queryUserList(String telephone);

}