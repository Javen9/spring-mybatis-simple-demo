package mybatis.simple.demo.controller;

import mybatis.simple.demo.mapper.order.OrderPoMapper;
import mybatis.simple.demo.support.PageHelper;
import mybatis.simple.demo.beans.PageParams;
import mybatis.simple.demo.beans.PageResponse;
import mybatis.simple.demo.support.enums.Operate;
import mybatis.simple.demo.support.transfer.condition.group.OrConditionGroup;
import mybatis.simple.demo.support.transfer.SQLParams;
import mybatis.simple.demo.mapper.user.UserDTO;
import mybatis.simple.demo.mapper.user.UserPo;
import mybatis.simple.demo.mapper.user.UserPoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

/**
 * Created by javen on 2017/8/28.
 */
@RestController
public class TestController {

    @Autowired
    private UserPoMapper userPoMapper;
    @Autowired
    private OrderPoMapper orderPoMapper;

    @RequestMapping("test")
    public String test() {
        testUser();
        return "success";
    }

    private void testUser() {
        UserPo userPo = new UserPo();
        userPo.setName("name" + new Random().nextInt(10));
        SQLParams sqlParams = SQLParams.instance().insert(userPo);
        userPoMapper.insert(sqlParams);
        Object pk = sqlParams.getPk();

        sqlParams.reset().where("pk", 1);
        userPoMapper.delete(sqlParams);

        userPoMapper.deleteByPK(2);

        userPo = new UserPo();
        userPo.setName("name" + new Random().nextInt(10));
        sqlParams.reset().update(userPo).where("pk", pk);
        userPoMapper.update(sqlParams);

//        sqlParams.reset().update("name", null).where("pk", pk);
//        userPoMapper.update(sqlParams);

        sqlParams.reset().where("name", Operate.LIKE, "name").excludeSelect("name");
        List<UserPo> list = userPoMapper.find(sqlParams);

        sqlParams.reset().where("name", Operate.LIKE, "name");
        UserPo po1 = userPoMapper.findOne(sqlParams);

        sqlParams.reset().where("name", Operate.LIKE, "name");
        UserPo po2 = userPoMapper.findByPK(10);

//        sqlParams.reset().page(new PageParams()).where("name", Operate.LIKE, "name");
        OrConditionGroup orConditionGroup = new OrConditionGroup();
        orConditionGroup.addOr("name", Operate.LIKE, "6");
        orConditionGroup.addOr("name", Operate.LIKE, "9");
        sqlParams.reset().page(new PageParams()).where(orConditionGroup);
        PageResponse<UserDTO> page = PageHelper.query(userPoMapper, sqlParams, UserDTO.class);
    }
}