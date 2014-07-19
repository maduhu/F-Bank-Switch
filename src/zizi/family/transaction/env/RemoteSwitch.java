/**
  *This code belongs to Riverbank Solutions Kenya.
  */


package zizi.family.transaction.env;

/**
 *
 * @author ignatius ojiambo
 */
public class RemoteSwitch {
    /**Remote Switch host name**/
    String host;
    /**Remote switch port number**/
    int port;
    
    /**
     * Get the Remote Switch host names
     * @return 
     */
    public String getHost() {
        return host;
    }
    /**
     * Get the remote switch port number
     * @return 
     */
    public int getPort() {
        return port;
    }       

    /**
     * Set the remote switch host name
     * @param host 
     */
    public void setHost(String host) {
        this.host = host;
    }
    
    /**
     * Set the Remote switch port number.
     * @param port 
     */
    public void setPort(int port) {
        this.port = port;
    }  
}
