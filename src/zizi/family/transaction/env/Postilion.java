/**
  *This code belongs to Riverbank Solutions Kenya.
  */


package zizi.family.transaction.env;

/**
 *
 * @author ignatius ojiambo
 */
public class Postilion {
    /**Set the Postilion's host name**/
    String host;
    /**Set the port number for the Postilion **/
    int port;

    /**
     * Get the Postilion host name
     * @return 
     */
    public String getHost() {
        return host;
    }
    /**
     * Get the postilion port number
     * @return 
     */
    public int getPort() {
        return port;
    }
    
    /**
     * Set the host name for this Postilion
     * @param host 
     */
    public void setHost(String host) {
        this.host = host;
    }
    /**
     * Set the port number for this Postilion
     * @param port 
     */
    public void setPort(int port) {
        this.port = port;
    }    
}
