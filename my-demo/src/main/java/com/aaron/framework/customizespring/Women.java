package com.aaron.framework.customizespring;

import com.aaron.framework.customizespring.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2019-01-25
 */
@Component
public class Women
{
    @Autowired
    private StudentService student;


    public void introduce()
    {
        System.out.println("women");
    }
}
