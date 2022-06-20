package com.ljming.webrtc.avchat;

import com.google.gson.Gson;
import com.ljming.webrtc.project.controller.WebSocketServer;
import com.ljming.webrtc.bean.EventBean;
import com.ljming.webrtc.bean.RoomBean;
import com.ljming.webrtc.bean.UserBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Title:EventHandler
 * <p>
 * Description:事件处理器
 * </p >
 * Author Jming.L
 * Date 2022/3/15 10:44
 */
public class EventHandler {

    private Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    private Map<String, Action> hashMap;
    private static Gson gson = new Gson();

    EventHandler() {
        hashMap = new ConcurrentHashMap<>();
        hashMap.put(EventBean.CREATE, new CreateEvent());
        hashMap.put(EventBean.INVITE, new InviteEvent());
        hashMap.put(EventBean.RING, new RingEvent());
        hashMap.put(EventBean.CANCEL, new CancelEvent());
        hashMap.put(EventBean.REJECT, new RejectEvent());
        hashMap.put(EventBean.JOIN, new JoinEvent());
        hashMap.put(EventBean.OFFER, new OfferEvent());
        hashMap.put(EventBean.ANSWER, new AnswerEvent());
        hashMap.put(EventBean.CANDIDATE, new CandidateEvent());
        hashMap.put(EventBean.LEAVE, new LeaveEvent());
        hashMap.put(EventBean.AUDIO, new AudioEvent());
        hashMap.put(EventBean.DISCONNECT, new DisconnectEvent());
    }

    /**
     * 执行相应的事件指令
     */
    public void handleEvent(String event, String message, Map<String, Object> data) {
        Action action = hashMap.get(event);
        action.execute(message, data);
    }


    public interface Action {

        String USER_ID = "userID";
        String USER_AVATAR = "avatar";
        String ROOM = "room";
        String ROOM_SIZE = "roomSize";
        String ROOM_OWNER = "you";
        String ROOM_GUEST = "connections";
        String USER_LIST = "userList";
        String INVITE_ID = "inviteID";
        String AUDIO_ONLY = "audioOnly";
        String TO_ID = "toID";
        String FROM_ID = "fromID";

        void execute(String message, Map<String, Object> data);

    }


    private class CreateEvent implements Action {

        @Override
        public void execute(String message, Map<String, Object> data) {
            String room = (String) data.get(ROOM);
            String userId = (String) data.get(USER_ID);

            logger.info(String.format("createRoom:%s ", room));

            RoomBean roomParam = OnlineMember.getRoomInfo(room);
            // 没有这个房间
            if (roomParam == null) {
                int size = (int) Double.parseDouble(String.valueOf(data.get(ROOM_SIZE)));
                // 创建房间
                RoomBean roomInfo = new RoomBean();
                roomInfo.setMaxSize(size);
                roomInfo.setRoomId(room);
                roomInfo.setUserId(userId);
                // 将房间储存起来
                OnlineMember.putRoomInfo(roomInfo);


                CopyOnWriteArrayList<UserBean> copy = new CopyOnWriteArrayList<>();
                // 将自己加入到房间里
                UserBean my = OnlineMember.getUserBean(userId);
                copy.add(my);
                OnlineMember.getRoomInfo(room).setUserBeans(copy);
                EventBean send = new EventBean();
                send.setEventName(EventBean.PEERS);
                Map<String, Object> map = new HashMap<>();
                map.put(ROOM_GUEST, "");
                map.put(ROOM_OWNER, userId);
                map.put(ROOM_SIZE, size);
                send.setData(map);
                logger.info(gson.toJson(send));
                sendMsg(my, -1, gson.toJson(send));
            }

        }
    }

    private class InviteEvent implements Action {


        @Override
        public void execute(String message, Map<String, Object> data) {
            String userList = (String) data.get(USER_LIST);
            String room = (String) data.get(ROOM);
            String inviteId = (String) data.get(INVITE_ID);
            boolean audioOnly = (boolean) data.get(AUDIO_ONLY);
            String[] users = userList.split(",");

            logger.info(String.format("room:%s,%s invite %s audioOnly:%b", room, inviteId, userList, audioOnly));

            // 给其他人发送邀请
            for (String user : users) {
                UserBean userBean = OnlineMember.getUserBean(user);
                if (userBean != null) {
                    sendMsg(userBean, -1, message);
                }
            }
        }

    }

    private class RingEvent implements Action {

        @Override
        public void execute(String message, Map<String, Object> data) {
            String room = (String) data.get(ROOM);
            String inviteId = (String) data.get(TO_ID);

            UserBean userBean = OnlineMember.getUserBean(inviteId);
            if (userBean != null) {
                sendMsg(userBean, -1, message);
            }
        }

    }

    private class CancelEvent implements Action {

        @Override
        public void execute(String message, Map<String, Object> data) {
            String room = (String) data.get(ROOM);
            String userList = (String) data.get(USER_LIST);
            String[] users = userList.split(",");
            for (String userId : users) {
                UserBean userBean = OnlineMember.getUserBean(userId);
                if (userBean != null) {
                    sendMsg(userBean, -1, message);
                }
            }

            if (OnlineMember.getRoomInfo(room) != null) {
                OnlineMember.removeRoomInfo(room);
            }
        }

    }

    private class RejectEvent implements Action {

        @Override
        public void execute(String message, Map<String, Object> data) {
            String room = (String) data.get(ROOM);
            String toID = (String) data.get(TO_ID);
            UserBean userBean = OnlineMember.getUserBean(toID);
            if (userBean != null) {
                sendMsg(userBean, -1, message);
            }
            RoomBean roomInfo = OnlineMember.getRoomInfo(room);
            if (roomInfo != null) {
                if (roomInfo.getMaxSize() == 2) {
                    OnlineMember.removeRoomInfo(room);
                }
            }
        }

    }

    private class JoinEvent implements Action {

        @Override
        public void execute(String message, Map<String, Object> data) {
            String room = (String) data.get(ROOM);
            String userID = (String) data.get(USER_ID);

            RoomBean roomInfo = OnlineMember.getRoomInfo(room);

            int maxSize = roomInfo.getMaxSize();
            CopyOnWriteArrayList<UserBean> roomUserBeans = roomInfo.getUserBeans();

            //房间已经满了
            if (roomUserBeans.size() >= maxSize) {
                return;
            }
            UserBean my = OnlineMember.getUserBean(userID);
            // 1. 將我加入到房间
            roomUserBeans.add(my);
            roomInfo.setUserBeans(roomUserBeans);
            OnlineMember.putRoomInfo(roomInfo);

            // 2. 返回房间里的所有人信息
            EventBean send = new EventBean();
            send.setEventName(EventBean.PEERS);
            Map<String, Object> map = new HashMap<>();

            String[] cons = new String[roomUserBeans.size()];
            for (int i = 0; i < roomUserBeans.size(); i++) {
                UserBean userBean = roomUserBeans.get(i);
                if (userBean.getUserId().equals(userID)) {
                    continue;
                }
                cons[i] = userBean.getUserId();
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < cons.length; i++) {
                if (cons[i] == null) {
                    continue;
                }
                sb.append(cons[i]).append(",");
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            map.put(ROOM_GUEST, sb.toString());
            map.put(ROOM_OWNER, userID);
            map.put(ROOM_SIZE, roomInfo.getMaxSize());
            send.setData(map);
            sendMsg(my, -1, gson.toJson(send));


            EventBean newPeer = new EventBean();
            newPeer.setEventName("__new_peer");
            Map<String, Object> sendMap = new HashMap<>();
            sendMap.put(USER_ID, userID);
            newPeer.setData(sendMap);

            // 3. 给房间里的其他人发送消息
            for (UserBean userBean : roomUserBeans) {
                if (userBean.getUserId().equals(userID)) {
                    continue;
                }
                sendMsg(userBean, -1, gson.toJson(newPeer));
            }
        }

    }

    private class OfferEvent implements Action {

        @Override
        public void execute(String message, Map<String, Object> data) {
            String userId = (String) data.get(USER_ID);
            UserBean userBean = OnlineMember.getUserBean(userId);
            sendMsg(userBean, -1, message);
        }

    }

    private class AnswerEvent implements Action {

        @Override
        public void execute(String message, Map<String, Object> data) {
            String userId = (String) data.get(USER_ID);
            UserBean userBean = OnlineMember.getUserBean(userId);
            if (userBean == null) {
                logger.info("用户 " + userId + " 不存在");
                return;
            }
            sendMsg(userBean, -1, message);
        }

    }

    private class CandidateEvent implements Action {

        @Override
        public void execute(String message, Map<String, Object> data) {
            String userId = (String) data.get(USER_ID);
            UserBean userBean = OnlineMember.getUserBean(userId);
            if (userBean == null) {
                logger.info("用户 " + userId + " 不存在");
                return;
            }
            sendMsg(userBean, -1, message);
        }

    }

    private class LeaveEvent implements Action {

        @Override
        public void execute(String message, Map<String, Object> data) {
            String room = (String) data.get(ROOM);
            String userId = (String) data.get(FROM_ID);
            if (userId == null) return;
            RoomBean roomInfo = OnlineMember.getRoomInfo(room);
            CopyOnWriteArrayList<UserBean> roomInfoUserBeans = roomInfo.getUserBeans();
            Iterator<UserBean> iterator = roomInfoUserBeans.iterator();
            while (iterator.hasNext()) {
                UserBean userBean = iterator.next();
                if (userId.equals(userBean.getUserId())) {
                    continue;
                }
                sendMsg(userBean, -1, message);

                if (roomInfoUserBeans.size() == 1) {
                    logger.info("房间里只剩下一个人");
                    if (roomInfo.getMaxSize() == 2) {
                        OnlineMember.removeRoomInfo(room);
                    }
                }

                if (roomInfoUserBeans.size() == 0) {
                    logger.info("房间无人");
                    OnlineMember.removeRoomInfo(room);
                }
            }
        }

    }

    private class AudioEvent implements Action {

        @Override
        public void execute(String message, Map<String, Object> data) {
            String userId = (String) data.get(USER_ID);
            UserBean userBean = OnlineMember.getUserBean(userId);
            if (userBean == null) {
                logger.info("用户 " + userId + " 不存在");
                return;
            }
            sendMsg(userBean, -1, message);
        }

    }

    private class DisconnectEvent implements Action {

        @Override
        public void execute(String message, Map<String, Object> data) {
            String room = (String) data.get(ROOM);
            String userId = (String) data.get(FROM_ID);
            if (userId == null) return;
            RoomBean roomInfo = OnlineMember.getRoomInfo(room);
            CopyOnWriteArrayList<UserBean> roomInfoUserBeans = roomInfo.getUserBeans();
            for (UserBean userBean : roomInfoUserBeans) {
                if (userId.equals(userBean.getUserId())) {
                    continue;
                }
                sendMsg(userBean, -1, message);

                if (roomInfoUserBeans.size() == 1) {
                    logger.info("房间里只剩下一个人");
                    if (roomInfo.getMaxSize() == 2) {
                        OnlineMember.removeRoomInfo(room);
                    }
                }

                if (roomInfoUserBeans.size() == 0) {
                    logger.info("房间无人");
                    OnlineMember.removeRoomInfo(room);
                }
            }
        }

    }

    // 给不同设备发送消息
    private void sendMsg(UserBean userBean, int device, String str) {
        if (device == 0) {
            Session phoneSession = userBean.getPhoneSession();
            if (phoneSession != null) {
                synchronized (WebSocketServer.class) {
                    phoneSession.getAsyncRemote().sendText(str);
                }
            }
        } else if (device == 1) {
            Session pcSession = userBean.getPcSession();
            if (pcSession != null) {
                synchronized (WebSocketServer.class) {
                    pcSession.getAsyncRemote().sendText(str);
                }
            }
        } else {
            Session phoneSession = userBean.getPhoneSession();
            if (phoneSession != null) {
                synchronized (WebSocketServer.class) {
                    phoneSession.getAsyncRemote().sendText(str);
                }
            }
            Session pcSession = userBean.getPcSession();
            if (pcSession != null) {
                synchronized (WebSocketServer.class) {
                    pcSession.getAsyncRemote().sendText(str);
                }
            }

        }

    }


}
