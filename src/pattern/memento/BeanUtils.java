package pattern.memento;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;

public class BeanUtils {

    public static HashMap<String, Object> beanToMap(Object bean){
        HashMap<String, Object> result = new HashMap<String, Object>();
        try {
            BeanInfo info = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] props = info.getPropertyDescriptors();
            for(PropertyDescriptor prop : props){
                String propName = prop.getName();
                Method getter = prop.getReadMethod();
                if(!propName.equalsIgnoreCase("class")){
                    result.put(propName, getter.invoke(bean, new Object[]{}));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void mapToBean(Object bean, HashMap<String, Object> map){
        try {
            BeanInfo info = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] props = info.getPropertyDescriptors();
            for(PropertyDescriptor prop : props){
                String propName = prop.getName();
                if(map.containsKey(propName)){
                    Method setter = prop.getWriteMethod();
                    setter.invoke(bean, map.get(propName));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
