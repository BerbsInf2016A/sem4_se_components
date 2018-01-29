import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

public class ApplicationTests {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(System.out);
        System.setErr(System.err);
    }

    @Test
    public void Application_HandleUserInput_ShowComponents() {
        Application testee = new Application();

        testee.handleUserInput("show components");

        Assert.assertEquals("md5hash sha256hash", outContent.toString().trim());
    }

    @Test
    public void Application_HandleUserInput_setCurrentComponentMD5() {
        Application testee = new Application();

        testee.updateConfigAndComponent(HashType.sha256hash);
        testee.createHashPortInstance();

        testee.handleUserInput("set current component md5hash");

        String currentComponent = testee.getCurrentComponentInformation();

        Assert.assertEquals("MD5Hash should be set","MD5Hash - Version 1.0", currentComponent);
    }

    @Test
    public void Application_HandleUserInput_setCurrentComponentSHA256() {
        Application testee = new Application();

        testee.updateConfigAndComponent(HashType.md5hash);
        testee.createHashPortInstance();

        testee.handleUserInput("set current component sha256hash");

        String currentComponent = testee.getCurrentComponentInformation();

        Assert.assertEquals("MD5Hash should be set","SHA256Hash - Version 1.0", currentComponent);
    }

    @Test
    public void Application_HandleUserInput_setCurrentComponent_InvalidValue() {
        Application testee = new Application();

        testee.handleUserInput("set current component invalid");

        Assert.assertEquals("Wong input should be messaged.", "No such component found!", outContent.toString().trim());
    }

    @Test
    public void Application_HandleUserInput_UnknownCommand() {
        Application testee = new Application();

        testee.handleUserInput("test");

        Assert.assertEquals("Wong input should be messaged.", "Unknown command!", outContent.toString().trim());
    }

    @Test
    public void Application_LoadMD5HashComponent() {

        Application testee = new Application();

        // Set the md5hash as current component.
        testee.updateConfigAndComponent(HashType.md5hash);
        testee.createHashPortInstance();


        String result = testee.getCurrentComponentInformation();

        Assert.assertEquals( "MD5Hash should be loaded","MD5Hash - Version 1.0", result);
    }

    @Test
    public void Application_LoadSHA256HashComponent() {
        Application testee = new Application();

        // Set the md5hash as current component.
        testee.updateConfigAndComponent(HashType.sha256hash);
        testee.createHashPortInstance();


        String result = testee.getCurrentComponentInformation();

        Assert.assertEquals( "SHA256Hash should be loaded","SHA256Hash - Version 1.0", result);
    }

    @Test
    public void Application_GetComponents() {
        Application testee = new Application();

        List<String> result = testee.getComponents();

        Assert.assertEquals("Should return 2 values", 2, result.size());
        Assert.assertEquals("Should be MD5", HashType.md5hash.toString().toLowerCase(), result.get(0).toLowerCase());
        Assert.assertEquals("Should be SHA256", HashType.sha256hash.toString().toLowerCase(), result.get(1).toLowerCase());
    }


    @Test
    public void Application_MD5Hash_ExecuteHash() {

        Application testee = new Application();

        // Set the md5hash as current component.
        testee.updateConfigAndComponent(HashType.md5hash);
        testee.createHashPortInstance();


        String result = testee.executeHash("testhashvalue");

        Assert.assertEquals( "MD5Hash should be correct","785BDA25E5F90837DD11517C38B80067".toLowerCase(), result.toLowerCase());
    }

    @Test
    public void Application_SHA256Hash_ExecuteHash() {

        Application testee = new Application();

        // Set the md5hash as current component.
        testee.updateConfigAndComponent(HashType.sha256hash);
        testee.createHashPortInstance();


        String result = testee.executeHash("testhashvalue");

        Assert.assertEquals( "SHA256Hash should be correct","9DADF68D7CF63135232D6F0DB1B343B8161987B3AC51E835E8E6A660C290DD84".toLowerCase(), result.toLowerCase());
    }

}
