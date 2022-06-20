package com.ljming.webrtc.avchat;

/**
 * Title:EventBuilder
 * <p>
 * Description:建造者
 * </p >
 * Author Jming.L
 * Date 2022/3/15 10:59
 */
public enum EventBuilder {

    INSTANCE;

    private EventHandler handler;

    EventBuilder() {
        handler = new EventHandler();
    }

    private EventHandler getInstance() {
        return handler;
    }


    public static EventHandler build() {
        return EventBuilder.INSTANCE.getInstance();
    }

}
