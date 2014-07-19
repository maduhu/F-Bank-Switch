/**
  *This code belongs to Riverbank Solutions Kenya.
  */


package zizi.family.io;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import org.apache.http.entity.ContentType;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import zizi.family.diagnostics.Diagnostics;

/**
 *  A custom HTTP client
 * @author ignatius ojiambo
 */
public class CustomHTTP<T> {
  Class<T> entityClass;
  private String url;
  String nameValuePairs = "";
  HttpClient httpclient;
  HttpPost httppost;
  private byte[] result;
  private HttpResponse response;
  
  /**
   * Initialize the variables for the custom HTTP object
   * @param url 
   */
  public CustomHTTP(String url) {
    this.url = url;
    this.httpclient = new DefaultHttpClient();
    this.httppost = new HttpPost(this.url);
//    httppost.addHeader("accept", "application/json");
  }
  /**
   * Add parameters for the HTTP client
   * @param key 
   */
   public void addParameter(String key){
    this.nameValuePairs = key;
  }
   
   public void connect() {
        try {
            //set Entity
            httppost.setEntity(new StringEntity(nameValuePairs,ContentType.create("application/json")));
            System.out.println("To dash board ::::: "+nameValuePairs);
            // Execute HTTP Post Request
            response = httpclient.execute(httppost);
            
            
            //write to stream
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            line = rd.readLine();
              
               
            
            result = line.getBytes();
                    Diagnostics d = Diagnostics.getInstance();
            d.setOnline(d.getDashboard());
            System.out.println("Print dashboard result"+line);
            
        } catch (ClientProtocolException e) {
            Diagnostics d = Diagnostics.getInstance();
            d.setOffline(d.getDashboard());            
        } catch (IOException e) {
            Diagnostics d = Diagnostics.getInstance();
            d.setOffline(d.getDashboard());            
        }
    }
  
     /**
     * Return the result in the form of a byte array
     * @return
     */
    public byte[] getResult() {
        return result;
    }

    /**
     * Return the result in form of a String.
     *
     * @return
     */
    @Override
    public String toString() {
        return new String(getResult());
    }
    
    /**
     * Constructs a class from the JSON serialized form of the class.
     * @return
     */
    public T getNamedClass() {
        Gson gson = new Gson();
        return gson.fromJson(this.toString(), entityClass);
    }

    /**
     * Help bind a custom Output Stream.
     * @param out
     * @return 
     */
    public OutputStream bindtoOutPutStream(OutputStream out) {
        try {
            response.getEntity().writeTo(out);
            return out;
        } catch (IOException ex) {
            return null;
        }
    }
}
