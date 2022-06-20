package com.ljming.webrtc.project.dao;

import com.ljming.webrtc.bean.UserBean;

import java.util.List;

/**
 * Title:UserDao
 * <p>
 * Description:
 * </p >
 * Author Jming.L
 * Date 2022/3/11 15:53
 */
public interface UserDao {

    UserBean selectUserBeanByUserId(String userId);

    List<UserBean> selectAllUserBeans();

    UserBean selectUserBeanByPhone(String phone);

    int insertUserBean(UserBean userBean);

    List<UserBean> selectFriendBeansByUserId(String userId);
}
