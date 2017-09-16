package mybatis.simple.demo.support;

import mybatis.simple.demo.beans.PageResponse;
import mybatis.simple.demo.support.base.BaseMapper;
import mybatis.simple.demo.support.transfer.SQLParams;
import mybatis.simple.demo.util.BeanConverter;

/**
 * Created by javen on 2017/9/2.
 */
public class PageHelper {

    public static <T> PageResponse<T> query(BaseMapper<?, ?> mapper, SQLParams sqlParams, Class<T> type) {
        return new PageResponse(sqlParams.getPageParams(), mapper.count(sqlParams), BeanConverter.copy(mapper.page(sqlParams), type));
    }
}
