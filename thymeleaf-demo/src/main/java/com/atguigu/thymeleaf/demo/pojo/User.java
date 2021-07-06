package com.atguigu.thymeleaf.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {


    String name;
    int age;
    String role;
    //对象类型属性
    User friend;
}
