package mybatis.simple.demo.support.transfer;

import mybatis.simple.demo.beans.PageParams;
import mybatis.simple.demo.support.enums.Operate;
import mybatis.simple.demo.support.transfer.condition.Condition;
import mybatis.simple.demo.support.transfer.condition.group.ConditionGroup;

/**
 * Created by javen on 2017/8/30.
 */
public class SQLParams extends BaseSQLParams {

    private SQLParams() {
    }

    public static SQLParams instance() {
        return new SQLParams();
    }

    /**
     * 多次执行复用同一个SQLParams实例时，防止受原缓存数据影响，应先重置对象
     */
    public SQLParams reset() {
        insertObj = null;
        updateObj = null;
        pageParams = null;
        conditionList.clear();
        conditionGroupList.clear();
        updateParamMap.clear();
        sortMap.clear();
        excludeSelectList.clear();
        customParams.clear();
        return this;
    }

    public SQLParams where(String field, Object value) {
        conditionList.add(new Condition(field, Operate.EQ, value));
        return this;
    }

    public SQLParams where(String field, Operate operate, Object... value) {
        conditionList.add(new Condition(field, operate, value));
        return this;
    }

    public SQLParams where(ConditionGroup conditionGroup) {
        conditionGroupList.add(conditionGroup);
        return this;
    }

    public SQLParams insert(Object obj) {
        insertObj = obj;
        return this;
    }

    public SQLParams update(String field, Object obj) {
        updateParamMap.put(field, obj);
        return this;
    }

    public SQLParams update(Object obj) {
        updateObj = obj;
        return this;
    }

    public SQLParams desc(String... fields) {
        addSort("desc", fields);
        return this;
    }

    public SQLParams asc(String... fields) {
        addSort("asc", fields);
        return this;
    }

    public SQLParams page(PageParams pageParams) {
        this.pageParams = pageParams;
        return this;
    }

    public SQLParams excludeSelect(String... fields) {
        if (fields != null) {
            for (String field : fields) {
                excludeSelectList.add(field);
            }
        }
        return this;
    }

    public SQLParams addCustomParam(String field, Object value) {
        customParams.put(field, value);
        return this;
    }
}
