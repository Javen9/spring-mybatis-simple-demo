package mybatis.simple.demo.mapper.order;

import mybatis.simple.demo.support.annotation.TableInfo;
import mybatis.simple.demo.support.base.BasePo;

import java.util.List;

/**
 * Created by javen on 2017/8/28.
 */
@TableInfo(name = "orders")
public class OrderPo extends BasePo {
    private int id;

    private String name;

    private Integer type;

    private List<OrderPo> list;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<OrderPo> getList() {
        return list;
    }

    public void setList(List<OrderPo> list) {
        this.list = list;
    }
}
