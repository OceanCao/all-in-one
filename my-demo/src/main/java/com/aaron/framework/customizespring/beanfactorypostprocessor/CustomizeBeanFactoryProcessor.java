package com.aaron.framework.customizespring.beanfactorypostprocessor;

import com.aaron.framework.customizespring.CustomizeImportBeanDefinitionRegistrar;
import com.aaron.framework.customizespring.ImportBean;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

/**
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2018/5/16
 */
@Component
@Import ({ImportBean.class, CustomizeImportBeanDefinitionRegistrar.class})
public class CustomizeBeanFactoryProcessor implements BeanFactoryPostProcessor, ApplicationContextAware
{
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException
    {

        System.out.println("CustomizeBeanFactoryProcessor");
    }


    public static void main(String[] args)
    {
        LocalDateTime parse = LocalDateTime.parse("2015-12-23 21:58:44", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        System.out.println(parse.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")));

        System.out.println("");
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        System.out.println("applicationContext --> " + applicationContext);
    }
}
