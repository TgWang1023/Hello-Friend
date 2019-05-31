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
    private JoinRequest emptyJoin;
    private JoinRequest noNameJoin;
    private JoinRequest fullJoinCreate;
    private JoinRequest fullJoinJoin;
    private JoinRequest fullJoin;
    private JoinRequest removeJoin;
    private JoinRequest removeJoin2;
    private JoinRequest removeJoin3;
    private JoinRequest removeJoin4;

     @Before public void setUp() {
	 testJoin= new JoinRequest("Derek", "cs32", "English", 1);
	 testJoin2=new JoinRequest("Karly", "cs32", "English", 0);
	 testJoin3=new JoinRequest("Amanda", "cs56", "English", 1);
	 testJoin4=new JoinRequest("Denise", "cs56", "English", 1);
	 emptyJoin=new JoinRequest("Dennis", "cs64", "", 0);
	 noNameJoin=new JoinRequest("Dean", "COMM 1", "English", 0);
	 fullJoinCreate=new JoinRequest("Kenji", "cs8", "English", 1);
	 fullJoinJoin=new JoinRequest("Colette", "cs8", "English", 0);
	 fullJoin=new JoinRequest("Bear", "cs8", "English", 0);
	 removeJoin=new JoinRequest("Sweetie", "dogs", "English", 1);
	 removeJoin2=new JoinRequest("Shadow", "dogs", "English", 0);

	 removeJoin3=new JoinRequest("Salem", "cats", "English", 0);
	 removeJoin4=new JoinRequest("Cactus", "cats", "English", 0);
     }
    
    @Test
    public void test_createRoom() {
	assertEquals(10, testManager.createRoom(testJoin,"33"));
    }

    @Test
    public void test_createRoomEmpty() {
	assertEquals(1, testManager.createRoom(emptyJoin,"15"));
    }

    
    @Test
    public void test_removeUser() {
    testManager.createRoom(removeJoin,"7/999");
    testManager.joinRoom(removeJoin2,"8/878");
    testManager.removeUser("8","878");
    assertEquals(20, testManager.joinRoom(removeJoin2,"8/999"));
    }

    @Test
    public void test_removeUser2() {
    testManager.createRoom(removeJoin3,"6/888");
    testManager.removeUser("6","888");
    assertEquals(21, testManager.joinRoom(removeJoin4,"7/838"));
    }
     
    @Test
    public void test_findUserName() {
    testManager.createRoom(testJoin4,"11");
    testManager.createRoom(testJoin3,"55");
    assertEquals("Denise", testManager.findUserName("11"));
    }
    @Test
    public void test_joinRoom() {
	assertEquals(20, testManager.joinRoom(testJoin2,"53"));
    }

    @Test
    public void test_joinRoomEmpty() {
	assertEquals(1, testManager.joinRoom(emptyJoin,"53"));
    }
    @Test
    public void test_joinRoomNoName() {
	assertEquals(21, testManager.joinRoom(noNameJoin,"365"));
    }

    @Test
    public void test_joinFullRoom() {
	testManager.createRoom(fullJoinCreate, "66");
	testManager.joinRoom(fullJoinJoin,"53");
	assertEquals(22, testManager.joinRoom(fullJoin,"35"));
    }
}
