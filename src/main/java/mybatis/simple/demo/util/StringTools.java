package mybatis.simple.demo.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by javen on 2017/8/31.
 */
public class StringTools {

    public static boolean isEmptyByTrim(String... values) {
        if (values == null) {
            return true;
        }
        for (String value : values) {
            if (value == null || value.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNotEmptyByTrim(String... values) {
        return !isEmptyByTrim(values);
    }

    public static String coll2Str(Collection<String> coll, String reg) {
        if (coll == null) {
            return null;
        }
        return coll.stream().filter((v) -> {
            return isNotEmptyByTrim(v);
        }).collect(Collectors.joining(reg));
    }

    public static void main(String[] args) {
        String dd = coll2Str(new ArrayList<>(), " and ");
        System.out.print(dd);
    }
}
