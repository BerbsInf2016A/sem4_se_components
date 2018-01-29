import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
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
/*
    public int execute(int a,int b,String operation) {
        int result = Integer.MAX_VALUE;

        try {
            Method method = port.getClass().getMethod(operation,int.class,int.class);
            result = (Integer)method.invoke(port,a,b);
        } catch (Exception e) {
            System.out.println("operation " + operation + " not supported.");
        }

        return result;
    }
*/

    public static void main(String... args) {
        Application application = new Application();
        // Create last used component.
        application.createHashPortInstance();

        boolean run = true;
        while (run){
            String value = application.handleUserInput();

            application.executeHash(value);

            run = !application.askForEndOfExecution();
        }


        application.createHashPortInstance();
    }

    private void executeHash(String value) {
        //String hashValeu = port.execut....
    }

    private boolean askForEndOfExecution() {
        return true;
    }

    private String handleUserInput() {
        System.out.println("What do you want to do?");
        System.out.println("Enter \"show components\", \"show current component\", \"set current component <name> [md5 or sha256]\", \"execute <data> [string]");

        /*

        switch

        Update config.... And load component



         */
        this.updateConfigAndComponent(requestedHashType);
        this.executeHash(value);
        String componentInfomration = this.getCurrentComponentInformation();
        List<String> componentInformations = this.getComponents();

    }

    private void updateConfigAndComponent(HashType hashType) {
    }
}