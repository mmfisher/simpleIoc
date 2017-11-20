package cn.yu.main;

public interface BeanFactory {

    /**
     * 根据beanNamehuo获取bean对象
     * @param BeanName
     * @return
     */
    Object getBean(String BeanName);
}
