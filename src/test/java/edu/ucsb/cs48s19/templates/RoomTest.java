package edu.ucsb.cs48s19.templates;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


import org.junit.Test;
import org.junit.Before;

public class RoomTest {

    private Room testRoom;
    private User testUser;
    private User testUserTwo;

     @Before public void setUp() {
	 testUser=  new User("Picard", "Tamarian","47");
	 testRoom= new Room("Tanagra", testUser);
	 testUserTwo=  new User("Dathon", "Tamarian","47");
	 
    }
    
    @Test
    public void test_getName() {
	assertEquals("Tanagra", testRoom.getName());
    }

    @Test
    public void test_getOwner() {
	assertEquals(testUser, testRoom.getOwner());
    }

    @Test
    public void test_isFull() {
	assertEquals(false, testRoom.isFull());
    }

    @Test
    public void test_joinUser() {
    	assertEquals(true, testRoom.joinUser(testUserTwo));
    }

    @Test
    public void test_isFull2() {
	assertEquals(true, testRoom.isFull());
	}

    @Test
      public void test_joinUser2() {
    	assertEquals(false, testRoom.joinUser(testUserTwo));
    }
	    
    @Test
    public void test_toString() {
	assertEquals("Room{name='Tanagra\', owner=Picard, joiner=Dathon}", testRoom.toString());
    }
}
