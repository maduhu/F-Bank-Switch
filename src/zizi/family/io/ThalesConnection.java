/**
  *This code belongs to Riverbank Solutions Kenya.
  */


package zizi.family.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import zizi.family.diagnostics.Diagnostics;
import zizi.family.transactions.filter.ZiziTransactionFilter;

/**
 *  A thales console adopter
 * @author ignatius ojiambo
 */
public class ThalesConnection {
    /**Socket connection to the thales***/
     private Socket socket = null;
     /**Ip addresss of the thales***/
    private String ipAddress = "";
    /**port connection to the thames***/
    private int port = 0;
    /**Output and input data writers and readers**/
    private DataOutputStream out = null;
    private DataInputStream in = null;
    
    /**
     * Connect to thales and become a console.
     * @param host thales location
     * @param port  thales port
     */
    public ThalesConnection(String host, int port) {
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
                Logger.getLogger(ThalesConnection.class.getName()).log(Level.INFO, "Connected to the Server socket on: {0}", port);

                in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

                Diagnostics d = Diagnostics.getInstance();
                d.setOnline(d.getHsm());
                ZiziTransactionFilter.getInstance().changeFilterConfigurationsBasedOnDiagnosticsChange();

                Logger.getLogger(ThalesConnection.class.getName()).log(Level.INFO, "Input and Output Streams created.");
            }
        } catch (UnknownHostException ex) {
            Diagnostics d = Diagnostics.getInstance();
            d.setOffline(d.getHsm());
            ZiziTransactionFilter.getInstance().changeFilterConfigurationsBasedOnDiagnosticsChange();
            Logger.getLogger(ThalesConnection.class.getName()).log(Level.INFO, "Host " + ipAddress + " not found." + port, ex);
        } catch (IOException ex) {
            Diagnostics d = Diagnostics.getInstance();
            d.setOffline(d.getHsm());
            ZiziTransactionFilter.getInstance().changeFilterConfigurationsBasedOnDiagnosticsChange();
            Logger.getLogger(ThalesConnection.class.getName()).log(Level.INFO, "Connection error confirm that the HSM service is running on :" + port, ex);
            //close everything.
            this.close();
        }
    }
    
    /**
     * Write to the thales to find if it is online
     * @param data
     * @return 
     */
    public boolean write(String data) {
        try {
            Logger.getLogger(ThalesConnection.class.getName()).log(Level.INFO, "<< writing {0}", data);
            out.writeUTF(data);
            out.flush();
            Diagnostics d = Diagnostics.getInstance();
            d.setOnline(d.getHsm());
            return true;
        } catch (IOException ex) {
            Diagnostics d = Diagnostics.getInstance();
            d.setOffline(d.getHsm());
            Logger.getLogger(ThalesConnection.class.getName()).log(Level.INFO, "Could not write to the connection.");
            return false;
        }
    }
    
    /**
     * Read from the thales
     * @return 
     */
    public String read() {
        String response = "";
        try {
            response = in.readUTF();
            Diagnostics d = Diagnostics.getInstance();
            d.setOnline(d.getHsm());
        } catch (IOException ex) {
            Diagnostics d = Diagnostics.getInstance();
            d.setOffline(d.getHsm());
            Logger.getLogger(ThalesConnection.class.getName()).log(Level.INFO, "Could not read the response.", ex);
        }
        Logger.getLogger(ThalesConnection.class.getName()).log(Level.INFO, "reading >>{0}", response);
        return response;
    }

    /**
     * Enable to and fro communication.
     * @param data - data being sent to the HSM.
     * @return response from HSM.
     */
    public String duplex(String data) {
        write(data);
        return read();
    }

    /**
     * Check if ZIZI is connected to the HSM.
     * @return connection status.
     */
    public boolean isConnected() {
        return socket.isConnected();
    }

    /**
     * Closes everything, the Streams and Sockets.
     */
    public void close() {
        try {
            if (out != null) {
                Logger.getLogger(ThalesConnection.class.getName()).log(Level.INFO, "Closing the output stream.");
                out.close();
            }
            if (in != null) {
                Logger.getLogger(ThalesConnection.class.getName()).log(Level.INFO, "Closing the input stream.");
                in.close();
            }
            Logger.getLogger(ThalesConnection.class.getName()).log(Level.INFO, "Closing the sockets.");
            if (socket != null) {
                socket.close();
            }
            Diagnostics d = Diagnostics.getInstance();
            d.setOffline(d.getHsm());
            Logger.getLogger(ThalesConnection.class.getName()).log(Level.INFO, "Connection successfully closed.");
        } catch (IOException ex) {
            Diagnostics d = Diagnostics.getInstance();
            d.setOffline(d.getHsm());
            Logger.getLogger(ThalesConnection.class.getName()).log(Level.SEVERE, "Could not close the connection properly.", ex);
        }
    }
}
