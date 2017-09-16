package mybatis.simple.demo.support.base;

/**
 * Created by javen on 2017/8/28.
 */
public class BaseSqlProvider {

    public String insert() {
        return "insert into ${tableName} ${commonInsert}";
    }

    public String delete() {
        return "delete from ${tableName} where ${strictWhere}";
    }

    public String deleteByPK() {
        return "delete from ${tableName} where ${primaryKey}=#{pk_value}";
    }

    public String update() {
        return "update ${tableName} set ${commonUpdate} where ${strictWhere}";
    }

    public String find() {
        return "select ${selectColumns} from ${tableName} where ${commonWhere} order by ${commonOrderBy}";
    }

    public String findOne() {
        return "select ${selectColumns} from ${tableName} where ${commonWhere} limit 1";
    }

    public String findByPK() {
        return "select * from ${tableName} where ${primaryKey}=#{pk_value}";
    }

    public String count() {
        return "select count(1) from ${tableName} where ${commonWhere}";
    }

    public String page() {
        return "select ${selectColumns} from ${tableName} where ${commonWhere} order by ${commonOrderBy} LIMIT #{pageParams.start}, #{pageParams.pageSize}";
    }
}
