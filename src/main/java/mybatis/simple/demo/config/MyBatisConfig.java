package mybatis.simple.demo.config;

import mybatis.simple.demo.support.ResultMappingFactory;
import mybatis.simple.demo.support.interceptor.SqlInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Created by javen on 2017/8/28.
 */
@Configuration
@MapperScan("mybatis.simple.demo.mapper.*")
public class MyBatisConfig {

    @Autowired
    private DataSource dataSource;

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean() {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);

        factory.setPlugins(new Interceptor[]{new SqlInterceptor()});
        try {
            return factory.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Bean
    public ResultMappingFactory getFactory(SqlSessionFactory sqlSessionFactory) {
        return new ResultMappingFactory(sqlSessionFactory);
    }
}
