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
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("What do you want to do?");
            System.out.println("Enter \"show components\", \"show current component\", \"set current component  <name> [md5hash or sha256hash]\", \"execute <data> [string]\", \"quit\"");

            try {
                String argument = br.readLine();
                run = application.handleUserInput(argument);
                System.out.println();
            } catch (Exception e) {
                System.out.println("Wrong input!");
            }
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
    public boolean handleUserInput(String argument) {
        // Handle show components.
        if (argument.trim().toLowerCase().equals("show components")) {
            List<String> components = this.getComponents();
            for (int i = 0; i < components.size(); i++) {
                String componentInformation = components.get(i);
                System.out.printf("%s ", componentInformation);
                if( i == components.size() - 1) {
                    String separator = System.getProperty("line.separator");
                    System.out.printf("%s ", separator);
                }
            }
            return true;
        }
        // Handle show current component.
        if (argument.trim().toLowerCase().equals("show current component")) {
            System.out.println(this.getCurrentComponentInformation());
            return true;
        }
        // Handle set current component.
        if (argument.toLowerCase().startsWith("set current component")) {
            argument = argument.toLowerCase().replace("set current component", "").trim();
            switch(argument.toLowerCase()) {
                case "md5hash":
                    this.updateConfigAndComponent(HashType.md5hash);
                    System.out.println("Component set!");
                    break;
                case "sha256hash":
                    this.updateConfigAndComponent(HashType.sha256hash);
                    System.out.println("Component set!");
                    break;
                default:
                    System.out.println("No such component found!");
                    break;
            }
            return true;
        }
        // Handle execute command.
        if (argument.toLowerCase().startsWith("execute")) {
            argument = argument.replace("execute", "").trim();
            argument = argument.replace("EXECUTE", "").trim();
            System.out.println("Result: " + this.executeHash(argument));
            return true;
        }
        // Handle quit command.
        if (argument.trim().toLowerCase().equals("quit")) {
            return false;
        }

        // Handle unknown command.
        System.out.println("Unknown command!");
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