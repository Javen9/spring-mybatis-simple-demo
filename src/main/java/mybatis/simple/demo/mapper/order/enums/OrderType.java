package mybatis.simple.demo.mapper.order.enums;

/**
 * Created by javen on 2017/9/4.
 */
public enum OrderType {

    sample(1),

    wholesale(2);

    private int value;

    OrderType(int value) {
        this.value = value;
    }

    public String toString() {
        return String.valueOf(value);
    }
}
