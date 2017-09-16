package mybatis.simple.demo.support.transfer.condition.group;

import mybatis.simple.demo.support.enums.Operate;

/**
 * Created by javen on 2017/9/2.
 */
public class OrConditionGroup extends ConditionGroup {

    public ConditionGroup addOr(String field, Operate operate, Object... value) {
        add(field, operate, value);
        return this;
    }
}
