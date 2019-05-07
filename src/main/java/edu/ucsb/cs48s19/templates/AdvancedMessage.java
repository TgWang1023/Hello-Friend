package edu.ucsb.cs48s19.templates;

import java.util.Objects;

public class AdvancedMessage {

    private String content;
    private boolean systemFlag;
    private int infoCode;
    private String sender;
    private boolean toReceiver;

    public AdvancedMessage() { }

    public AdvancedMessage(
            String content, 
            boolean systemFlag, 
            int infoCode, 
            String sender, 
            boolean toReceiver) {
        this.content = content;
        this.systemFlag = systemFlag;
        this.infoCode = infoCode;
        this.sender = sender;
        this.toReceiver = toReceiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSystemFlag() {
        return systemFlag;
    }

    public void setSystemFlag(boolean systemFlag) {
        this.systemFlag = systemFlag;
    }

    public int getInfoCode() {
        return infoCode;
    }

    public void setInfoCode(int infoCode) {
        this.infoCode = infoCode;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public boolean isToReceiver() {
        return toReceiver;
    }

    public void setToReceiver(boolean toReceiver) {
        this.toReceiver = toReceiver;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof AdvancedMessage)) {
            return false;
        }
        AdvancedMessage advancedMessage = (AdvancedMessage) o;
        return Objects.equals(content, advancedMessage.content) && systemFlag == advancedMessage.systemFlag && infoCode == advancedMessage.infoCode && Objects.equals(sender, advancedMessage.sender) && toReceiver == advancedMessage.toReceiver;
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, systemFlag, infoCode, sender, toReceiver);
    }

    @Override
    public String toString() {
        return "{" +
            " content='" + getContent() + "'" +
            ", systemFlag='" + isSystemFlag() + "'" +
            ", infoCode='" + getInfoCode() + "'" +
            ", sender='" + getSender() + "'" +
            ", toReceiver='" + isToReceiver() + "'" +
            "}";
    }

}
