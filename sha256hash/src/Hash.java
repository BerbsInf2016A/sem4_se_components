import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

public class Hash {
    /**
     * The hash instance.
     */
    private static Hash instance = new Hash();
    /**
     * The port instance.
     */
    public Port port;

    /**
     * The hash component.
     */
    private Hash(){
        this.port = new Port();
    }

    /**
     * Returns the instance.
     *
     * @return The instance of the component.
     */
    public static Hash getInstance() {
        return instance;
    }

    /**
     * Returns the version string.
     *
     * @return The version string.
     */
    public String innerGetVersion() {
        return "SHA256Hash - Version 1.0";
    }

    /**
     * The method to calculate the hash value.
     *
     * @param input The value to hash.
     * @return The hashed value.
     */
    private String innerHash(String input){
        // Copied from: https://www.quickprogrammingtips.com/java/how-to-generate-sha256-hash-in-java.html
        String result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            return bytesToHex(hash); // make it printable
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * Copied from: https://www.quickprogrammingtips.com/java/how-to-generate-sha256-hash-in-java.html
     * Use javax.xml.bind.DatatypeConverter class in JDK to convert byte array
     * to a hexadecimal string. Note that this generates hexadecimal in upper case.
     * @param hash The hashed value.
     * @return
     */
    private String bytesToHex(byte[] hash) {
        return DatatypeConverter.printHexBinary(hash);
    }

    /**
     * The port for the hash component.
     */
    public class Port implements IHash {

        /**
         * Gets the version of the component.
         *
         * @return The version string.
         */
        public String getVersion() {
            return innerGetVersion();
        }

        /**
         * Calculate the hash value for the parameter.
         *
         * @param value The value to hash.
         * @return The hashed value.
         */
        public String hash(String value) {
            return innerHash(value);
        }
    }
}