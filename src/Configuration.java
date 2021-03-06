import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public enum Configuration {
    instance;

    public String userDirectory = System.getProperty("user.dir");
    public String fileSeparator = System.getProperty("file.separator");
    public String pathToJar = userDirectory + fileSeparator + getHashType() + fileSeparator + "jar" + fileSeparator + "component.jar";

    public HashType getHashType() {
        try {
            Properties properties = new Properties();
            FileInputStream fileInputStream = new FileInputStream(userDirectory + fileSeparator + "hash.props");
            properties.load(fileInputStream);
            fileInputStream.close();
            if (properties.getProperty("hashType").equals("md5hash"))
                return HashType.md5hash;
            else if (properties.getProperty("hashType").equals("sha256hash"))
                return HashType.sha256hash;
            else
                return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public void setHashType( HashType type) {
        try {
            Properties properties = new Properties();
            FileInputStream fileInputStream = new FileInputStream(userDirectory + fileSeparator + "hash.props");
            properties.load(fileInputStream);
            fileInputStream.close();
            properties.setProperty("hashType", type.toString());
            properties.store(new FileOutputStream(userDirectory + fileSeparator + "hash.props"), null);
            this.pathToJar = userDirectory + fileSeparator + getHashType() + fileSeparator + "jar" + fileSeparator + "component.jar";
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}