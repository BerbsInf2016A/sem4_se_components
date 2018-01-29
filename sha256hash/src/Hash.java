import javax.xml.bind.DatatypeConverter;
import java.lang.reflect.Method;
import java.security.MessageDigest;

public class Hash {
    private static Hash instance = new Hash();
    public Port port;

    private Hash(){
        this.port = new Port();
    }

    public static Hash getInstance() {
        return instance;
    }

    public class Port implements IHash {

        private Method[] methods = getClass().getMethods();

        public String getVersion() {
            return innerGetVersion();
        }

        public String hash(String hash) {
            return null;
        }

        public void listMethods() {
            System.out.println("--- public methods for " + getClass().getName());
            for (int i = 0; i < methods.length; i++)
                if (!methods[i].toString().contains("Object") && !methods[i].toString().contains("list"))
                    System.out.println(methods[i]);
            System.out.println("---");
        }
    }
    
    public String innerGetVersion() {
        return "SHA256Hash - Version 1.0";
    }

    private String innerHash(String value){
        // Copied from: https://www.quickprogrammingtips.com/java/how-to-generate-sha256-hash-in-java.html
        String result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes("UTF-8"));
            return DatatypeConverter.printHexBinary(hash);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return result;

    }
}