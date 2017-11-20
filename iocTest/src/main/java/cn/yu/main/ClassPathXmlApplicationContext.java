package cn.yu.main;

import cn.yu.bean.Bean;
import cn.yu.bean.Property;
import cn.yu.config.parse.ConfigManager;
import cn.yu.utils.BeanUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yu
 */
public class ClassPathXmlApplicationContext implements BeanFactory {

    /** 配置信息 */
    private Map<String, Bean> config;
    /** ioc容器 */
    private  Map<String, Object> context = new HashMap<String, Object>();


    public ClassPathXmlApplicationContext(String path) {

        config = ConfigManager.getConfig(path);
        if(config != null){
            for (Map.Entry<String, Bean> en : config.entrySet()) {

                String beanName = en.getKey();
                Bean bean = en.getValue();
                //检查bean是否已经存在,单例bean
                Object existBean = context.get(beanName);
                if(existBean == null){
                    //createBean方法也会向容器中加入bean,要检查bean是否存在
                    Object beanObj = createBean(bean);
                    context.put(beanName, beanObj);
                }

            }
        }
    }

    /**
     * 根据配置创建bean对象
     * @param bean
     * @return
     */
    private Object createBean(Bean bean) {
        //1.获得要创建的bean的class

        String className = bean.getClassName();
        Class clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(className + "没有找到,请检查配置");
        }
        //创建对象
        //没有空参构造会有异常
        Object beanObj = null;
        try {
            beanObj = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(className + ",必须有空参构造");
        }
        //2.获得bean的属性
        if(bean.getProperties() != null){
            for(Property prop : bean.getProperties()){

                String name = prop.getName();
                //根据属性名称获取注入属性对应的set方法
                Method setMethod = BeanUtils.getWriteMethod(beanObj, name);
                //set方法要注入的参数
                Object param = null;
                if(prop.getValue() != null){
                    //(1).value属性注入
                    param = prop.getValue();
                }

                if(prop.getRef() != null){

                    //(2).其他bean的注入
                    //首先检查容器中是否已经创建的ref的bean
                    Object existBean = context.get(prop.getRef());
                    if(existBean == null){
                        //容器中没有bean,创建该bean
                        existBean = createBean(config.get(prop.getRef()));
                        //将创建好的Bean放入容器
                        context.put(prop.getRef(), existBean);

                    }
                    param = existBean;
                }

                //注入
                try {
                    setMethod.invoke(beanObj, param);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(className + "的属性" + name + "没有合法的set方法,或参数不正确" );
                }
            }
        }

        return beanObj;
    }

    @Override
    public Object getBean(String beanName) {

        return context.get(beanName);
    }
}
