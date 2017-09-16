package mybatis.simple.demo.mapper.user;

import mybatis.simple.demo.support.annotation.TableInfo;
import mybatis.simple.demo.support.base.BasePo;

/**
 * Created by javen on 2017/8/28.
 */
@TableInfo(name = "test", pk = "pk")
public class UserPo extends BasePo {
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
