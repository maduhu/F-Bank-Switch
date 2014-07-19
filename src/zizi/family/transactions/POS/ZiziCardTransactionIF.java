/**
  *This code belongs to Riverbank Solutions Kenya.
  */

package zizi.family.transactions.POS;

/**
 *
 * @author ignatius ojiambo
 */
public interface ZiziCardTransactionIF extends ZiziTransactionIF{
    public String getTrack2();
    public String getPinBlock();
}
