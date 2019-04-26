package edu.ucsb.cs48s19.templates;

import static org.junit.Assert.assertEquals;


import org.junit.Test;
import org.junit.Before;

public class MessageTest {

    private Message testMessage;

     @Before public void setUp() {
	 testMessage = new Message("this is a test");
     }    
    
    @Test
    public void test_getContent() {
	assertEquals("this is NOT a test", testMessage.getContent());
    }

}
    
