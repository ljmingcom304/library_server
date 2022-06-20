package com.ljming.webrtc.bean;

import java.util.Map;

/**
 * Title:EventBean
 * <p>
 * Description:事件
 * </p >
 * Author Jming.L
 * Date 2022/3/17 10:21
 */
public class EventBean {

    public static final String LOGIN = "__login_success";       //登录成功
    public static final String CREATE = "__create";             //创建房间
    public static final String PEERS = "__peers";               //创建节点
    public static final String INVITE = "__invite";             //通话邀请
    public static final String RING = "__ring";                 //响铃回复
    public static final String CANCEL = "__cancel";             //通话取消
    public static final String REJECT = "__reject";             //拒绝接听
    public static final String JOIN = "__join";                 //加入房间
    public static final String OFFER = "__offer";               //发起信令
    public static final String ANSWER = "__answer";             //信令应答
    public static final String CANDIDATE = "__ice_candidate";   //ICE
    public static final String LEAVE = "__leave";               //离开房间
    public static final String AUDIO = "__audio";               //语音通话
    public static final String DISCONNECT = "__disconnect";     //断开连接

    private String eventName;
    private Map<String, Object> data;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }




}
