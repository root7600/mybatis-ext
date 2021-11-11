package com.yan.mybatis;

import com.alibaba.fastjson.JSON;
import com.yan.mybatis.po.AppraisalAddress;
import org.junit.Test;

import java.io.Reader;
import java.util.List;

/**
 * @author hairui
 * @date 2021/11/11
 * @des
 */
public class ApiTest {

    @Test
    public void test_queryUserInfoById() {
        String resource = "mybatis-config-datasource.xml";
        Reader reader;
        try {
            reader = Resources.getResourceAsReader(resource);
            SqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(reader);

            SqlSession session = sqlMapper.openSession();
            try {
                AppraisalAddress appraisalAddress = session.selectOne("com.yan.mybatis.dao.AppraisalAddressMapper.selectById", 1L);
                System.out.println(JSON.toJSONString(appraisalAddress));
            } finally {
                session.close();
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test_queryUserList() {
        String resource = "mybatis-config-datasource.xml";
        Reader reader;
        try {
            reader = Resources.getResourceAsReader(resource);
            SqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(reader);

            SqlSession session = sqlMapper.openSession();
            try {
                AppraisalAddress req = new AppraisalAddress();
                req.setTelephone("15869147600");
                List<AppraisalAddress> userList = session.selectList("com.yan.mybatis.dao.AppraisalAddressMapper.queryUserList", req);
                System.out.println(JSON.toJSONString(userList));
            } finally {
                session.close();
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
