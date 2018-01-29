import java.lang.reflect.Method;

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
            return innergetVersion();
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
    public String innergetVersion() {
        return "MD5Hash - Version 1.0";
    }

    private String innerHash(String value){

        // TODO: Add Function
        return "";
    }
}