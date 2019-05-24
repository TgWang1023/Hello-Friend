package edu.ucsb.cs48s19.templates;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


import org.junit.Test;
import org.junit.Before;

public class PairTest {

    private Pair testPair;
    private User testUser;
    private AdvancedMessage testMessage;
    private Pair secondTestPair;

     @Before public void setUp() {
	 testUser= new User("Mark", "English", "810");
	 testMessage= new AdvancedMessage("Hello",true, 10, "Billy", true);
	 testPair= new Pair(testUser, testMessage);
     }

    @Test
    public void test_toString() {
	assertEquals("{ receiver='User{name='Mark\', language='English\', sessionId='810\'}', message='{ content='Hello', systemFlag='true', infoCode='10', sender='Billy', toReceiver='true'}'}", testPair.toString());
    }
}
