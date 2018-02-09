import java.io.UnsupportedEncodingException;
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
        return "MD5Hash - Version 1.0";
    }

    /**
     * The method to calculate the hash value.
     *
     * @param input The value to hash.
     * @return The hashed value.
     */
    private String innerHash(String input){
        // Copied from: http://www.asjava.com/core-java/java-md5-example/
        byte[] source;
        try {
            //Get byte according by specified coding.
            source = input.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            source = input.getBytes();
        }
        String result = null;
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            //The result should be one 128 integer
            byte temp[] = md.digest();
            char str[] = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = temp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            result = new String(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
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
            return innerHash( value );
        }
    }
}