package com.abinav.creation;

public class Chat {

    private String sender;
    private String receiver;
    private String message;

    //Constructor
    public Chat(String sender,String receiver,String message){
        this.sender=sender;
        this.receiver=receiver;
        this.message=message;
    }

    //Default Constructor
    public Chat(){}

    //Getter and setters for instance variables
    public String getSender(){
        return sender;
    }
    public void setSender(String sender){
        this.sender=sender;
    }
    public String getReceiver(){
        return receiver;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
