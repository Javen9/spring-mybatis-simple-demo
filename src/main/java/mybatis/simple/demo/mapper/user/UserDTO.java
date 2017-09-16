package mybatis.simple.demo.mapper.user;

/**
 * Created by javen on 2017/8/28.
 */
public class UserDTO {
    private Integer pk;

    private String name;

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
