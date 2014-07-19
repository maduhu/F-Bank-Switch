/**
  *This code belongs to Riverbank Solutions Kenya.
  */


package zizi.family.diagnostics;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import zizi.family.transactions.filter.ZiziTransactionFilter;

/**
 *  Checks for diagnostics after every 30 seconds
 * @author ignatius ojiambo
 */
public class DiagnosticsService implements Runnable{
    private ScheduledThreadPoolExecutor executor = null;
    
    private DiagnosticsService()
  {
    this.executor = new ScheduledThreadPoolExecutor(1);
    this.executor.scheduleAtFixedRate(this, 1L, 30L, TimeUnit.SECONDS);
    this.executor.setRemoveOnCancelPolicy(true);
    this.executor.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
  }
    public static DiagnosticsService getInstance() {
        return DiagnosticsServiceHolder.INSTANCE;
    }

    private static class DiagnosticsServiceHolder {

        private static final DiagnosticsService INSTANCE = new DiagnosticsService();
    }
    /**
     * Runs a diagnostic task see  what components are online
     */
    @Override
    public void run()    {
      Logger.getLogger(DiagnosticsService.class.getName()).log(Level.INFO, "Running Zizi Diagniostics task.");
      Diagnostics.getInstance().runDiagnostics();
      if (!Diagnostics.getInstance().equals(Diagnostics.getInstance().getPrevious()))
      {
        ZiziTransactionFilter.getInstance().changeFilterConfigurationsBasedOnDiagnosticsChange();
        Diagnostics.getInstance().reportToDashBoard();
      }
      
      ZiziTransactionFilter.getInstance().changeFilterConfigurationsBasedOnDiagnosticsChange();      
      
    }
    
      /**
     * Stops the executor service.
     */
    public void stopService() {
        executor.shutdownNow();
    }
}
