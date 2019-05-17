package edu.ucsb.cs48s19.templates;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


import org.junit.Test;
import org.junit.Before;

public class AdvancedMessageTest {

    private AdvancedMessage testAMessage;
    private AdvancedMessage copyAMessage;
    private User notMessage;

     @Before public void setUp() {
	 testAMessage = new AdvancedMessage("this is content", true, 1, "Derek", true);
	 copyAMessage = new AdvancedMessage();
    }
    
    @Test
    public void test_getContent() {
	assertEquals("this is content", testAMessage.getContent());
    }

    @Test
    public void test_getContent2() {
	testAMessage.setContent("this is still content");
	assertEquals("this is still content", testAMessage.getContent());
    }

    @Test
    public void test_isSystemFlag() {
    	assertEquals(true, testAMessage.isSystemFlag());
    }

    @Test
    public void test_isSystemFlag2() {
	testAMessage.setSystemFlag(false);
    	assertEquals(false, testAMessage.isSystemFlag());
    }

    @Test
    public void test_setInfoCode() {
	testAMessage.setInfoCode(11);
    	assertEquals(11, testAMessage.getInfoCode());
    }

    @Test
    public void test_setSender() {
	testAMessage.setSender("Dean");
    	assertEquals("Dean", testAMessage.getSender());
    }

    @Test
    public void test_setToReceiver() {
    	assertEquals(true, testAMessage.isToReceiver());
    }

    @Test
    public void test_setToReceiver2() {
	testAMessage.setToReceiver(false);
    	assertEquals(false, testAMessage.isToReceiver());
    }

    @Test
    public void test_equals() {
	assertEquals(true, testAMessage.equals(testAMessage));
    }

    @Test
    public void test_equals2() {
	assertEquals(false, testAMessage.equals(notMessage));
    }

    @Test
    public void test_equals3() {
	copyAMessage.setContent("this is content");
	copyAMessage.setSystemFlag(true);
	copyAMessage.setInfoCode(1);
	copyAMessage.setSender("Derek");
	copyAMessage.setToReceiver(true);
	assertEquals(true, testAMessage.equals(copyAMessage));
    }

    @Test
    public void test_toString() {
    assertEquals("{ content='this is content', systemFlag='true', infoCode='1', sender='Derek', toReceiver='true'}", testAMessage.toString());
    }
}
    
