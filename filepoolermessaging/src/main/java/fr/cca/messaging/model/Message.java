package fr.cca.messaging.model;

import java.io.Serializable;


public class Message implements Serializable{
    private MessageType type;
    private String uuid;
    private MessageData data;

    public Message()
    {
    }
    public MessageData getData() {
        return data;
    }

    public void setData(MessageData data) {
        this.data = data;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Message(MessageType type, String uuid, String content) {
        this.type = type;
        this.uuid = uuid;
        this.data = new MessageData(content);
    }


}
