package edu.ucsb.cs48s19.templates;

public class JoinRequest {
    
    // CONSTANTS
    public static final int CREATE_REQUEST = 1;
    public static final int JOIN_REQUEST = 0;


    private String userName;
    private String roomName;
    private String userLanguage;
    private int request;

    public JoinRequest() { }

    public JoinRequest(String userName, String roomName, String userLanguage, int request) {
        this.userName = userName;
        this.roomName = roomName;
        this.userLanguage = userLanguage;
        this.request = request;
    }

    public String getUserName() {
        return userName;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getUserLanguage() {
        return userLanguage;
    }

    public int getRequest() {
        return request;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setUserLanguage(String userLanguage) {
        this.userLanguage = userLanguage;
    }

    public void setRequest(int request) {
        this.request = request;
    }

    public boolean hasEmptyEntry() {
        return (this.userName == null || this.userName.compareTo("") == 0 ||
                this.roomName == null || this.roomName.compareTo("") == 0 ||
                this.userLanguage == null || this.userLanguage.compareTo("") == 0);
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
