package com.yan.mybatis.autoconfigure;

import com.yan.mybatis.SqlSessionFactory;
import com.yan.mybatis.SqlSessionFactoryBuilder;
import com.yan.mybatis.spring.MapperFactoryBean;
import com.yan.mybatis.spring.MapperScannerConfigurer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author hairui
 * @date 2021/11/11
 * @des
 */
@Configuration
@ConditionalOnClass({SqlSessionFactory.class})
@EnableConfigurationProperties(MybatisProperties.class)
public class MybatisAutoConfiguration implements InitializingBean {


    @Bean
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory(Connection connection, MybatisProperties mybatisProperties) throws Exception {
        return new SqlSessionFactoryBuilder().build(connection, mybatisProperties.getMapperLocations());
    }

    @Bean
    @ConditionalOnMissingBean
    public Connection connection(MybatisProperties mybatisProperties) {
        try {
            Class.forName(mybatisProperties.getDriver());
            return DriverManager.getConnection(mybatisProperties.getUrl(), mybatisProperties.getUsername(), mybatisProperties.getPassword());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class AutoConfiguredMapperScannerRegistrar implements EnvironmentAware, ImportBeanDefinitionRegistrar {

        private String basePackage;

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(MapperScannerConfigurer.class);
            builder.addPropertyValue("basePackage", basePackage);
            registry.registerBeanDefinition(MapperScannerConfigurer.class.getName(), builder.getBeanDefinition());
        }

        @Override
        public void setEnvironment(Environment environment) {
            this.basePackage = environment.getProperty("mybatis.datasource.base-dao-package");
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Configuration
    @Import(AutoConfiguredMapperScannerRegistrar.class)
    @ConditionalOnMissingBean({MapperFactoryBean.class, MapperScannerConfigurer.class})
    public static class MapperScannerRegistrarNotFoundConfiguration implements InitializingBean {
        @Override
        public void afterPropertiesSet() {
        }
    }
}
