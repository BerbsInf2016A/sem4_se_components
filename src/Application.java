import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class Application {
    private Object port;

    /**
     * Main method for the application.
     * @param args
     */
    public static void main(String... args) {
        Application application = new Application();
        // Create last used component.
        application.createHashPortInstance();

        boolean run = true;
        while (run){
           run = application.handleUserInput();
        }
    }

    /**
     * Creates a port instance for the hash class.
     */
    @SuppressWarnings({"rawtypes","unchecked"})
    public void createHashPortInstance() {
        Object classInstance;

        try {
            URL[] urls = {new File(Configuration.instance.pathToJar).toURI().toURL()};
            URLClassLoader urlClassLoader = new URLClassLoader(urls,Application.class.getClassLoader());
            Class createdClass = Class.forName("Hash",true,urlClassLoader);

            classInstance = createdClass.getMethod("getInstance",new Class[0]).invoke(null);
            port = createdClass.getDeclaredField("port").get(classInstance);

        } catch (Exception e) {
            System.out.println("--- exception");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Calls a method on the port.
     *
     * @param operation The method name.
     * @param value The parameter value.
     * @return Return value of the called method.
     */
    public String callMethod(String operation, String value) {
        String result = "";

        try {
            Method method = port.getClass().getMethod(operation,String.class);
            result = (String)method.invoke(port, value);
        } catch (Exception e) {
            System.out.println("operation " + operation + " not supported.");
        }

        return result;
    }

    /**
     * Call a method without parmameters.
     *
     * @param operation The name of the method to call.
     * @return The return value of the called method.
     */
    public String callMethod(String operation) {
        String result = "";

        try {
            Method method = port.getClass().getMethod(operation);
            result = (String)method.invoke(port);
        } catch (Exception e) {
            System.out.println("operation " + operation + " not supported.");
        }

        return result;
    }

    /**
     * Execute the hash function.
     *
     * @param value The value which should be hashed.
     * @return The hashed value.
     */
    public String executeHash(String value) {
        return this.callMethod("hash", value);
    }

    /**
     * Handles the user input.
     */
    private boolean handleUserInput() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("What do you want to do?");
        System.out.println("Enter \"show components\", \"show current component\", \"set current component  <name> [md5hash or sha256hash]\", \"execute <data> [string]\", \"quit");

        try {
            String argument = br.readLine();
            if (argument.trim().toLowerCase().equals("show components")) {
                for(String componentInformation : this.getComponents()) {
                    System.out.printf("%s ", componentInformation);
                }
                System.out.println();
            } else if (argument.trim().toLowerCase().equals("show current component")) {
                System.out.println(this.getCurrentComponentInformation());
            } else if (argument.toLowerCase().startsWith("set current component")) {
                argument = argument.replace("set current component", "").trim();
                switch(argument.toLowerCase()) {
                    case "mda5hash":
                        this.updateConfigAndComponent(HashType.md5hash);
                        break;
                    case "sha256hash":
                        this.updateConfigAndComponent(HashType.sha256hash);
                        break;
                    default:
                        System.out.println("No such Component found!");
                        break;
                }
            } else if (argument.toLowerCase().startsWith("execute")) {
                argument = argument.replace("execute", "").trim();
                System.out.println("Result: " + this.executeHash(argument));
            } else if (argument.trim().toLowerCase().equals("quit")) {
                return false;
            } else {
                System.out.println("Wrong input!");
            }
        } catch (Exception e) {
            System.out.println("Wrong input!");
        }
        return true;
    }

    /**
     * Get the components.
     *
     * @return returns a list of components.
     */
    public List<String> getComponents() {
        List<String> components = new ArrayList<>();
        for ( HashType component :  HashType.values()) {
            components.add(component.toString());
        }
        return components;
    }

    /**
     * Gets the current component information.
     *
     * @return The version of the current component.
     */
    public String getCurrentComponentInformation() {
        return this.callMethod("getVersion");
    }

    /**
     * Updates the property file and reloads the component, if necessary.
     *
     * @param hashType The hash type to set.
     */
    public void updateConfigAndComponent(HashType hashType) {
        if(!(Configuration.instance.getHashType() == hashType)){
            Configuration.instance.setHashType(hashType);
            this.createHashPortInstance();
        }
    }

}