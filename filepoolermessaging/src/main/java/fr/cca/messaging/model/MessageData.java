package fr.cca.messaging.model;

public class MessageData {

    private String messageStr;
    public String getMessageStr() {
        return messageStr;
    }

    public void setMessageStr(String messageStr) {
        this.messageStr = messageStr;
    }

    public MessageData(){

    }


    public MessageData(String content){
        this.messageStr = content;
    }




}
