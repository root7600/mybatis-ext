package com.yan.mybatis;

/**
 * @author hairui
 * @date 2021/11/11
 * @des
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory{

    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration.connection, configuration.mapperElement);
    }
}
