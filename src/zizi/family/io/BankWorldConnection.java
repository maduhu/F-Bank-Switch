/**
  *This code belongs to Riverbank Solutions Kenya.
  */


package zizi.family.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jpos.iso.ISOUtil;

/**
 *  A thales console adaptor
 * @author ignatius ojiambo
 */
public class BankWorldConnection {
    private Socket socket = null;
    private String ipAddress = "";
    private int port = 0;
    private BufferedOutputStream out = null;
    private BufferedInputStream in = null;
    //for the time being..
    String command = "";
    
    public BankWorldConnection(String host, int port) {
        //Connect to thales and become a console.
        this.ipAddress = host;
        this.port = port;
        connect();
    }
    
    public void connect() {
        try {
            //Connect to the socket
            socket = new Socket(ipAddress, port);
            //create the IO channels..
            if (socket != null) {
                Logger.getLogger(BankWorldConnection.class.getName()).log(Level.INFO, "Connected to the Server socket on: " + port);

                in = new BufferedInputStream(new BufferedInputStream(socket.getInputStream()));
                out = new BufferedOutputStream(new BufferedOutputStream(socket.getOutputStream()));

                Logger.getLogger(BankWorldConnection.class.getName()).log(Level.INFO, "Input and Output Streams created.");
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(BankWorldConnection.class.getName()).log(Level.INFO, "Host " + ipAddress + " not found." + port, ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(BankWorldConnection.class.getName()).log(Level.INFO, "Connection error confirm that Bankworld PVI service is running on :" + port, ex);
            //close everything.
            this.close();
        }
    }
    
        public boolean write(byte[] data) {
        try {
            Logger.getLogger(BankWorldConnection.class.getName()).log(Level.INFO, "<< writing " + new String(data));
            out.write(ISOUtil.hex2byte(new String(data)));
            out.flush();
            Logger.getLogger(BankWorldConnection.class.getName()).log(Level.INFO, +new String(data).length() + "bytes sent.");
            return true;
        } catch (IOException ex) {
            Logger.getLogger(BankWorldConnection.class.getName()).log(Level.SEVERE, "Could not write to the connection.");
            return false;
        }
    }

    public byte[] read() {
        String response = "";
        byte[] b = new byte[256];
        try {                                                                                    
            //read the bytes.
            in.read(b);

            //fetch the response
            response = new String(b);
            
            Logger.getLogger(BankWorldConnection.class.getName()).log(Level.INFO, +response.length() + "bytes received.");
        } catch (IOException ex) {
            Logger.getLogger(BankWorldConnection.class.getName()).log(Level.SEVERE, "Could not read the response.", ex);
        }
        Logger.getLogger(BankWorldConnection.class.getName()).log(Level.INFO, "reading >>" + response);
        return b;
    }
    
    /**
     * Enable to and fro communication.
     * @param data - data being sent to the HSM.
     * @return response from HSM.
     */
    public byte[] duplex(byte[] data) {
        write(data);
        return read();
    }

    /**
     * Return the connection status to TranzWare.
     * @return connection status.
     */
    public boolean isConnected(){
        return socket.isConnected();
    }
    /**
     * Closes everything, the Streams and Sockets.
     */
    public void close() {

        try {
            if (out != null) {
                Logger.getLogger(BankWorldConnection.class.getName()).log(Level.INFO, "Closing the output stream.");
                out.close();
            }
            if (in != null) {
                Logger.getLogger(BankWorldConnection.class.getName()).log(Level.INFO, "Closing the input stream.");
                in.close();
            }
            Logger.getLogger(BankWorldConnection.class.getName()).log(Level.INFO, "Closing the sockets.");
            if (socket != null) {
                socket.close();
            }
            Logger.getLogger(BankWorldConnection.class.getName()).log(Level.INFO, "Connection successfully closed.");
        } catch (IOException ex) {
            Logger.getLogger(BankWorldConnection.class.getName()).log(Level.SEVERE, "Could not close the connection properly.", ex);
        }

    }

}
