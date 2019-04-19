package hello;

public class RoomMessage {
    private String roomNumber;
    private String roomMessage;

    public RoomMessage() { }

    public RoomMessage(String roomNumber, String roomMessage) {
        this.roomNumber = roomNumber;
        this.roomMessage = roomMessage;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomMessage() {
        return roomMessage;
    }

    public void setRoomMessage(String roomMessage) {
        this.roomMessage = roomMessage;
    }
}
