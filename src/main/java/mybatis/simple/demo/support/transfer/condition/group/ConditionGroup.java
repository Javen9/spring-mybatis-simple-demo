package mybatis.simple.demo.support.transfer.condition.group;

import mybatis.simple.demo.support.enums.Operate;
import mybatis.simple.demo.support.transfer.condition.Condition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by javen on 2017/9/2.
 */
public abstract class ConditionGroup {

    private List<Condition> conditionList = new ArrayList<>();

    protected void add(String field, Operate operate, Object... value) {
        conditionList.add(new Condition(field, operate, value));
    }

    public List<Condition> getConditionList() {
        return conditionList;
    }
}
