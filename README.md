# Hello-Friend
CS48 Chat Project

## about the frontEnd test instruction
It is a test based upon selenium tools
1. Please make sure that you have installed the latest chrome(version of 75)
Important!!!:The dependent chromedriver is in the folder tools/

2. Please modify the line 30 in test/frontEnd/Webtest.java
    //for mac
    private static String chrome_driver="tools/chromedriver";
    //for linux
    // private static String chrome_driver="tools/chromedriver_linux";
    //for windows
    // private static String chrome_driver="tools/chromedriver.exe";
3. Please launch the local host first
    $mvn spring-boot:run
4. Then do
    $mvn test



