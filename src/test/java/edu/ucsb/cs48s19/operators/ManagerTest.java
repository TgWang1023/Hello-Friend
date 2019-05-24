package edu.ucsb.cs48s19.operators;

import edu.ucsb.cs48s19.templates.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


import org.junit.Test;
import org.junit.Before;

public class ManagerTest {

    private Manager testManager;
    private JoinRequest testJoin;
    private JoinRequest testJoin2;
    private JoinRequest testJoin3;
    private JoinRequest testJoin4;

     @Before public void setUp() {
	 testJoin= new JoinRequest("Derek", "cs32", "English", 0);
	 testJoin2=new JoinRequest("Karly", "cs32", "English", 15);
	 testJoin3=new JoinRequest("Amanda", "cs56", "English", 55);
	 testJoin4=new JoinRequest("Denise", "cs56", "English", 33);
     }
    
    @Test
    public void test_createRoom() {
	assertEquals(11, testManager.createRoom(testJoin,"33"));
    }

    @Test
    public void test_createRoom2() {
	assertEquals(10, testManager.createRoom(testJoin2,"22"));
    }
    @Test
    public void test_createRoom3() {
	assertEquals(10, testManager.createRoom(testJoin3,"55"));
    }
    @Test
    public void test_createRoom4() {
	assertEquals(11, testManager.createRoom(testJoin4,"11"));
    }
    //@Test
    //public void test_removeRoom() {
    //testManager.removeRoom();
    //assertEquals(10, testManager.createRoom(testJoin4,"53"));
    // }
     
    //@Test
    // public void test_findUserName() {
    //	assertEquals("Amanda", testManager.findUserName("55"));
    // }
    
    @Test
    public void test_joinRoom() {
	assertEquals(20, testManager.joinRoom(testJoin2,"53"));
    }

    //public void test_toString() {
    //	assertEquals("User{name='Worf\', language='Klingonee\', sessionId='47\'}", testUser.toString());
    // }
}
