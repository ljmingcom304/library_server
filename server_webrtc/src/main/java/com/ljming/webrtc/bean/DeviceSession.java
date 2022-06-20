package com.ljming.webrtc.bean;

import javax.websocket.Session;

/**
 * Title:DeviceSession
 * <p>
 * Description:会话
 * </p >
 * Author Jming.L
 * Date 2022/3/17 10:31
 */
public class DeviceSession {

    private Session session;
    private int device; // 0 phone 1 pc
    private int statue; // 0 idle  1 inCall


    public DeviceSession(Session session, int device) {
        this.session = session;
        this.device = device;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public int getDevice() {
        return device;
    }

    public void setDevice(int device) {
        this.device = device;
    }

    public int getStatue() {
        return statue;
    }

    public void setStatue(int statue) {
        this.statue = statue;
    }
}
