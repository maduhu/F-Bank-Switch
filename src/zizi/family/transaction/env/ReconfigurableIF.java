/**
  *This code belongs to Riverbank Solutions Kenya.
  */


package zizi.family.transaction.env;

/**
 *  Connect,Disconnect & reconnect component
 * @author ignatius ojiambo
 */
public interface ReconfigurableIF {
    void connectComponent();
    void disconnectComponent();
    void reconnectComponent();
}
