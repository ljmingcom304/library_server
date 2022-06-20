package com.ljming.webrtc.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.websocket.Session;
import java.io.Serializable;

public class UserBean implements Serializable{

    private Integer id;
    @JsonFormat
    private String userId;
    @JsonFormat
    private String avatar;

    private String phone;

    private String name;

    private boolean isApp;

    private DeviceSession pcSession;
    private DeviceSession phoneSession;

    public UserBean() {
        super();
    }


    public UserBean(String userId, String avatar) {
        this.userId = userId;
        this.avatar = avatar;
    }

    @JsonIgnore
    public void setPhoneSession(Session session, int device) {
        if (session == null) {
            this.phoneSession = null;
            return;
        }
        this.phoneSession = new DeviceSession(session, device);
    }

    @JsonIgnore
    public void setPcSession(Session session, int device) {
        if (session == null) {
            this.pcSession = null;
            return;
        }
        this.pcSession = new DeviceSession(session, device);
    }

    @JsonIgnore
    public Session getPhoneSession() {
        return phoneSession == null ? null : phoneSession.getSession();
    }

    @JsonIgnore
    public Session getPcSession() {
        return pcSession == null ? null : pcSession.getSession();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonIgnore
    public String getUserId() {
        return userId;
    }

    @JsonIgnore
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @JsonIgnore
    public String getAvatar() {
        return avatar;
    }

    @JsonIgnore
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        UserBean user = (UserBean) obj;
        return this.userId.equals(user.getUserId());
    }

    public boolean isApp() {
        return isApp;
    }

    public void setApp(boolean phone) {
        isApp = phone;
    }


}
