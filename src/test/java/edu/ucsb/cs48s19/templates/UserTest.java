package edu.ucsb.cs48s19.templates;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


import org.junit.Test;
import org.junit.Before;

public class UserTest {

    private User testUser;
    private User secondTestUser;

     @Before public void setUp() {
	 testUser= new User("Worf", "Klingonee","47");
     }
    @Before public void copy() {
	 secondTestUser = new User();
	 secondTestUser.setName("Kurn");
	 secondTestUser.setLanguage("Klingonee");
	 secondTestUser.setSessionId("47");
    }
    
    @Test
    public void test_getName() {
	assertEquals("Worf", testUser.getName());
    }

    @Test
    public void test_setName() {
	assertEquals("Kurn", secondTestUser.getName());
    }

    @Test
    public void test_setLanguage() {
	assertEquals("Klingonee", secondTestUser.getLanguage());
    }

    @Test
    public void test_setSessionID() {
    	assertEquals("47", secondTestUser.getSessionId());
    }

    @Test
      public void test_hasSessionID() {
    	assertEquals(true, testUser.hasSessionId("47"));
    }

    @Test
      public void test_hasSessionID2() {
    	assertEquals(false, testUser.hasSessionId("24"));
    }

    @Test
    public void test_toString() {
	assertEquals("User{name='Worf\', language='Klingonee\', sessionId='47\'}", testUser.toString());
    }
}
