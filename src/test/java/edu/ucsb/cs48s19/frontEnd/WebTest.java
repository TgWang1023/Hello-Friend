package package edu.ucsb.cs48s19.frontEnd;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.concurrent.TimeUnit;

//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;


public class WebTest {
    //the web url to test
    private static String url="http://localhost:8080/";
    private static WebDriver driver1;
    private static WebDriver driver2;
    private static WebDriver driver3;


    private static String prj_path="../../../../../../../";
    private static String chrome_driver=prj_path+"tools/chromedriver";


    public WebElement connect(WebDriver dr){
        //find the connect button id
        WebElement connect_btn=dr.findElement(By.id("connect"));
        //click the connect button
        connect_btn.click();
        //check if the room form is shown on the page
        WebElement element=dr.findElement(By.id("room_form"));


        return element;
    }

    public String getLastMessage(WebDriver dr){
        WebElement ul=dr.findElement(By.id("conversation"));
        List<WebElement> messages=ul.findElements(By.tagName("li"));
        String sys_msg=messages.get(messages.size()-1).getText();
        return sys_msg;

    }



    public String create_room(WebDriver dr,String user_name,String user_language,String room_name){
        //Switch to the create tab section
        WebElement tab_section=dr.findElement(By.id("create_tab"));
        tab_section.click();
        //filling out the form
        //filling your name
        WebElement name = dr.findElement(By.name("name"));
        name.sendKeys(user_name);
        //select the language
        Select select = new Select(dr.findElement(By.id("user_lang")));
        select.selectByValue(user_language);
        //filling the room name
        WebElement rm=dr.findElement(By.name("roomN"));
        rm.sendKeys(room_name);

        //submit the form using submit button
        dr.findElement(By.id("create_room")).click();

        //waiting for the chat section to be shown

//        WebDriverWait wait = new WebDriverWait(dr, 30);
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("chat_section")));

        //get the system message
        String sys_msg=getLastMessage(dr);

        return sys_msg;

    }

    public String join_room(WebDriver dr,String user_name, String user_language,String room_name){
        //Switch to the join tab section
        WebElement tab_section=dr.findElement(By.id("join_tab"));
        tab_section.click();
        WebElement join_section=dr.findElement(By.id("join_room_section"));
        //filling out the form
        //filling your name
        WebElement name = join_section.findElement(By.name("name"));
        name.sendKeys(user_name);
        //select the language
        Select select = new Select(join_section.findElement(By.id("join_user_lang")));
        select.selectByValue(user_language);
        //filling the room name
        WebElement rm=join_section.findElement(By.name("roomN"));
        rm.sendKeys(room_name);

        //submit the form using submit button
        join_section.findElement(By.id("join_room")).click();

        //waiting for the chat section to be shown

//        WebDriverWait wait = new WebDriverWait(dr, 30);
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("chat_section")));

        //get the system message
        String sys_msg=getLastMessage(dr);

        return sys_msg;

    }

    public void enterInput(WebDriver dr,String args){
        WebElement chat_box=dr.findElement(By.id("room_msg"));
        chat_box.sendKeys(args);
        WebElement btn=dr.findElement(By.id("send_msg"));
        btn.click();
    }


    public void disconnect(WebDriver dr){
        //find the disconnect button id
        WebElement disconnect_btn=dr.findElement(By.id("disconnect"));
        //click the disconnect button
        disconnect_btn.click();
    }

    public void quit(WebDriver dr){
        dr.quit();
    }

//    public void slp(){
//        TimeUnit.SECONDS.sleep(10);
//    }

    public void disconnect_quit(WebDriver dr){
        //find the disconnect button id
        WebElement disconnect_btn=dr.findElement(By.id("disconnect"));
        //click the disconnect button
        disconnect_btn.click();
        dr.quit();
    }

    public WebDriver create_driver_connect(){
        WebDriver driver=new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(WebTest.url);
        connect(driver);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        return driver;

    }


    @BeforeClass
    public static void setup(){

        System.out.println("Welcome to the hello friend web test");

        //Optional:finding the chrome driver location
        System.setProperty("webdriver.chrome.driver",chrome_driver);



    }
    @Test
    public void connect_3_usr(){
        WebDriver driver=create_driver_connect();
        Assert.assertNotNull(connect(driver));
        disconnect_quit(driver);
    }

    @Test
    public void create_room_normal(){
        //test for English user
        WebDriver driver1=create_driver_connect();
        Assert.assertEquals("Create success.",create_room(driver1,"testUser1","en","room"));
        disconnect_quit(driver1);

    }

    @Test
    public void create_room_missing_input(){
        WebDriver driver1=create_driver_connect();
        Assert.assertEquals("Error: Please fill all entries in the form.",create_room(driver1,"","en",""));
        disconnect_quit(driver1);

    }

    @Test
    public void create_existing_room(){
        WebDriver driver1=create_driver_connect();
        WebDriver driver2=create_driver_connect();
        //first create a room using driver 1
        create_room(driver1,"testUser1","en","room1");
        //then create another room with the same name
        Assert.assertEquals("Create failed. This room name has been occupied.",create_room(driver2,"testUser2","en","room1"));
        disconnect_quit(driver1);
        disconnect_quit(driver2);

    }

    @Test
    public void join_non_existing_room(){
        WebDriver driver1=create_driver_connect();
        Assert.assertEquals("Join failed. The room with the name doesn't exist.",join_room(driver1,"testUser1","en","non-existing"));
        disconnect_quit(driver1);
    }

    @Test
    public void join_existing_room(){
        WebDriver driver1=create_driver_connect();
        WebDriver driver2=create_driver_connect();
        create_room(driver1,"testUser1","en","room1");
        Assert.assertEquals("Join success.",join_room(driver2,"testUser2","en","room1"));
        disconnect_quit(driver1);
        disconnect_quit(driver2);


    }

    @Test
    public void third_user_join(){
        WebDriver driver1=create_driver_connect();
        WebDriver driver2=create_driver_connect();
        WebDriver driver3=create_driver_connect();
        create_room(driver1,"testUser1","en","room1");
        join_room(driver2,"testUser2","en","room1");
        Assert.assertEquals("Join failed. The room is full.",join_room(driver3,"testUser3","en","room1"));
        disconnect_quit(driver1);
        disconnect_quit(driver2);
        disconnect_quit(driver3);


    }

    @Test
    public void leave_room(){
        WebDriver driver1=create_driver_connect();
        WebDriver driver2=create_driver_connect();
        create_room(driver1,"testUser1","en","room1");
        join_room(driver2,"testUser2","en","room1");
        disconnect_quit(driver2);
        Assert.assertEquals("Another user has disconnected.",getLastMessage(driver1));
        disconnect_quit(driver1);

    }


    @AfterClass
    public static void close(){
        System.out.println("End of Test");
    }




}
