package edu.ucsb.cs48s19.templates;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


import org.junit.Test;
import org.junit.Before;

public class JoinTest {

    private JoinRequest testJoin;
    private JoinRequest secondTestJoin;

     @Before public void setUp() {
	 testJoin = new JoinRequest("Derek Bang", "test room", "Klingonee", 47);
     }
    @Before public void create() {
	 secondTestJoin = new JoinRequest();
	 secondTestJoin.setUserName("Derek Bang");
    }
    
    @Test
    public void test_getUserName() {
	assertEquals("Derek Bang", secondTestJoin.getUserName());
    }

    @Test
    public void test_getUserName2() {
	assertEquals(testJoin.getUserName(), secondTestJoin.getUserName());
    }

    @Test
    public void test_toString2() {
	assertEquals("JoinRequest{userName='Derek Bang\', roomName='test room\', userLanguage='Klingonee\', request=47}", testJoin.toString());
    }
}
