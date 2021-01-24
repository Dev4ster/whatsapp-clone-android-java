package com.victormenezes.whatsapp.model;

public class Message {

    public Message() {
    }

    private String idUser;



    private String message;

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
