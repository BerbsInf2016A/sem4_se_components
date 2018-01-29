import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ApplicationTests {
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
