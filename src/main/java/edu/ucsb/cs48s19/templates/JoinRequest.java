package edu.ucsb.cs48s19.templates;

public class JoinRequest {
    private String userName;
    private String roomName;
    private String userLanguage;
    private int request;

    public JoinRequest() { }

    public JoinRequest(String userName, String roomName, String userLanguage) {
        this.userName = userName;
        this.roomName = roomName;
        this.userLanguage = userLanguage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getUserLanguage() {
        return userLanguage;
    }

    public void setUserLanguage(String userLanguage) {
        this.userLanguage = userLanguage;
    }

    public int getRequest() {
        return request;
    }

    public void setRequest(int request) {
        this.request = request;
    }

    @Override
    public String toString() {
        return "JoinRequest{" +
                "userName='" + userName + '\'' +
                ", roomName='" + roomName + '\'' +
                ", userLanguage='" + userLanguage + '\'' +
                ", request=" + request +
                '}';
    }
}
