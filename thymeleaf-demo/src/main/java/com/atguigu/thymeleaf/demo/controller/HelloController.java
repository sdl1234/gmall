package com.atguigu.thymeleaf.demo.controller;


import com.atguigu.thymeleaf.demo.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class HelloController {


    @GetMapping("/111")
    public String test111(Model model){
        model.addAttribute("msg","Hello,Thymeleaf!");
        return "hello";
    }


    @GetMapping("test")
    public  String test(Model model){
//        User user  = new User("水东流",22,new User("柳岩",20,null));
//        model.addAttribute("msg","hello thymeleaf");
//        model.addAttribute("user",user);
//
//
//        List<User> users = Arrays.asList(
//          new User("柳岩",21 ,null),
//          new User("水东流",22 ,null),
//          new User("女子",23 ,null),
//          new User("男子",24 ,null)
//        );
//        model.addAttribute("users",users);

        User user =new User("水东流",22,"admin",new User("柳岩",20 ,"manager",null));
        model.addAttribute("msg","hello thymeleaf");
        model.addAttribute("user",user);

        return "hello";
    }


}
