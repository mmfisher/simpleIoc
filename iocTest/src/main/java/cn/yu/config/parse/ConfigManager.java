package cn.yu.config.parse;

import cn.yu.bean.Bean;
import cn.yu.bean.Property;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigManager {

    public static Map<String, Bean> getConfig(String path){
        //创建一个用于返回的map对象
        Map<String, Bean> map = new HashMap<String, Bean>();
        //1.创建解析器
        SAXReader reader = new SAXReader();
        //2.加载配置文件
        InputStream is = ConfigManager.class.getResourceAsStream(path);
        Document document = null;
        try {
            document = reader.read(is);
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new RuntimeException("xml配置错误");
        }
        //3.定义xpath表达式
        String xpath = "//bean";
        //4.对bean元素进行遍历
        List<Element> list = document.selectNodes(xpath);
        if(list != null){
            for (Element beanEle: list) {
                Bean bean = new Bean();
                //将bean元素的name/class属性封装到Bean对象中
                String name = beanEle.attributeValue("name");
                String className = beanEle.attributeValue("class");
                bean.setClassName(className);
                bean.setName(name);
                //获取Bean元素下的所有proeperty子元素
                List<Element> children = beanEle.elements("property");

                if(children != null){
                    for (Element child : children){
                        Property prop = new Property();

                        String pName = child.attributeValue("name");
                        String pValue = child.attributeValue("value");
                        String pRef = child.attributeValue("ref");

                        prop.setName(pName);
                        prop.setRef(pRef);
                        prop.setValue(pValue);

                        bean.getProperties().add(prop);
                    }
                }
                map.put(name, bean);
            }
        }
        return map;
    }
}
