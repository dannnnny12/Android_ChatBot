package com.example.chatbot;

public class MessageModel {
    private String cnt;

    public MessageModel(String cnt) {
        this.cnt = cnt;
    }

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }
}