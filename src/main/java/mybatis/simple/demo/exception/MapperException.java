package mybatis.simple.demo.exception;

/**
 * Created by javen on 2017/8/31.
 */
public class MapperException extends RuntimeException {
    public MapperException(String message) {
        super(message);
    }
}
