package edu.ucsb.cs48s19.templates;

import java.util.Objects;

public class AdvancedMessage {

    private String content;
    private boolean systemFlag;
    private int infoCode;
    private String sender;
    private boolean toReceiver;

    public AdvancedMessage() {
    }

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
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSystemFlag() {
        return this.systemFlag;
    }

    public boolean getSystemFlag() {
        return this.systemFlag;
    }

    public void setSystemFlag(boolean systemFlag) {
        this.systemFlag = systemFlag;
    }

    public int getInfoCode() {
        return this.infoCode;
    }

    public void setInfoCode(int infoCode) {
        this.infoCode = infoCode;
    }

    public String getSender() {
        return this.sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public boolean isToReceiver() {
        return this.toReceiver;
    }

    public boolean getToReceiver() {
        return this.toReceiver;
    }

    public void setToReceiver(boolean toReceiver) {
        this.toReceiver = toReceiver;
    }

    public AdvancedMessage content(String content) {
        this.content = content;
        return this;
    }

    public AdvancedMessage systemFlag(boolean systemFlag) {
        this.systemFlag = systemFlag;
        return this;
    }

    public AdvancedMessage infoCode(int infoCode) {
        this.infoCode = infoCode;
        return this;
    }

    public AdvancedMessage sender(String sender) {
        this.sender = sender;
        return this;
    }

    public AdvancedMessage toReceiver(boolean toReceiver) {
        this.toReceiver = toReceiver;
        return this;
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
