package com.atzrh.controller;

import com.atzrh.pojo.User;
import com.atzrh.service.UserService;
import com.ssm.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zrh
 * @version 1.0.0
 * @title UserController
 * @description <TODO description class purpose>
 * @create 2023/11/3 22:59
 **/
@Controller
public class UserController {
    @AutoWired
    UserService userService;
    @RequestMapping("/test")
    public String test(HttpServletRequest req, HttpServletResponse rep,@RequestPara("name") String name) throws IOException {
        rep.setContentType("text/html;charset=utf-8");
        PrintWriter out = rep.getWriter();
        out.print("<h1>Test Success!</h1>");
        req.setAttribute("nameInfo",name);
        return "forward:/user.jsp";
    }

    @ResponseBody
    @RequestMapping("/json")
    public List<User> json(HttpServletRequest req, HttpServletResponse rep,@RequestPara("name") String name){
        User user = new User(1, name, 25);
        List<User> userList = new ArrayList<>();
        userList.add(user);
        userList.add(user);
        return userList;
    }
}
