package mybatis.simple.demo.support.transfer.condition;

import mybatis.simple.demo.exception.MapperException;
import mybatis.simple.demo.support.enums.Operate;

/**
 * Created by javen on 2017/8/30.
 */
public class Condition {

    private String field;
    private Operate operate;
    private Object[] value;

    public Condition(String field, Operate operate, Object... value) {
        if (field == null || operate == null) {
            throw new MapperException("SQL条件参数错误，条件不符合SQL语句要求");
        }
        if (!(Operate.ISNULL.equals(operate) || Operate.ISNNULL.equals(operate))) {
            if (value == null) {
                throw new MapperException("SQL条件参数错误，" + field + "  值不符合SQL语句要求");
            }
            if (Operate.BTW.equals(operate) || Operate.NBTW.equals(operate)) {
                if (value.length != 2) {
                    throw new MapperException("SQL条件参数错误，" + field + "  值不符合SQL语句要求");
                }
            }
        }
        this.field = field;
        this.operate = operate;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public Operate getOperate() {
        return operate;
    }

    public Object[] getValue() {
        return value;
    }
}
