package mybatis.simple.demo.support.enums;

import java.util.HashMap;
import java.util.Map;

public enum Operate {
    EQ("="),
    NE("!="),
    LT("<"),
    LTE("<="),
    GT(">"),
    GTE(">="),
    BTW("Between...AND..."),
    NBTW("Not Between...AND..."),
    IN("IN"),
    NIN("NOT IN"),
    LIKE("LIKE"),
    PLIKE("PLIKE"),
    SLIKE("SLIKE"),
    NLIKE("NOT LIKE"),
    ISNULL("IS NULL"),
    ISNNULL("IS NOT NULL");

    private final String value;
    private static Map<String, Operate> enumMap = new HashMap<>();

    static {
        for (Operate type : Operate.values()) {
            enumMap.put(type.value, type);
        }
    }

    Operate(String value) {
        this.value = value;
    }
}
