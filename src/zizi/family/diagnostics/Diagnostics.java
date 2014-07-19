package zizi.family.diagnostics;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import zizi.family.io.CustomHTTP;
import zizi.family.transaction.env.ConfigurationBean;
import zizi.family.transaction.env.Configurations;
import zizi.family.transaction.env.GSONEngine;
import zizi.family.transactions.ziziswitch.bankworld.BankWorldTransactor;
import zizi.family.transactions.ziziswitch.hsm.HsmTransactor;

/**
 *
 * @author ignatius ojiambo
 */
public class Diagnostics  implements Runnable{
    /**Security device*/
  private Component hsm;
  /**Remote switch for pin validation*/
  private Component remoteSwitch;
  /**Postilion**/
  private Component Postilion;
  /**Dashboard**/
  private Component dashboard;
  /****/
  private Date time; 
  
  diagStateJSON stateJSON;
  
  Diagnostics previous = null;
   //lists all components
  private Component[] componentList = null;
  Gson gson = null;
  //Configurations
  Configurations configurations = null;
  ConfigurationBean config = null;

    public Diagnostics(){
        
        stateJSON = new diagStateJSON();
        /**Initialise the components***/
        dashboard = new Component();
        hsm = new Component();
        remoteSwitch = new Component();
        Postilion = new Component();
        
        remoteSwitch.setIsOnline(true);
        remoteSwitch.setNarrative("Component is Online. Set manually.");
        
        /***get the global configurations***/
        configurations = Configurations.getInstance();
        config = configurations.getConfigurations();
        /***Initialise the components for ZIZI***/
        componentList = new Component[] { this.dashboard, this.hsm, this.remoteSwitch, this.Postilion };
        //init the gson component.
        gson = GSONEngine.getInstance().getDiagnosticsGson();
    }
    
    
    private static class diagStateJSON{
        private String    hsm = "off";
       private String remoteSwitch = "off";
       private String Postilion = "off";
       
       public void setHSM(String onOFF){
           hsm = onOFF;
       }
       
       public void setremoteSwitch(String onOFF){
           remoteSwitch = onOFF;
       }
       
       public void setPostilion(String onOFF){
           Postilion = onOFF;
       }
       
       
    }
    
    
    
    /**
     * Get the Diagnostic instance
     * @return 
     */
    public static Diagnostics getInstance() {
        return DiagnosticsHolder.INSTANCE;
    }
    /**
     * Hold diagnostic instance
     */
    private static class DiagnosticsHolder {
        private static final Diagnostics INSTANCE = new Diagnostics();
    }
    /**
     * Return this class as diagnostics
     * @return 
     */
    public Diagnostics getDiagnostics() {
        return this;
    }
    /**
     * Return Diagnostics as GSON Object
     * @return 
     */
    public String getDiagnosticsinJson() {
               return gson.toJson(stateJSON);

    }
    
     public void runDiagnostics() {        
        //store current diagnostics parameters in the <code>previous</code>
        //variable.
        setPrevious(this);
        

        //set current time stamp.
        time = new Date(System.currentTimeMillis());
        
        //HSM
        socketComponentDiagnostics(config.getHsm().getHost(), config.getHsm().getPort(), hsm, "hsm");

        //REMOTE SWITCH
        socketComponentDiagnostics(config.getRemoteSwitch().getHost(), config.getRemoteSwitch().getPort(), remoteSwitch, "remoteSwitch");

        //Postilion
        socketComponentDiagnostics(config.getPostilion().getHost(), config.getPostilion().getPort(), Postilion, "postilion");
      
         //DASHBOARD        
        String url = "http://" + config.getDashBoard().getHost() + config.getDashBoard().getUri();
        httpComponentDiagnostics(url, dashboard);
        reportToDashBoard();
    }
    
     /**
      * Check to see if a socket a  communicating component is online
      * @param host the name of the component
      * @param port the port number of the component
      * @param c the component object
      * @param component  the component name
      */
    private void socketComponentDiagnostics(String host, int port, Component c, String component){
      Socket s = null;
      try
      {
        s = new Socket();
        s.setPerformancePreferences(1, 0, 0);
        s.connect(new InetSocketAddress(host, port));
        if (c.isOperationallyOnline())        {
          c.setIsOnline(true);
          c.setNarrative("Component is Online");
        }        
        s.close();
        s = null;
        if("hsm".equals(component)){
            stateJSON.setHSM("on");
        }else if("remoteSwitch".equals(component)){
            stateJSON.setremoteSwitch("on");
        }else if("postilion".equals(component)){
            stateJSON.setPostilion("on");
        }
        
        
      }catch (IOException e)  {
                    if("hsm".equals(component)){
                     stateJSON.setHSM("off");
                 }else if("remoteSwitch".equals(component)){
                     stateJSON.setremoteSwitch("off");
                 }else if("postilion".equals(component)){
                     stateJSON.setPostilion("off");
                 }
            c.setIsOnline(false);
                    System.out.println("INSIDE socketComponentDiagnostics IN Diagnostics: Component is Offline: component: "+component);
            c.setNarrative("Component is Offline. Check connection");
            if(component.equalsIgnoreCase("remoteSwitch"))
            {
                c.setIsOnline(true);
                c.setNarrative("Component is Online");
            }
      }
      finally
      {
        if (s != null) {
          try{
            s.close();
            s = null;
          }
          catch (IOException ex)
          {
            Logger.getLogger(Diagnostics.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
      }
   }
    
    /**
     * Check to see if a HTTP communicating component is working
     * @param url the url of the component
     * @param c the component that is being tested for connectivity
     */
    private void httpComponentDiagnostics(String url, Component c) {
       HttpClient httpclient;
      httpclient = new DefaultHttpClient();
        // Prepare a request object
        HttpGet httpget = new HttpGet(url);
        // Accept JSON
        httpget.addHeader("accept", "application/json");
        // Execute the request
        HttpResponse response;
        try {
            response = httpclient.execute(httpget);
            
            // Get the response entity
            HttpEntity entity = response.getEntity();
            // If response entity is not null
            if (entity != null) {
                try ( // get entity contents and convert it to string
                        InputStream instream = entity.getContent()) {
                    String result = convertStreamToString(instream);                    
                  
                }
            }
            c.setIsOnline(true);
            c.setNarrative("Component is online");
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            c.setIsOnline(false);
            c.setNarrative("Client Protocol Exception");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            c.setIsOnline(false);
            Logger.getLogger(Diagnostics.class.getName()).log(Level.SEVERE,"INSIDE httpComponentDiagnostics IN Diagnostics: Component is Offline", this);
             c.setNarrative("Component is Offline. Check connection");
        }
    } 
    /**
     * Create a string from an InputStream
     * @param instream
     * @return A string that has been converted from the 
     */ 
    private static String convertStreamToString(InputStream instream)    {
      BufferedReader reader = new BufferedReader(new InputStreamReader(instream));

      String line;
      StringBuilder sbBuffer = new StringBuilder();
      try
      {
        while ((line = reader.readLine()) != null) {
          sbBuffer.append(line).append("\n");
        }
        reader.close();
      }
      catch (IOException e) {}
      return sbBuffer.toString();
    }
    /**
     * Return the current dashboard diagnostics
     * @return 
     */    
    public Component getDashboard() {
      return this.dashboard;
    }
    /**
     * Set the current dashboard settings
     * @param dashboard 
     */
    public void setDashboard(Component dashboard)
    {
      System.out.println("DashBoard value changed");
      this.dashboard = dashboard;
    }
    /**
     * Get the HSM settings
     * @return 
     */
    public Component getHsm(){
      return this.hsm;
    }
    /**
     * Set the HSM value
     * @param hsm 
     */
    public void setHsm(Component hsm){
      System.out.println("Hsm value changed");
      this.hsm = hsm;
    }
    /**
     * Get the remote switch value
     * @return 
     */
    public Component getRemoteSwitch(){
      return this.remoteSwitch;
    }
    /**
     * Set the remote switch value
     * @param remoteSwitch 
     */
    public void setRemoteSwitch(Component remoteSwitch){
      System.out.println("Remote Switch value changed");
      this.remoteSwitch = remoteSwitch;
    }
    /**
     * Get the value of the postilion
     * @return 
     */
    public Component getPostilion(){
      return this.Postilion;
    }
    /**
     * Set the postilion value
     * @param Postilion 
     */
    public void setPostilion(Component Postilion){
      System.out.println("Postilion value changed");
      this.Postilion = Postilion;
    }
    
    /**
     * Set the status for the Component if it is offline
     * @param component 
     */
    public void setOffline(Component component){
      int i = 0;
      for (Component c : this.componentList) {
        if (c.equals(component))
        {
          c.setIsOnline(false);
            System.out.println("INSIDE setOffline IN Diagnostics: Component is Offline");
          c.setNarrative("Component is Offline. Check connection");
          if (i == 1)
          {
            HsmTransactor.getInstance();
            HsmTransactor.reconnectComponent();
          }
          if (i == 2) {
            BankWorldTransactor.getInstance().reconnectComponent();
          }
          i++;
        }
      }
      reportToDashBoard();
    }
    /**
     * Set the Components status
     * @param component 
     */
    public void setOnline(Component component) {
        for (Component c : componentList) {
            if (c.equals(component)) {
                c.setIsOnline(true);
                c.setNarrative("Component is Online.");
            }
        }
    }
    /**
     * Get previous Diagnostics
     * @return a previous diagnostic state object
     */
    public Diagnostics getPrevious(){
     return this.previous;
    }
  /**
   * Set previous diagnostic object with current diagnostics
   * @param previous 
   */
    public void setPrevious(Diagnostics previous) {
      this.previous = previous;
    }

  @Override
    public boolean equals(Object obj){
            if (obj == null) {
              return false;
            }
        Diagnostics d = (Diagnostics)obj;
    if (
           (getDashboard().isOnline() == d.getDashboard().isOnline()) 
        && (getHsm().isOnline() == d.getHsm().isOnline())
        && (getRemoteSwitch().isOnline() == d.getRemoteSwitch().isOnline())
        && (getPostilion().isOnline() == d.getPostilion().isOnline())) {
      return true;
    }
    return false;
  }
  
  public void reportToDashBoard(){
    Logger.getGlobal().log(Level.FINEST, "TO DASH BOARD: {0}", gson.toJson(stateJSON));  
    CustomHTTP post = new CustomHTTP("http://" + this.config.getDashBoard().getHost() + this.config.getDashBoard().getUri());
    post.addParameter( gson.toJson(stateJSON));
    post.connect();
  }
    
    @Override
    public void run() {  }
    
      
}
