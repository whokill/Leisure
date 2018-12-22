package cn.linghouse.Entity;
/*
 *Create by on 2018/12/19
 *Author:Linghouse
 *describe:EventBus事件实体类
 */

public class MessageEvent {
    private String message;

    public MessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
