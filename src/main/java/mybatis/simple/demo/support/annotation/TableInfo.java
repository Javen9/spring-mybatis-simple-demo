package mybatis.simple.demo.support.annotation;

import org.springframework.beans.factory.annotation.Required;

import java.lang.annotation.*;

/**
 * Po映射表注解
 * Created by javen on 2017/8/30.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableInfo {
    @Required
    String name();

    String pk() default "id";
}
