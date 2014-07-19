/**
  *This code belongs to Riverbank Solutions Kenya.
  */


package zizi.family.transaction.env;

/**
 *  Put all configuration in place
 * @author ignatius ojiambo
 */
public class ConfigurationBean {
    
    DashBoard dashBoard;
    HSM hsm;
    RemoteSwitch remoteSwitch;
    Postilion postilion;
    /**
     * Default constructor
     */
    private ConfigurationBean() {
    }
    /**
     * Holds the object instance for this configuration class
     * @return 
     */
    public static ConfigurationBean getInstance() {
        return ConfigurationBeanHolder.INSTANCE;
    }
    /**
     * Creates the configuration object
     */
    private static class ConfigurationBeanHolder {
        private static final ConfigurationBean INSTANCE = new ConfigurationBean();
    }
    /**
     * Get the Dashboard feature for this application
     * @return 
     */
    public DashBoard getDashBoard()  {
        return this.dashBoard;
    }
    /**
     * Set the dashboard feature for ZIZI
     * @param dashBoard 
     */
    public void setDashBoard(DashBoard dashBoard) {
        this.dashBoard = dashBoard;
    }
    /**
     * Get the HSM features for ZIZI 
     * @return 
     */
    public HSM getHsm()  {
      return this.hsm;
    }
    /**
     * Set the HSM features for ZIZI
     * @param hsm 
     */
    public void setHsm(HSM hsm){
      this.hsm = hsm;
    }
    /**
     * Get the remote switch feature for ZIZI
     * @return 
     */
    public RemoteSwitch getRemoteSwitch() {
      return this.remoteSwitch;
    }
    /**
     * Set the remote switch feature for Zizi
     * @param remoteSwitch 
     */
    public void setRemoteSwitch(RemoteSwitch remoteSwitch)   {
      this.remoteSwitch = remoteSwitch;
    }
    /**
     * Get the postilion feature for ZIZI
     * @return 
     */
    public Postilion getPostilion(){
        return this.postilion;
    }
    /**
     * Set the postilion Feature for ZIZI
     * @param t24 
     */
    public void setPostilion(Postilion t24){
      this.postilion = t24;
    }
}
