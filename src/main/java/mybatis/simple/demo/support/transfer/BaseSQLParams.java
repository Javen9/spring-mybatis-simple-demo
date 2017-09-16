package mybatis.simple.demo.support.transfer;

import mybatis.simple.demo.exception.MapperException;
import mybatis.simple.demo.support.ResultMappingFactory;
import mybatis.simple.demo.support.annotation.TableInfo;
import mybatis.simple.demo.support.base.BasePo;
import mybatis.simple.demo.beans.PageParams;
import mybatis.simple.demo.support.enums.Operate;
import mybatis.simple.demo.support.transfer.condition.Condition;
import mybatis.simple.demo.support.transfer.condition.group.ConditionGroup;
import mybatis.simple.demo.support.transfer.condition.group.OrConditionGroup;
import mybatis.simple.demo.util.BeanConverter;
import mybatis.simple.demo.util.StringTools;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by javen on 2017/8/30.
 */
public abstract class BaseSQLParams {

    /**
     * 新增数据时。返回由数据库自动生成的主键
     */
    private Object pk;

    /**
     * 存储指定的参数项
     */
    protected Object insertObj;
    protected Object updateObj;
    protected PageParams pageParams;
    protected List<Condition> conditionList = new ArrayList<>();
    protected List<ConditionGroup> conditionGroupList = new ArrayList<>();
    protected Map<String, Object> updateParamMap = new HashMap<>();
    protected Map<String, String> sortMap = new LinkedHashMap<>();
    protected List<String> excludeSelectList = new ArrayList<>();
    protected Map<String, Object> customParams = new HashMap<>();//自定义语句，传输参数使用
    /**
     * 存储执行sql临时参数项
     */
    protected Map<String, Object> conditionMap_;
    private Map<String, Object> updateParamMap_;
    private Map<String, Object> insertParamMap_;

    private String tableName;
    private String primaryKey;
    private Map<String, String> propertyColumnMap;

    /**
     * 语句执行前，初始化Po与表的映射信息
     */
    public void initialize(Class<? extends BasePo> poClass) {
        this.pk = null;
        if (poClass != null) {
            propertyColumnMap = ResultMappingFactory.getPropertyColumnMap(poClass);

            TableInfo tableInfo = poClass.getAnnotation(TableInfo.class);
            if (tableInfo != null) {
                this.tableName = tableInfo.name();
                this.primaryKey = tableInfo.pk();
            }
        }
    }

    public String getTableName() {
        if (StringTools.isNotEmptyByTrim(this.tableName)) {
            return this.tableName;
        }
        throw new MapperException("SQL参数错误，没有指定表名");
    }

    public String getPrimaryKey() {
        if (StringTools.isNotEmptyByTrim(this.primaryKey)) {
            return this.primaryKey;
        }
        throw new MapperException("SQL参数错误，没有指定主键");
    }

    public String getCommonInsert() {
        insertParamMap_ = new HashMap<>();
        if (insertObj != null) {
            insertParamMap_.putAll(BeanConverter.beanToMap(insertObj));
        }
        if (insertParamMap_.size() > 0) {
            List<String> columnList = new ArrayList<>();
            List<String> valueList = new ArrayList<>();
            for (String property : insertParamMap_.keySet()) {
                String columnName = propertyColumnMap.get(property) != null ? propertyColumnMap.get(property) : property;
                columnList.add(columnName);
                valueList.add("#{insertParamMap_." + property + "}");
            }
            return "(" + StringTools.coll2Str(columnList, ",") + ") values (" + StringTools.coll2Str(valueList, ",") + ")";
        }
        throw new MapperException("SQL参数错误，没有可插入的数据");
    }

    public String getCommonUpdate() {
        updateParamMap_ = new HashMap<>();
        if (updateObj != null) {
            updateParamMap_.putAll(BeanConverter.beanToMap(updateObj));
        }
        updateParamMap_.putAll(updateParamMap);
        if (updateParamMap_.size() > 0) {
            List<String> list = new ArrayList<>();
            for (String property : updateParamMap_.keySet()) {
                String columnName = propertyColumnMap.get(property) != null ? propertyColumnMap.get(property) : property;
                list.add(columnName + "=#{updateParamMap_." + property + "}");
            }
            return StringTools.coll2Str(list, ",");
        }
        throw new MapperException("SQL参数错误，没有指定更新字段");
    }

    public String getSelectColumns() {
        if (!(propertyColumnMap == null || propertyColumnMap.isEmpty())) {
            Map<String, String> propertyColumnMap_ = propertyColumnMap;
            if (excludeSelectList.size() > 0) {
                propertyColumnMap_ = new HashMap<>();
                propertyColumnMap_.putAll(propertyColumnMap);
                for (String field : excludeSelectList) {
                    propertyColumnMap_.remove(field);
                }
            }
            return propertyColumnMap_.values().stream().collect(Collectors.joining(","));
        }
        return "*";
    }

    public String getStrictWhere() {
        String where = buildWhere();
        if (StringTools.isEmptyByTrim(where)) {
            throw new MapperException("SQL参数错误，请指定where条件");
        }
        return where;
    }

    public String getCommonWhere() {
        String where = buildWhere();
        if (StringTools.isEmptyByTrim(where)) {
            where = "1=1";
        }
        return where;
    }

    public String getCommonOrderBy() {
        List<String> orderByList = new ArrayList<>();
        if (sortMap.size() > 0) {
            for (String key : sortMap.keySet()) {
                String columnName = propertyColumnMap.get(key);
                if (StringTools.isNotEmptyByTrim(columnName)) {
                    orderByList.add(columnName + " " + sortMap.get(key));
                }
            }
        }
        if (orderByList.size() == 0) {
            return getPrimaryKey() + " desc";
        }
        return StringTools.coll2Str(orderByList, ",");
    }

    public Map<String, Object> getConditionMap_() {
        return conditionMap_;
    }

    public Map<String, Object> getUpdateParamMap_() {
        return updateParamMap_;
    }

    public Map<String, Object> getInsertParamMap_() {
        return insertParamMap_;
    }

    public PageParams getPageParams() {
        return pageParams;
    }

    public Map<String, Object> getCustomParams() {
        return customParams;
    }

    public Object getPk() {
        return pk;
    }

    /**
     * 生成Where条件
     */
    private String buildWhere() {
        conditionMap_ = new HashMap<>();

        List<String> fragmentList = buildConditionFragmentList(propertyColumnMap, conditionList);
        for (ConditionGroup conditionGroup : conditionGroupList) {
            List<String> groupList = buildConditionFragmentList(propertyColumnMap, conditionGroup.getConditionList());
            if (groupList.size() < 2) {
                throw new MapperException("SQL条件参数错误，条件组中条件列表无法成组");
            }
            if (conditionGroup instanceof OrConditionGroup) {
                fragmentList.add("(" + StringTools.coll2Str(groupList, " or ") + ")");
            }
        }

        return StringTools.coll2Str(fragmentList, " and ");
    }

    /**
     * 根据条件对象列表 生成 条件片段列表
     */
    private List<String> buildConditionFragmentList(Map<String, String> propertyColumnMap, List<Condition> list) {
        List<String> fragmentList = new ArrayList<>();
        for (Condition condition : list) {
            String property = condition.getField();
            String columnName = propertyColumnMap.get(property) != null ? propertyColumnMap.get(property) : property;
            /**
             * key处理方式说明：适应重名的条件
             */
            String key = UUID.randomUUID().toString();
            boolean single = true;
            if (Operate.EQ.equals(condition.getOperate())) {
                fragmentList.add(columnName + "=#{conditionMap_." + key + "}");
            } else if (Operate.NE.equals(condition.getOperate())) {
                fragmentList.add(columnName + "<>#{conditionMap_." + key + "}");
            } else if (Operate.LT.equals(condition.getOperate())) {
                fragmentList.add(columnName + "<#{conditionMap_." + key + "}");
            } else if (Operate.LTE.equals(condition.getOperate())) {
                fragmentList.add(columnName + "<=#{conditionMap_." + key + "}");
            } else if (Operate.GT.equals(condition.getOperate())) {
                fragmentList.add(columnName + ">#{conditionMap_." + key + "}");
            } else if (Operate.GTE.equals(condition.getOperate())) {
                fragmentList.add(columnName + ">=#{conditionMap_." + key + "}");
            } else if (Operate.LIKE.equals(condition.getOperate())) {
                fragmentList.add(columnName + " like concat('%',#{conditionMap_." + key + "},'%')");
            } else if (Operate.PLIKE.equals(condition.getOperate())) {
                fragmentList.add(columnName + " like concat('%',#{conditionMap_." + key + "})");
            } else if (Operate.SLIKE.equals(condition.getOperate())) {
                fragmentList.add(columnName + " like concat(#{conditionMap_." + key + "},'%')");
            } else if (Operate.NLIKE.equals(condition.getOperate())) {
                fragmentList.add(columnName + " not like concat('%',#{conditionMap_." + key + "},'%')");
            } else {
                single = false;
                if (Operate.BTW.equals(condition.getOperate()) || Operate.NBTW.equals(condition.getOperate())) {
                    if (Operate.BTW.equals(condition.getOperate())) {
                        fragmentList.add(columnName + " between #{conditionMap_." + key + "1} and #{conditionMap_." + key + "2}");
                    } else if (Operate.NBTW.equals(condition.getOperate())) {
                        fragmentList.add(columnName + " not between #{conditionMap_." + key + "1} and #{conditionMap_." + key + "2}");
                    }
                    conditionMap_.put(key + "1", condition.getValue()[0]);
                    conditionMap_.put(key + "2", condition.getValue()[1]);
                } else if (Operate.IN.equals(condition.getOperate()) || Operate.NIN.equals(condition.getOperate())) {
                    List<String> vList = new ArrayList<>();
                    for (int index = 0; index < condition.getValue().length; index++) {
                        vList.add("#{conditionMap_." + key + index + "}");
                        conditionMap_.put(key + index, condition.getValue()[index]);
                    }
                    if (Operate.IN.equals(condition.getOperate())) {
                        fragmentList.add(columnName + " in (" + StringTools.coll2Str(vList, ",") + ")");
                    } else if (Operate.NIN.equals(condition.getOperate())) {
                        fragmentList.add(columnName + " not in (" + StringTools.coll2Str(vList, ",") + ")");
                    }
                } else if (Operate.ISNULL.equals(condition.getOperate())) {
                    fragmentList.add(columnName + " is null");
                } else if (Operate.ISNNULL.equals(condition.getOperate())) {
                    fragmentList.add(columnName + " is not null");
                }
            }
            if (single) {
                conditionMap_.put(key, condition.getValue()[0]);
            }
        }
        return fragmentList;
    }

    /**
     * 添加排序项
     */
    protected void addSort(String type, String... fields) {
        if (fields != null) {
            for (String field : fields) {
                if (StringTools.isNotEmptyByTrim(field)) {
                    sortMap.put(field, type);
                }
            }
        }
    }
}
