package com.yan.mybatis.spring;

import com.yan.mybatis.Resources;
import com.yan.mybatis.SqlSessionFactory;
import com.yan.mybatis.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.io.Reader;

/**
 * @author hairui
 * @date 2021/11/11
 * @des
 */
public class SqlSessionFactoryBean implements FactoryBean<SqlSessionFactory>, InitializingBean {

    private String resource;
    private SqlSessionFactory sqlSessionFactory;


    @Override
    public SqlSessionFactory getObject() throws Exception {
        return sqlSessionFactory;
    }

    @Override
    public Class<?> getObjectType() {
        return sqlSessionFactory.getClass();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try (Reader reader = Resources.getResourceAsReader(resource)) {
            this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

}
