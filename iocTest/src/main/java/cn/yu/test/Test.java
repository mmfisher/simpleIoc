package cn.yu.test;


import cn.yu.main.BeanFactory;
import cn.yu.main.ClassPathXmlApplicationContext;
import cn.yu.model.A;
import cn.yu.model.B;

public class Test {

   public static void main(String[] args){

       BeanFactory bf = new ClassPathXmlApplicationContext("/applicationContext.xml");
       B b = (B)bf.getBean("b");

       System.out.print(b.getA().getName());

   }
}
