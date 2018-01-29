import java.io.UnsupportedEncodingException;
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

        public String hash(String value) {
            return innerHash( value );
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
        return "MD5Hash - Version 1.0";
    }

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
}