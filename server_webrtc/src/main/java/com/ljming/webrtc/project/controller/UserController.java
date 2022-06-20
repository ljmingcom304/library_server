package com.ljming.webrtc.project.controller;


import com.ljming.webrtc.avchat.OnlineMember;
import com.ljming.webrtc.bean.RoomBean;
import com.ljming.webrtc.bean.UserBean;
import com.ljming.webrtc.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("/")
    public String index() {
        UserBean userBean = userService.getUserBean("001");
        String avatar = (userBean != null ? userBean.getAvatar() : null);
        return "welcome to my webRTC demo=" + avatar;
    }


    @ResponseBody
    @RequestMapping("/login")
    public UserBean userLogin(String phone) {
        UserBean userBean = userService.loginUserBeanByPhone(phone);
        return userBean;
    }

    public List<UserBean> friendList(String userId) {
        List<UserBean> userList = userService.getFriendBeans(userId);
        return userList;
    }

    @RequestMapping("/roomList")
    public List<RoomBean> roomList() {
        List<RoomBean> rooms = OnlineMember.getRooms();
        return rooms;
    }

    @RequestMapping("/userList")
    public List<UserBean> userList() {
        return userService.getUserBeans();
    }

}
