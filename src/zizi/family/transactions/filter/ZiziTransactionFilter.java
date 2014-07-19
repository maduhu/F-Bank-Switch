/**
  *This code belongs to Riverbank Solutions Kenya.
  */


package zizi.family.transactions.filter;


import java.util.Arrays;
import java.util.Enumeration;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.jboss.weld.util.collections.ArraySet;
import zizi.family.diagnostics.Diagnostics;
import zizi.family.transactions.POS.FundsTransferPOSBeanCardPresent;

/**
 *  Filters transactions to be processed by the Switch
 *  Turns away all transactions if the components are not online
 *  Turns away all transactions that require critical component such as Postilion
 *  Thus no transaction 
 * @author ignatius ojiambo
 */
public class ZiziTransactionFilter {
    //contains the transactions that can be processed by zizi.
    ConcurrentHashMap<String, String> map, supported_transactions;
    //get the diagnostics object
    Diagnostics diagnostics = null;
    //categories of transactions
    boolean all = false, cardless = false;
    
    /**
     * Returns  the object instance of this class
     * @return 
     */
    public static ZiziTransactionFilter getInstance() {
        return ZiziTransactionFilterHolder.INSTANCE;
    }
    /**
     * Holds the Object instance of this class
     */
    private static class ZiziTransactionFilterHolder {
        private static final ZiziTransactionFilter INSTANCE = new ZiziTransactionFilter();
    }
    
    
    private ZiziTransactionFilter() {
        map = new ConcurrentHashMap<>();
        supported_transactions = new ConcurrentHashMap<>();
        /**Check out online state condition of components**/
        diagnostics = Diagnostics.getInstance();
        diagnostics.runDiagnostics();
        /**Filters the transactions based on state of the devices that are down***/
        this.changeFilterConfigurationsBasedOnDiagnosticsChange();
        this.log();

        loadAllTxs();
    }
    
    private void loadAllTxs() {
        map.put("funds_transfer_cash_present", FundsTransferPOSBeanCardPresent.class.getCanonicalName());
        supported_transactions.put(FundsTransferPOSBeanCardPresent.class.getCanonicalName(), "Transfer funds to matatu");
   }
    
    private void removeAllTransactions() {
        Enumeration<String> e = map.keys();
        while (e.hasMoreElements()) {
            map.remove(e.nextElement());
        }
    }

    private void removeCardTransactions() {
        map.remove("funds_transfer_cash_present");
    }

    private void addCardTransactions() {
//        map.put("balance_enquiry_card_present", BalanceEnquiryPOSBeanCardPresent.class.getCanonicalName());
//        map.put("bill_payment_card_present", BillPaymentPOSBeanCardPresent.class.getCanonicalName());
//        map.put("cash_withdrawal_card_present", CashWithdrawalPOSBeanCardPresent.class.getCanonicalName());
        map.put("funds_transfer_cash_present", FundsTransferPOSBeanCardPresent.class.getCanonicalName());
//        map.put("ministatement_card_present", MiniStatementPOSBeanCardPresent.class.getCanonicalName());
//        map.put("school_fees_card", FundsTransferPOSBeanCardPresent.class.getCanonicalName());
    }
    
        public void changeFilterConfigurationsBasedOnDiagnosticsChange() {
        //set hsm setting
        //remove all supported transactions.

        diagnostics = Diagnostics.getInstance();

        if (diagnostics.equals(diagnostics.getPrevious())) {
            
            System.out.println("Diagnostics is still the same: "+diagnostics.getDiagnosticsinJson());
        }

        removeAllTransactions();

        if ((diagnostics.getPostilion().isOnline()) ) {
            loadAllTxs();
        }

        if ((!diagnostics.getPostilion().isOnline()))
        {
          removeAllTransactions();
          return;
        }

       

        //remove card based transactions in the case that either the hsm or the
        //remote switch are offline
        if (!diagnostics.getHsm().isOnline() || !diagnostics.getRemoteSwitch().isOnline()) {
            removeCardTransactions();
        } else //add card transactions in the case that the hsm and remote switch are 
        //online.
        if (diagnostics.getHsm().isOnline() && diagnostics.getRemoteSwitch().isOnline()) {
            addCardTransactions();
        }

        //support account opening only when the rules engine is down.

        //set dashboard setting
    }
        
    /**
     * Returns a map of the supported transactions to the zizi transaction entry
     * point.
     *
     * @return
     */
    public ConcurrentHashMap getSupportedTransactions() {
        return map;
    }

    /**
     * Returns a list of transactions supported by ZIZI.
     *
     * @return a list of transactions supported by ZIZI.
     */
    public Set<String> getSupportedTransactionsString() {

        Set<String> txList = new ArraySet<>();
        try {
            Enumeration<String> ivy = map.keys();

            while (ivy.hasMoreElements()) {

                String nextElement = ivy.nextElement();
                String transaction = supported_transactions.get(map.get(nextElement));

              
                    txList.add(transaction);
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return txList;
    }

   
 public void log() {
        System.out.println(Arrays.toString(map.values().toArray()));
    }
}
