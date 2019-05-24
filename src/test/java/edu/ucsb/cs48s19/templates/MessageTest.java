package edu.ucsb.cs48s19.templates;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


import org.junit.Test;
import org.junit.Before;

public class MessageTest {

    private Message testMessage;
    private Message secondTestMessage;

     @Before public void setUp() {
	 testMessage = new Message("this is a test");
     }
    @Before public void copy() {
	 secondTestMessage = new Message(testMessage);
    }
    
    @Test
    public void test_getContent() {
	assertEquals("this is a test", testMessage.getContent());
    }

    @Test
    public void test_getContent2() {
	assertNotEquals("this is NOT a test", testMessage.getContent());
    }

    @Test
    public void test_MessageCopy() {
    	assertEquals("this is a test", secondTestMessage.getContent());
    }

    @Test
    public void test_toString() {
	assertEquals("Message{content='this is a test\'}", testMessage.toString());
    }
}
    
