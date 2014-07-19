/**
  *This code belongs to Riverbank Solutions Kenya.
  */
package zizi.family.transaction.env;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Handles management of configurations of ZIZI
 *  Creates configurations and load configuration
 * @author ignatius ojiambo
 */
public class Configurations {
   /**GSON object that will be used during the configuration process**/
    Gson gson = null;
    /**Holds the details of configuration.**/
    ConfigurationBean configuration;
    private final String CONFIGURATION_FILE = "zizi-config.json";
    
    private Configurations() {
        /** create the gson instance.**/
        gson = GSONEngine.getInstance().getConfigGson();
        /** load the configurations**/
        configuration = loadConfigurationsFromFile();
    }
    /**
     * Get the instance for Configuration class
     * @return 
     */
    public static Configurations getInstance() {
        return ConfigurationsHolder.INSTANCE;
    }
    /**
     * Hold the instance of the Configuration class
     */
    private static class ConfigurationsHolder {
        private static final Configurations INSTANCE = new Configurations();
    }
    
    public final ConfigurationBean loadConfigurationsFromFile() {
        /**Reads file**/
        FileReader fr;
        /**Loading the configuration's string**/
        String configurations;
            configurations = null;
            
        try {
            /**Try reading from the file**/
            File file = new File(CONFIGURATION_FILE);
            fr = new FileReader(file);
            /**Read file contents as characters**/
            char[] c = new char[(int) file.length()];
            fr.read(c);
            configurations = new String(c);
            fr.close();

            //converting it to the configurations object.
            configuration = gson.fromJson(configurations, ConfigurationBean.class);
            //display the conifigurations
             Logger.getLogger(Configurations.class.getName()).log(Level.SEVERE, "CONFIGURATION FILE CONTENTS:\n=====================================\n\n\n"
                    + gson.toJson(configuration), configurations);
             /**Return the configuration***/
            return configuration;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Configurations.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Configurations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Set the configuration bean
     * @param configurations
     **/
    public void loadConfigurationsFromJson(String configurations) {
        configuration = gson.fromJson(configurations, ConfigurationBean.class);
    }

    /**
     * Write the new configurations to file
     * @param configuration 
     */
    public void writeConfigurations(ConfigurationBean configuration) {
        String config = gson.toJson(configuration);

        FileWriter writer = null;
        try {
            writer = new FileWriter(new File(CONFIGURATION_FILE));
            writer.write(config);
            writer.flush();
            writer.close();
            writer = null;
        } catch (IOException ex) {
            Logger.getLogger(Configurations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Configurations.class.getName()).log(Level.SEVERE, null, ex);
            }
            writer = null;
        }
    }
    
    /**
     * Returns the configurations held by this current instance.
     *   Na comment, sina kitu cha kusema. hahha
     * @return 
     */
    public ConfigurationBean getConfigurations(){
        return configuration;
    }
}
