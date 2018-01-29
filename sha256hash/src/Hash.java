import javax.xml.bind.DatatypeConverter;
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

        public String getVersion() {
            return innerGetVersion();
        }

        public String hash(String hash) {
            return null;
        }
    }

    public String innerGetVersion() {
        return "SHA256Hash - Version 1.0";
    }

    // Copied from: https://www.quickprogrammingtips.com/java/how-to-generate-sha256-hash-in-java.html
    private String innerHash(String value){
        String result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes("UTF-8"));
            return bytesToHex(hash); // make it printable
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    // Copied from: https://www.quickprogrammingtips.com/java/how-to-generate-sha256-hash-in-java.html
    private String  bytesToHex(byte[] hash) {
        return DatatypeConverter.printHexBinary(hash);
    }

}