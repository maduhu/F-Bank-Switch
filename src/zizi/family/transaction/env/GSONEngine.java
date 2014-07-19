/**
  *This code belongs to Riverbank Solutions Kenya.
  */


package zizi.family.transaction.env;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Initialise all Gson variables here. This is a highly re-usable class and
 * should be initialised only once in order to be accessed globally.
 *
 * @author ignatius ojiambo
 */
public class GSONEngine {
    /**GSON variables**/
     private final Gson gson,
            dashboard_gson,
            config_gson,
            diagnostics_gson,
            diagnostics_dashboard_gson,
            console_display_gson;
     
     private GSONEngine() {
    /**Set the GSON data**/
        gson = new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        console_display_gson = new GsonBuilder()
                .serializeNulls()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .setPrettyPrinting()
                .create();

        dashboard_gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        config_gson = new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        diagnostics_gson = new GsonBuilder()
                .setPrettyPrinting()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        diagnostics_dashboard_gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
    }
    /**
     * Get the instance of GSONEngine
     * @return 
     */
    public static GSONEngine getInstance() {
        return GsonEngineHolder.INSTANCE;
    }
    /**
     * Hold the GSONEngine object instance
     */
    private static class GsonEngineHolder {
        private static final GSONEngine INSTANCE = new GSONEngine();
    }
    
    /**
     * No pretty printing.
     *  (Didn't know what Dan meant with the above comment)
     * @return
     */
    public Gson getDashBoardGson() {
        return dashboard_gson;
    }

    /**
     * No pretty printing.
     *(Didn't know what Dan meant with the above comment)
     * @return
     */
    public Gson getDiagnosticsDashBoardGson() {
        return diagnostics_dashboard_gson;
    }

    /**
     * Has pretty printing.
     *(Didn't know what Dan meant with the above comment)
     * @return
     */
    public Gson getConfigGson() {
        return config_gson;
    }

    /**
     * Has pretty printing.
     *(Didn't know what Dan meant with the above comment)
     * @return
     */
    public Gson getDiagnosticsGson() {
        return diagnostics_gson;
    }

    /**
     * Has pretty printing.
     *(Didn't know what Dan meant with the above comment)
     * @return
     */
    public Gson getGson() {
        return dashboard_gson;
    }

    /**
     * Has pretty printing.
     *(Didn't know what Dan meant with the above comment)
     * @return
     */
    public Gson getConsoleGson() {
        return console_display_gson;
    }
     
}
