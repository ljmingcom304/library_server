package com.ljming.webrtc.avchat;

import com.ljming.webrtc.bean.RoomBean;
import com.ljming.webrtc.bean.UserBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Title:OnlineMember
 * <p>
 * Description:
 * </p >
 * Author Jming.L
 * Date 2022/3/17 10:08
 */
public class OnlineMember {


    // 在线用户表
    private static ConcurrentHashMap<String, UserBean> userBeans = new ConcurrentHashMap<>();

    // 在线房间表
    private static ConcurrentHashMap<String, RoomBean> rooms = new ConcurrentHashMap<>();

    /**
     * 获取当前在线用户
     */
    public static UserBean getUserBean(String userId) {
        return userBeans.get(userId);
    }

    /**
     * 设置在线用户
     */
    public static void putUserBean(UserBean userBean) {
        userBeans.put(userBean.getUserId(), userBean);
    }

    /**
     * 移除离线用户
     */
    public static void removeUserBean(String userId) {
        userBeans.remove(userId);
    }

    /**
     * 获取所有在线房间
     */
    public static List<RoomBean> getRooms() {
        return new ArrayList<>(rooms.values());
    }

    /**
     * 获取指定在线房间
     */
    public static RoomBean getRoomInfo(String roomId) {
        return rooms.get(roomId);
    }

    /**
     * 创建在线房间
     */
    public static void putRoomInfo(RoomBean roomInfo) {
        rooms.put(roomInfo.getRoomId(), roomInfo);
    }

    /**
     * 移除离线房间
     */
    public static void removeRoomInfo(String roomId) {
        rooms.remove(roomId);
    }


}
