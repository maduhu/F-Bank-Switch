/**
  *This code belongs to Riverbank Solutions Kenya.
  */

package zizi.family.transactions.POS;

/**
 *
 * @author ignatius ojiambo
 */
public interface ZiziTransactionIF {
    public String getType();
    
    public String getTerminalID();
    
    public double getAmount();
    
    
    public String getAccount(); 
}
