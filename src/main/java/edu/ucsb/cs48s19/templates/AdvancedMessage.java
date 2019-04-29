package edu.ucsb.cs48s19.templates;

public class AdvancedMessage {

    private String content;
    private boolean systemFlag;
    private int infoCode;

    public AdvancedMessage() { }

    public AdvancedMessage(String content) {
        this.content = content;
    }

    public AdvancedMessage(int code) {
        this.systemFlag = true;
        this.infoCode = code;
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

    @Override
    public String toString() {
        return "AdvancedMessage{" +
                "content='" + content + '\'' +
                ", systemFlag=" + systemFlag +
                ", infoCode=" + infoCode +
                '}';
    }

}
