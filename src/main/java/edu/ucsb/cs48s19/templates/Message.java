package edu.ucsb.cs48s19.templates;

public class Message {

    private String content;

    public Message() { }

    public Message(Message that) {
        if (that == null) { return; }
        this.content = that.content;
    }

    public Message(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                '}';
    }
}