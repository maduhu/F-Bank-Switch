/**
  *This code belongs to Riverbank Solutions Kenya.
  */


package zizi.family.transactions.ziziswitch.bankworld;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jpos.iso.ISODate;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.packager.ISO87ABankWorldPackager;
import zizi.family.diagnostics.Diagnostics;
import zizi.family.jpos.transaction.TransactionIDGenerator;
import zizi.family.transaction.env.ConfigurationBean;
import zizi.family.transaction.env.Configurations;
import zizi.family.transaction.env.ReconfigurableIF;

/**
 *  Manage communication and connection to the bank world
 * @author ignatius ojiambo
 */
public class BankWorldTransactor implements Runnable, ReconfigurableIF {
     //create ISO 87 bank world compatible packager
    static ISO87ABankWorldPackager packager = null;
    //create bank world connection
    static zizi.family.io.BankWorldConnection conn = null;
    //result hashmap where all results are stored.    
    static ConcurrentHashMap<String, ISOMsg> responses = null;
    //contains a string with the p[acked iso.
    private String packed = "";
    //does all the reads and send echo message replies back to bankworld.
    private Thread transactor = null;
    //holds the global configurations.
    private Configurations configurations = null;
    //holds the information about the current configuration.
    private ConfigurationBean config = null;
    //flag indicating whether zizi is signed on to bank world.
    private boolean signedOn = false;
    //get a diagnostics instance here.
    private Diagnostics diagnostics = Diagnostics.getInstance();
    
    private static class BankWorldTransactorHolder {
        private static final BankWorldTransactor transactor = new BankWorldTransactor();
    }
    /**
     * Get the instance of bank world transactor
     * @return 
     */
    public static BankWorldTransactor getInstance() {
        return BankWorldTransactorHolder.transactor;
    }
    /**
     * Initializes the bank world transaction
     */
    private BankWorldTransactor() {
        init();
    }
     
    @Override
    public void run() {
        Logger.getLogger(BankWorldTransactor.class.getName()).log(Level.INFO, "Starting TranzWare Transactor Daemon.");
        while (true) {
            try {
                byte[] in = conn.read();
                ISOMsg read = new ISOMsg();
                read.setPackager(packager);                
                Logger.getGlobal().log(Level.FINEST, "Hex 2 byte: " + ISOUtil.hexString(in));
                String response = ISOUtil.hexString(in).substring(4);
                read.unpack(response.getBytes());
                read.dump(System.out, "reading");

                //0800 or 0810
                if (read.getMTI().equals("0800")) {
                    if (read.getString(48).equals("20000000")) {
                        setSignedOn(false);
                        Logger.getLogger(BankWorldTransactor.class.getName()).log(Level.INFO, "TranzWare has logged off.");
                    }
                    read.setMTI("0810");
                    read.set("39", "3030");
                    read.dump(System.out, "echo-response");
                    //send the echo response.
                    packed = addISoHeader(read.pack());
                    Logger.getLogger(BankWorldTransactor.class.getCanonicalName()).log(Level.FINEST, "ECHO RESPONSE ISO : " + packed);
//                  conn.write(packed.getBytes());
                    writeToBankWorld(read);
                } else if (read.getMTI().equals("0810")) {
                    read.dump(System.out, "sign-on_response");
                    setSignedOn(true);
                    //create a flag to show whether zizi is signed on to 
                    //bank world or not.
                } else if (read.getMTI().equals("0110")) {
                    read.dump(System.out, "authorization_response");
                    //add the authorization response to the response map.
                    responses.put(read.getString(2), read);
                    Logger.getLogger(BankWorldTransactor.class.getName())
                            .log(Level.INFO, "Pan: " + read.getString(2) + " responses: " + responses.size());
                }
            } catch (ISOException ex) {
                Logger.getLogger(BankWorldTransactor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void connectComponent() {
           init();
    }
    /**
     * Stopping the bank world connection
     */
    @Override
    public void disconnectComponent() {
        Logger.getLogger(BankWorldTransactor.class.getName()).log(Level.INFO, "Stopping Bankworld transactor.");
        //kill the transactor thread.
        transactor = null;

        Logger.getLogger(BankWorldTransactor.class.getName()).log(Level.INFO, "Signing off and Closing zizi Bankworld socket.");
        if (conn.isConnected()) {
            writeToBankWorld(signOffMessage());
            conn.close();
        }

        Logger.getLogger(BankWorldTransactor.class.getName()).log(Level.INFO, "Erasing zizi - bankworld connection configurations.");
        configurations = null;
        packager = null;

        Logger.getLogger(BankWorldTransactor.class.getName()).log(Level.INFO, "Abandoning all Bankworld Responses");
        responses = null;
    }

    @Override
    public void reconnectComponent() {
      disconnectComponent();
      connectComponent();
      writeToBankWorld(signOnMessage());
    }
    
    private void init(){
        //init the packager
        packager = new ISO87ABankWorldPackager();
        //connect to the bank world
        configurations = Configurations.getInstance();

        config = configurations.getConfigurations();

        //create a single connection.
        conn = new zizi.family.io.BankWorldConnection(
                config.getRemoteSwitch().getHost(),
                config.getRemoteSwitch().getPort());

        //create the responses hashmap
        responses = new ConcurrentHashMap<>();

        //init the transactor thread.
        Logger.getLogger(BankWorldTransactor.class.getName()).log(Level.INFO, "Initializing Bankworld transactor.");
        transactor = new Thread(this);

        }
    /**
     * Create a new sign on message to the bank world
     * @return 
     */
    private static ISOMsg signOnMessage() {
        try {
            ISOMsg signon = new ISOMsg("0800");
            signon.setPackager(packager);
            Date signon_date = new Date(System.currentTimeMillis());
            signon.set("7", ISODate.getDateTime(signon_date));
            signon.set("11", "" + TransactionIDGenerator.nextIntegerTransactionId());
            signon.set("48", "10000000");
            return signon;
        } catch (ISOException ex) {
            Logger.getLogger(BankWorldTransactor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    /**
     * Create a sign off message to the bank
     * @return 
     */
    private static ISOMsg signOffMessage() {
        try {
            ISOMsg signoff = new ISOMsg("0800");
            signoff.setPackager(packager);
            Date signon_date = new Date(System.currentTimeMillis());
            signoff.set("7", ISODate.getDateTime(signon_date));
            signoff.set("11", "" + TransactionIDGenerator.nextIntegerTransactionId());
            signoff.set("48", "20000000");
            return signoff;
        } catch (ISOException ex) {
            Logger.getLogger(BankWorldTransactor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Converts the iso message to it's necessary bytes format then sends it to
     * the ZIZI-BANKWORLD connection.
     * @param msg
     */
    private void writeToBankWorld(ISOMsg msg) {
        byte[] b = null;
        try {
            if (msg.getMTI().equals("0100")) {
                b = new String("0033" + new String(msg.pack())).getBytes();
            } else {
                b = new String("0016" + new String(msg.pack())).getBytes();
            }

            //display the message that is to  be sent to bank world.
            msg.dump(System.out, "writing");
            conn.write(b);
        } catch (ISOException ex) {
            Logger.getLogger(BankWorldTransactor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * This method queries the resulting response map and makes the appropriate
     * response to the sequence of execution given to it.
     * @param pan
     * @return
     */
    public String getBankworldResponse(String pan) {
        try {
            pan = "3136" + pan;
            ISOMsg resp = responses.get(pan);
            String code = resp.getString(39);
            code = code.charAt(1) + "" + code.charAt(3);
            return code;
        } catch (NullPointerException ex) {
            return null;
        }
    }
    /**
     * This method queries the resulting response map and makes the appropriate
     * response to the sequence of execution given to it.
     * @param pan
     * @return
     */
    public String getPostilionAccount(String pan) {
        try {
            pan = "3136" + pan;

            ISOMsg resp = responses.get(pan);
            String account = resp.getString(102);
            char[] c = account.toCharArray();

            account = "";
            //create the account from the jumbled string
            for (int i = 0; i < c.length; i++) {
                if (i % 2 == 1) {
                    account += "" + c[i];
                }
            }
            return account.substring(2);
        } catch (NullPointerException ex) {
            return null;
        }
    }
    /**
     * Creating authentication message
     * @param pan Primary Account number
     * @param pinBlock Primary Pin block
     * @return 
     */
    public ISOMsg createAuthMessage(String pan, String pinBlock) {
        System.out.println("PAN: " + pan + "  Pin Block: " + pinBlock);
        try {
            ISOMsg authorization = new ISOMsg();
            authorization.setPackager(packager);
            authorization.setMTI("0100");
            authorization.set("2", "3136" + pan);
            authorization.set("3", "310000");
            authorization.set("7", ISODate.getDateTime(new Date(System.currentTimeMillis())));
            authorization.set("18", "6011");
            authorization.set("32", "3036888888");
            authorization.set("52", pinBlock);
            authorization.set("53", "2001010100000000");
            return authorization;
        } catch (ISOException ex) {
            Logger.getLogger(BankWorldTransactor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }
    /**
     * Add the ISO HEADER.Message Length
     * @param iso
     * @return
     */
    public static String addISoHeader(byte[] iso) {
        return "0016" + new String(iso);
    }
    /**
     * Starting the bankworld
     */
    public void startTransactor() {
        Logger.getLogger(BankWorldTransactor.class.getName()).log(Level.INFO, "Starting Bankworld.");

        if (transactor == null) {
            transactor = new Thread(this);
        }

        if (!conn.isConnected()) {
            conn.connect();
        }

        try {
            Thread.sleep(300l);
        } catch (InterruptedException ex) {
            Logger.getLogger(BankWorldTransactor.class.getName()).log(Level.SEVERE, null, ex);
        }

        //place sign on request
        placeSignOnRequest();

        //start the transactor thread
        transactor.start();
        Logger.getLogger(BankWorldTransactor.class.getName()).log(Level.INFO, "Bank world Transactor Started.");
    }
    
    /**
     * Places a request for the sign on procedure.
     */
    public void placeSignOnRequest() {
        Logger.getLogger(BankWorldTransactor.class.getName()).log(Level.INFO, "Signing on to Bankworld...");
        writeToBankWorld(signOnMessage());
    }
    
    public void stopTransactor() {
        //start by signing off.
        writeToBankWorld(signOffMessage());
        //kill transactor thread
        transactor.stop();
        transactor = null;
        //close all connections
        conn.close();
    }

    /**
     * Check whether zizi is signed on to bank world.
     * @returns Current sign on state between ZIZI and Bank world.
     */
    public boolean isSignedOn() {
        return signedOn;
    }

    /**
     * Changes the sign on / sign off state of ZIZI and bank world.
     * @param signedOn
     */
    public void setSignedOn(boolean signedOn) {
        this.signedOn = signedOn;
        if (!signedOn) {
            diagnostics.getRemoteSwitch().setOperationallyOnline(false);
            diagnostics.getRemoteSwitch().setNarrative("Component is online but ZIZI is not signed on.");
        }
    }
    

}
