package mybatis.simple.demo.support.interceptor;

import mybatis.simple.demo.support.ResultMappingFactory;
import mybatis.simple.demo.support.annotation.TableInfo;
import mybatis.simple.demo.support.base.BaseSqlProvider;
import mybatis.simple.demo.support.transfer.SQLParams;
import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;

/**
 * Created by javen on 2017/8/30.
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class SqlInterceptor implements Interceptor {

    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        //1、转换SqlSource类型，避免每一次都从ProviderSqlSource获取
        //2、ProviderSqlSource方式不支持${}传参(不支持预编译)
        SqlSource sqlSource = mappedStatement.getSqlSource();
        if (sqlSource instanceof ProviderSqlSource) {
            Field field = ReflectionUtils.findField(ProviderSqlSource.class, "providerMethod");
            field.setAccessible(true);
            Method method = (Method) ReflectionUtils.getField(field, sqlSource);

            String sql = (String) ReflectionUtils.invokeMethod(method, new BaseSqlProvider());
            sql = "<script>" + sql + "</script>";

            SqlSource sqlSource_new = new XMLLanguageDriver().createSqlSource(mappedStatement.getConfiguration(), sql, null);
            MetaObject msObject = SystemMetaObject.forObject(mappedStatement);
            msObject.setValue("sqlSource", sqlSource_new);
        }
        if (invocation.getArgs().length > 1) {
            Object arg = invocation.getArgs()[1];
            String resultMapId = mappedStatement.getResultMaps().get(0).getId();
            String mapperName = resultMapId.substring(0, resultMapId.lastIndexOf("."));
            Class poClass = ResultMappingFactory.getPoClass(mapperName);
            if (arg instanceof SQLParams) {
                SQLParams sqlParams = (SQLParams) arg;
                sqlParams.initialize(poClass);
            } else if (arg instanceof Map) {
                TableInfo tableInfo = (TableInfo) poClass.getAnnotation(TableInfo.class);
                if (tableInfo != null) {
                    ((Map) arg).put("tableName", tableInfo.name());
                    ((Map) arg).put("primaryKey", tableInfo.pk());
                }
            }
        }
        return invocation.proceed();
    }

    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    public void setProperties(Properties properties) {
    }
}
