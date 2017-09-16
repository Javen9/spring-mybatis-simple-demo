package mybatis.simple.demo.support;

import mybatis.simple.demo.support.base.BasePo;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by javen on 2017/8/30.
 */
public class ResultMappingFactory implements InitializingBean {

    private SqlSessionFactory sqlSessionFactory;

    private static Map<Class<?>, Map<String, String>> PropertyColumnCache = new HashMap<>();
    private static Map<String, Class<? extends BasePo>> poClassCache = new HashMap<>();

    public ResultMappingFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Collection<ResultMap> resultMaps = sqlSessionFactory.getConfiguration().getResultMaps();
        for (Object obj : resultMaps) {
            if (obj instanceof ResultMap) {
                ResultMap resultMap = (ResultMap) obj;
                if (resultMap.getId().indexOf(".BaseResultMap") >= 0) {
                    Map<String, String> mm = new HashMap<>();
                    List<ResultMapping> rms = resultMap.getResultMappings();
                    for (ResultMapping rm : rms) {
                        mm.put(rm.getProperty(), rm.getColumn());
                    }
                    PropertyColumnCache.put(resultMap.getType(), mm);
                    if (BasePo.class.isAssignableFrom(resultMap.getType())) {
                        String resultMapId = resultMap.getId();
                        String mapperName = resultMapId.substring(0, resultMapId.lastIndexOf("."));
                        poClassCache.put(mapperName, (Class<? extends BasePo>) resultMap.getType());
                    }
                }
            }
        }
    }

    public static Map<String, String> getPropertyColumnMap(Class<?> poClass) {
        return PropertyColumnCache.get(poClass);
    }

    public static Class<? extends BasePo> getPoClass(String mapperName) {
        return poClassCache.get(mapperName);
    }
}
