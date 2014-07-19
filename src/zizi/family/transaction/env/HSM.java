/**
  *This code belongs to Riverbank Solutions Kenya.
  */


package zizi.family.transaction.env;

/**
 *
 * @author ignatius ojiambo
 */
public class HSM {
    /**Set the host for HSM**/
    String host;
    /**Set the port for this HSM**/
    int port;
    /***Set the source TPK**/
    String srcTPK;
    /**Set the destination ZPK***/
    String destZPK; 
    
    /**
     * Get the HSM host name
     * @return 
     */
    public String getHost() {
        return host;
    }
    
    /**
     * Get the HSM port
     * @return 
     */
    public int getPort() {
        return port;
    }
    /**
     * Get the Source TPK for this HSM
     * @return 
     */
    public String getSrcTPK() {
        return srcTPK;
    }

    public String getDestZPK() {
        return destZPK;
    }
    
    /**
     * Set the HSM host name
     * @param host 
     */
    public void setHost(String host) {
        this.host = host;
    }
    
    /**
     * Set the HSM port number
     * @param port 
     */
    public void setPort(int port) {
        this.port = port;
    }
    
    /**
     * Set the HSM source TPK
     * @param srcTPK 
     */
    public void setSrcTPK(String srcTPK) {
        this.srcTPK = srcTPK;
    }
    
    /**
     * Set the HSM ZPK
     * @param destZPK 
     */
    public void setDestZPK(String destZPK) {
        this.destZPK = destZPK;
    }
}
