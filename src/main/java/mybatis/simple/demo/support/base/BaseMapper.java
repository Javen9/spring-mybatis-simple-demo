package mybatis.simple.demo.support.base;

import mybatis.simple.demo.support.transfer.SQLParams;
import org.apache.ibatis.annotations.*;

import java.io.Serializable;
import java.util.List;

/**
 * 通用公共Mapper
 * Created by javen on 2017/8/28.
 */
public interface BaseMapper<P, PK extends Serializable> {

    @InsertProvider(type = BaseSqlProvider.class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = "pk")
    Integer insert(SQLParams queryParam);

    @DeleteProvider(type = BaseSqlProvider.class, method = "delete")
    Integer delete(SQLParams queryParam);

    @DeleteProvider(type = BaseSqlProvider.class, method = "deleteByPK")
    Integer deleteByPK(@Param("pk_value") PK pk);

    @UpdateProvider(type = BaseSqlProvider.class, method = "update")
    Integer update(SQLParams queryParam);

    @SelectProvider(type = BaseSqlProvider.class, method = "find")
    List<P> find(SQLParams queryParam);

    @SelectProvider(type = BaseSqlProvider.class, method = "findOne")
    P findOne(SQLParams queryParam);

    @SelectProvider(type = BaseSqlProvider.class, method = "findByPK")
    P findByPK(@Param("pk_value") PK pk);

    @SelectProvider(type = BaseSqlProvider.class, method = "count")
    int count(SQLParams queryParam);

    @SelectProvider(type = BaseSqlProvider.class, method = "page")
    List<P> page(SQLParams queryParam);
}
