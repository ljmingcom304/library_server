package com.ljming.webrtc.project.controller;


import com.ljming.webrtc.avchat.EventBuilder;
import com.ljming.webrtc.avchat.EventHandler;
import com.ljming.webrtc.avchat.OnlineMember;
import com.ljming.webrtc.bean.EventBean;
import com.ljming.webrtc.bean.UserBean;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.ljming.webrtc.project.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ServerEndpoint("/ws/{userId}/{device}")
@Component
public class WebSocketServer {

    private Logger logger = LoggerFactory.getLogger(WebSocketServer.class);


    private String userId;
    private Gson gson = new Gson();

    private static ApplicationContext appContext;

    /**
     * SpringBoot默认单例与WebSocket多对象冲突
     */
    public static void setApplicationContext(ApplicationContext appContext) {
        WebSocketServer.appContext = appContext;
    }


    // 用户userId登录进来
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId, @PathParam("device") String device) {
        int deviceInt = Integer.parseInt(device);
        UserBean userBean = OnlineMember.getUserBean(userId);
        if (userBean == null) {
            UserService userService = appContext.getBean(UserService.class);
            userBean = userService.getUserBean(userId);
        }
        if (deviceInt == 0) {
            userBean.setPhoneSession(session, deviceInt);
            userBean.setApp(true);
            logger.info("Phone用户登陆:" + userBean.getUserId() + ",session:" + session.getId());
        } else {
            userBean.setPcSession(session, deviceInt);
            userBean.setApp(false);
            logger.info("PC用户登陆:" + userBean.getUserId() + ",session:" + session.getId());
        }
        this.userId = userId;

        //加入在线列表
        OnlineMember.putUserBean(userBean);

        // 登陆成功，返回个人信息
        EventBean send = new EventBean();
        send.setEventName(EventBean.LOGIN);
        Map<String, Object> map = new HashMap<>();
        map.put(EventHandler.Action.USER_ID, userId);
        map.put(EventHandler.Action.USER_AVATAR, userBean.getAvatar());
        send.setData(map);
        session.getAsyncRemote().sendText(gson.toJson(send));
    }

    // 用户下线
    @OnClose
    public void onClose() {
        logger.info(userId + "-->onClose......");
        // 根据用户名查出房间,
        UserBean userBean = OnlineMember.getUserBean(userId);
        if (userBean != null) {
            if (userBean.isApp()) {
                Session phoneSession = userBean.getPhoneSession();
                if (phoneSession != null) {
                    try {
                        phoneSession.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    userBean.setPhoneSession(null, 0);
                    OnlineMember.removeUserBean(userId);
                }
                logger.info("Phone用户离开:" + userBean.getUserId());
            } else {
                Session pcSession = userBean.getPcSession();
                if (pcSession != null) {
                    try {
                        pcSession.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    userBean.setPcSession(null, 0);
                    OnlineMember.removeUserBean(userId);
                    logger.info("PC用户离开:" + userBean.getUserId());
                }
            }
        }

    }

    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("receive data:" + message);
        try {
            EventBean data = gson.fromJson(message, EventBean.class);
            EventBuilder.build().handleEvent(data.getEventName(), message, data.getData());
        } catch (JsonSyntaxException e) {
            logger.error("json解析错误：" + message);
        }
    }

}