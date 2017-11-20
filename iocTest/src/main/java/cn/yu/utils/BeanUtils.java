package cn.yu.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public class BeanUtils {
    public static Method getWriteMethod(Object beanObj, String name) {

        Method writeMethod = null;

        try {
            //分析bean对象
            BeanInfo info = Introspector.getBeanInfo(beanObj.getClass());
            //根据beanInfo获取所有的属性描述器
            PropertyDescriptor[] pds = info.getPropertyDescriptors();
            //遍历这些属性描述器
            if(pds != null){
                for ( PropertyDescriptor pd: pds){
                    String pName = pd.getName();
                    if (pName.equals(name)){
                        writeMethod = pd.getWriteMethod();
                    }
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        if(writeMethod == null){
            throw new RuntimeException("请检查熟悉的set方法是否创建");
        }
        return writeMethod;
    }
}
