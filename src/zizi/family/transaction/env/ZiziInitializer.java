/**
  *This code belongs to Riverbank Solutions Kenya.
  */


package zizi.family.transaction.env;

import java.util.logging.Level;
import java.util.logging.Logger;
import zizi.family.diagnostics.Diagnostics;
import zizi.family.diagnostics.DiagnosticsService;
import zizi.family.transactions.ziziswitch.hsm.HsmTransactor;

/**
 *  Contains the initialisation details for all components in the distributed system
 *  @author ignatius ojiambo
 */
public final class ZiziInitializer {
    //Create the Gson Engine
    GSONEngine gsonEngine = null;
    //create the configurations
    Configurations conf = null;
    //create the diagnostics object.
    Diagnostics diagnostics = null;
    //diagnostics Service
    DiagnosticsService diagnosticsService = null;
    
    
    /**
     * Creates an instance of the initialiser class
     */
    private static class ZiziInitializerHolder {
        private static final ZiziInitializer INSTANCE = new ZiziInitializer();
    }
    /**
     * Creates the ZIZI initialiser instance
     * @return 
     */
    public static ZiziInitializer getInstance() {
        return ZiziInitializerHolder.INSTANCE;
    }
    
    private ZiziInitializer() {
//        create all gson resources first.
        gsonEngine = GSONEngine.getInstance();

//        set up all the necessary configurations.
        conf = Configurations.getInstance();

//        start the diagnostics
        diagnostics = Diagnostics.getInstance();

//        run diagnostics
////        diagnostics.runDiagnostics();
        //start the diagnostics service
        diagnosticsService = DiagnosticsService.getInstance();

    }
    
    
    /**
     * Initialize all the components required here.
     */
    public void initComponents() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000l);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ZiziInitializer.class.getName()).log(Level.SEVERE,"Zizi initializer - thread sleep Issue", ex);
                }
                              
            }
        }.start();

    }
    
    public void reInitComponents() {
        //read from configurations
        Logger.getLogger(Diagnostics.class.getCanonicalName()).log(Level.INFO, "Re-loading configurations from file.");

        //check the possibility of the connection Settings having changed on file system.
        Configurations.getInstance().loadConfigurationsFromFile();

        //reconnect the hsm
        HsmTransactor.reconnectComponent();

        //reconnect the remote switch
//        BankWorldTransactor.getInstance().reconnectComponent();
    }
}
