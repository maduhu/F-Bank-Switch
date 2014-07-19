/**
  *This code belongs to Riverbank Solutions Kenya.
  */


package zizi.family.transaction.env;

/**
 *
 * @author ignatius ojiambo
 */
public class DashBoard {
    /**Host for this dashboard**/
     String host;
     /*** Url for this dashboard**/
     String uri;
     
    /**
     * Get the host for this dashboard
     * @return 
     */
    public String getHost() {
        return host;
    }
    
    /**
     * Set the host for this dashboard
     * @param host 
     */
    public void setHost(String host) {
        this.host = host;
    }
    
    /**
     * Get the uri for this dashboard
     * @return 
     */
    public String getUri() {
        return uri;
    }
    
    /**
     * Set the uri for this dashboard
     * @param uri 
     */
    public void setUri(String uri) {
        this.uri = uri;
    }
}
