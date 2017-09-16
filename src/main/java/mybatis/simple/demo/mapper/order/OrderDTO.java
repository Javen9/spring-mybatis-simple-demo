package mybatis.simple.demo.mapper.order;

import mybatis.simple.demo.mapper.order.enums.OrderType;

import java.util.LinkedList;

/**
 * Created by javen on 2017/8/28.
 */
public class OrderDTO {
    private int id;

    private String name;

    private OrderType type;

    private LinkedList<OrderDTO> list;

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

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public LinkedList<OrderDTO> getList() {
        return list;
    }

    public void setList(LinkedList<OrderDTO> list) {
        this.list = list;
    }
}
