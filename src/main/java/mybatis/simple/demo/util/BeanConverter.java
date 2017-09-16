package mybatis.simple.demo.util;

import mybatis.simple.demo.exception.BeanConverterException;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对象转换工具
 * Created by javen on 2017/8/31.
 */
public class BeanConverter {

    /**
     * 缓存转换过类型的属性描述
     */
    private static Map<String, Map<String, PropertyDescriptor>> propertyDescriptorCache = new ConcurrentHashMap<>();

    /**
     * 將对象转换成指定类型列表(针对单一对象)
     *
     * @param src        源对象
     * @param targetType 目标类型
     * @return
     */
    public static <T> T copy(Object src, Class<T> targetType) {
        return convert(src, targetType);
    }

    /**
     * 將列表转换成指定类型列表(针对列表对象)
     *
     * @param srcList    源列表
     * @param targetType 目标类型
     * @return
     */
    public static <T> List<T> copy(List<?> srcList, Class<T> targetType) {
        List<T> list = new ArrayList<T>();
        for (Object src : srcList) {
            list.add(convert(src, targetType));
        }
        return list;
    }

    /**
     * 将对象转换成指定类型
     *
     * @param src        源对象
     * @param targetType 目标类型
     * @return
     */
    private static <T> T convert(Object src, Class<T> targetType) {
        if (src == null || targetType == null) {
            return null;
        }
        try {
            Map<String, PropertyDescriptor> src_descriptors = getPropertyDescriptors(src.getClass());
            Map<String, PropertyDescriptor> type_descriptors = getPropertyDescriptors(targetType);
            T bean = targetType.newInstance();
            for (String key : src_descriptors.keySet()) {
                PropertyDescriptor type_descriptor = type_descriptors.get(key);
                if (type_descriptor != null) {
                    PropertyDescriptor src_descriptor = src_descriptors.get(key);
                    Method readMethod = src_descriptor.getReadMethod();
                    Method writeMethod = type_descriptor.getWriteMethod();
                    if (readMethod != null && writeMethod != null) {
                        Object value = readMethod.invoke(src);
                        Class<?> propertyType = type_descriptor.getPropertyType();
                        if (value != null) {
                            if (value instanceof List) {
                                if (!List.class.isAssignableFrom(propertyType)) {
                                    continue;
                                }
                                Type fieldType = targetType.getDeclaredField(key).getGenericType();
                                if (fieldType instanceof ParameterizedType) {
                                    List list = Modifier.isAbstract(propertyType.getModifiers()) ? new ArrayList<>() : (List) propertyType.newInstance();
                                    Class clas = (Class) ((ParameterizedType) fieldType).getActualTypeArguments()[0];
                                    list.addAll(copy((List) value, clas));
                                    value = list;
                                }
                            } else if (value.getClass() != propertyType) {
                                value = differentTypeConvert(value, propertyType);
                            }
                        }
                        //值为null时直接忽略(同时也规避了基本数据类型问题)
                        if (value == null) {
                            continue;
                        }
                        writeMethod.invoke(bean, value);
                    }
                }
            }
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 不同类型之间值转换
     */
    private static Object differentTypeConvert(Object value, Class<?> propertyType) {
        Object result = value;
        if (propertyType == BigDecimal.class) {
            result = new BigDecimal(value.toString());
        } else if (propertyType.isEnum()) {
            Object[] enums = propertyType.getEnumConstants();
            for (Object item : enums) {
                if (item.toString().equals(value.toString())) {
                    result = item;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 将对象中属性转成以Map存储
     * <p>
     * 默认忽略值为null的属性
     * </p>
     *
     * @param obj 对象
     * @return
     */
    public static Map<String, Object> beanToMap(Object obj) {
        return beanToMap(obj, true);
    }

    /**
     * 将对象中属性转成以Map存储
     *
     * @param obj        对象
     * @param ignoreNull 是否忽略值为null的属性
     * @return
     */
    public static Map<String, Object> beanToMap(Object obj, boolean ignoreNull) {
        Map<String, Object> returnMap = new HashMap<>();
        if (obj == null) {
            return returnMap;
        }
        try {
            Map<String, PropertyDescriptor> descriptors = getPropertyDescriptors(obj.getClass());
            for (PropertyDescriptor descriptor : descriptors.values()) {
                String propertyName = descriptor.getName();
                Method readMethod = descriptor.getReadMethod();
                if (readMethod != null) {
                    Object result = ReflectionUtils.invokeMethod(readMethod, obj);
                    if (ignoreNull) {
                        if (result != null) {
                            returnMap.put(propertyName, result);
                        }
                    } else {
                        returnMap.put(propertyName, result);
                    }
                }
            }
        } catch (Exception e) {
            throw new BeanConverterException(e);
        }
        return returnMap;
    }

    /**
     * 获取指定类属性定义信息
     */
    private static Map<String, PropertyDescriptor> getPropertyDescriptors(Class<?> clazz)
            throws IntrospectionException {
        synchronized (clazz) {
            String canonicalName = clazz.getCanonicalName();
            Map<String, PropertyDescriptor> descriptorMap = propertyDescriptorCache.get(canonicalName);
            if (descriptorMap != null) {
                return descriptorMap;
            }

            descriptorMap = new HashMap<>();
            PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(clazz);
            for (PropertyDescriptor descriptor : descriptors) {
                if (descriptor.getPropertyType() == Class.class) {
                    continue;
                }
                descriptorMap.put(descriptor.getName(), descriptor);
            }

            propertyDescriptorCache.put(canonicalName, descriptorMap);
            return descriptorMap;
        }
    }
}
