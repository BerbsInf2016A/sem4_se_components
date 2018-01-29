import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class Application {
    private Object port;

    @SuppressWarnings({"rawtypes","unchecked"})
    public void createHashPortInstance() {
        Object classInstance;

        try {
            System.out.println("pathToJar : " + Configuration.instance.pathToJar);
            URL[] urls = {new File(Configuration.instance.pathToJar).toURI().toURL()};
            URLClassLoader urlClassLoader = new URLClassLoader(urls,Application.class.getClassLoader());
            Class createdClass = Class.forName("Hash",true,urlClassLoader);
            System.out.println("classInstance     : " + createdClass.toString());

            classInstance = createdClass.getMethod("getInstance",new Class[0]).invoke(null,new Object[0]);
            port = createdClass.getDeclaredField("port").get(classInstance);
            System.out.println("port      : " + port.hashCode());

            Method getVersion = port.getClass().getMethod("getVersion");
            String version = (String)getVersion.invoke(port);
            System.out.println("version   : " + version);
        } catch (Exception e) {
            System.out.println("--- exception");
            System.out.println(e.getMessage());
        }
    }

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

    private String callMethod(String operation) {
        String result = "";

        try {
            Method method = port.getClass().getMethod(operation);
            result = (String)method.invoke(port);
        } catch (Exception e) {
            System.out.println("operation " + operation + " not supported.");
        }

        return result;
    }



    public static void main(String... args) {
        Application application = new Application();
        // Create last used component.
        application.createHashPortInstance();

        boolean run = true;
        while (run){
           application.handleUserInput();

            run = !application.askForEndOfExecution();
        }
    }

    private String executeHash(String value) {
        return this.callMethod("hash", value);
    }

    private boolean askForEndOfExecution() {
        return false;
    }

    private void handleUserInput() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("What do you want to do?");
        System.out.println("Enter \"show components\", \"show current component\", \"set current component  <name> [md5hash or sha256hash]\", \"execute <data> [string]");

        try {
            String argument = br.readLine();
            if (argument.equals("show components")) {
                for(String componentInformation : this.getComponents()) {
                    System.out.printf(" %s ", componentInformation);
                }
                System.out.println();
            } else if (argument.equals("show current component")) {
                System.out.println(this.getCurrentComponentInformation());
            } else if (argument.startsWith("set current component")) {
                argument = argument.replace("set current component ", "");
                HashType hashType = HashType.valueOf(argument.toLowerCase());
                this.updateConfigAndComponent(hashType);
            } else if (argument.startsWith("execute")) {
                argument = argument.replace("execute ", "");
                System.out.println("Result: " + this.executeHash(argument));
            }
        } catch (IOException e) {
            System.out.println("Wrong input!");
        }
    }

    /**
     * Get the components.
     *
     * @return returns a list of components.
     */
    private List<String> getComponents() {
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
    private String getCurrentComponentInformation() {
        return this.callMethod("getVersion");
    }

    /**
     * Updates the property file and reloads the component, if necessary.
     *
     * @param hashType The hash type to set.
     */
    private void updateConfigAndComponent(HashType hashType) {
        if(!(Configuration.instance.getHashType() == hashType)){
            Configuration.instance.setHashType(hashType);
            this.createHashPortInstance();
        }
    }




}