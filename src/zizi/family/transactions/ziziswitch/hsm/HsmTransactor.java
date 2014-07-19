/**
  *This code belongs to Riverbank Solutions Kenya.
  */


package zizi.family.transactions.ziziswitch.hsm;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.hairi.Thales.HThalesCore;
import net.hairi.Thales.HThalesMsg;
import org.jdom.JDOMException;
import org.jpos.iso.ISOException;
import zizi.family.io.ThalesConnection;
import zizi.family.jpos.transaction.TransactionIDGenerator;
import zizi.family.transaction.env.ConfigurationBean;
import zizi.family.transaction.env.Configurations;
import zizi.family.transactions.POS.Track2Bean;
import zizi.family.transactions.POS.Track2Processor;

/**
 *
 * @author ignatius ojiambo
 */
public class HsmTransactor {
    private static Track2Processor processor = null;
    private Track2Bean bean = null;
    private String srcTPK;
    private String destZPK;
    private String pinBlock;
    private String track2;
    private String accountNumber;
    private static ThalesConnection conn = null;
    private static HThalesCore core = null;
    private static Configurations configurations = null;
    private static ConfigurationBean config;
    private static HsmReconnector connector;
    
    /**
     * Default constructor for this class
     * Initializes all the variables
     */
    private HsmTransactor() {
        init();
    }
    /**
     * Returns the instance of the HSM transactor object
     * @return 
     */
    public static HsmTransactor getInstance() {
        return HsmTransactoHolder.INSTANCE;
    }
    /**
     * Creates the instance fot HSM transactionholder
     */
    private static class HsmTransactoHolder {
        private static final HsmTransactor INSTANCE = new HsmTransactor();
    }
    
    /**
     * Runs the reconnection task in the HSM transactor.
     */
    private static class HsmReconnector implements Runnable {
     public HsmReconnector() { }

        //run the reconnection here.
        @Override
        public void run() {
            HsmTransactor.getInstance().disconnectComponent();
            connectComponent();
        }
    }
    
    private static void init() {
        //create the Track 2 processor
        processor = new Track2Processor();

        // init the configurations
        configurations = Configurations.getInstance();

//        conn = new ThalesConnection("172.16.23.253", 5500);
        config = configurations.getConfigurations();

        //Create a single connectin to the Thales HSM.
        conn = new ThalesConnection(config.getHsm().getHost(), config.getHsm().getPort());

        //creating the core thales object.
        core = new HThalesCore("192.168.1.115", 9998, "fsdXML/hsm-", "base");
    }
    public String translate(String srcTPK, String destZPK, String pinBlock, String accountNumber) {
        try {
            String hsm_request = "";
            HThalesMsg iso = null;

            hsm_request = TransactionIDGenerator.nextHSMTransactionID()
                    + createTranslationRequest(srcTPK, destZPK, pinBlock, accountNumber).pack();

            System.out.println("Request: " + hsm_request);

            //send the request to the hsm device and get a response.            
//            return conn.duplex(hsm_request);
            return hsm_request;
        } catch (JDOMException | ISOException ex) {
            Logger.getLogger(HsmTransactor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HsmTransactor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HsmTransactor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
     private static void connectComponent() {
        init();
    }

    private static void disconnectComponent() {
        //create the Track 2 processor
        processor = null;

        // init the configurations
        configurations = null;

        //conn = new ThalesConnection("172.16.23.253", 5500);
        config = null;

        //Create a single connectin to the Thales HSM.
        conn = null;

        //creating the core thales object.
        core = null;
    }

    public static HThalesMsg createTranslationRequest(String srcTPK, String destZPK, String pinBlock, String accountNumber) {
         HThalesMsg pinBlock_translation = null;
        try {

            //create the pin block translation message.
            //4180870280047010=16071261024967101000Pinblock:1412348D665AC5A3
            pinBlock_translation = core.createRequest("CA");
            pinBlock_translation.set("source_tpk", srcTPK);
            pinBlock_translation.set("destination_zpk", destZPK);
            pinBlock_translation.set("maximum_pin_length", "12");
            pinBlock_translation.set("source_pin_block", pinBlock);
            pinBlock_translation.set("source_pin_block_format_code", "01");
            pinBlock_translation.set("destination_pin_block_format_code", "01");
            pinBlock_translation.set("account_number", accountNumber);
            pinBlock_translation.dump(System.out, "write");
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(HsmTransactor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HsmTransactor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pinBlock_translation;
    }
    
    /**
     * Send request to HSM and get response
     * @param hsmRequest
     * @return 
     */
     public String processHsmTranslationRequest(String hsmRequest) {
        return conn.duplex(hsmRequest);
    }
     
     /**
     *
     * Runs the connection procedure.
     */
    public static void reconnectComponent() {
        conn.connect();
    }
    
}
