package com.ljming.webrtc.project.service;

import com.ljming.webrtc.bean.UserBean;
import com.ljming.webrtc.project.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Title:UserService
 * <p>
 * Description:注册用户
 * </p >
 * Author Jming.L
 * Date 2022/3/11 15:50
 */
@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public UserBean getUserBean(String userId) {
        return userDao.selectUserBeanByUserId(userId);
    }

    public List<UserBean> getUserBeans() {
        return userDao.selectAllUserBeans();
    }

    @Transactional
    public UserBean loginUserBeanByPhone(String phone) {
        UserBean userBean = userDao.selectUserBeanByPhone(phone);
        if (userBean == null) {
            userBean = new UserBean();
            userBean.setPhone(phone);
            String uuid = UUID.nameUUIDFromBytes(phone.getBytes()).toString().replaceAll("-", "");
            userBean.setName(uuid.substring(0, 8));
            userBean.setUserId(uuid);
            userDao.insertUserBean(userBean);
        }
        return userBean;
    }

    /**
     * 通过UserId查询好友列表
     */
    public List<UserBean> getFriendBeans(String userId) {
        List<UserBean> userList = userDao.selectFriendBeansByUserId(userId);
        return userList;
    }
}



















