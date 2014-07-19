/**
  *This code belongs to Riverbank Solutions Kenya.
  */


package zizi.family.transactions.POS;

/**
 *
 * @author ignatius ojiambo
 */
public class Track2Bean {
    String expiration_date;
    String service_code;
    String pan;
    String pvki;
    String pvv_offset;
    String cvv_cvc;
    String account_number;  
    String track2;
    
    
    public String getExpiration_date()
  {
    return this.expiration_date;
  }
  
  public void setExpiration_date(String expiration_date)
  {
    this.expiration_date = expiration_date;
  }
  
  public String getService_code()
  {
    return this.service_code;
  }
  
  public void setService_code(String service_code)
  {
    this.service_code = service_code;
  }
  
  public String getPan()
  {
    return this.pan;
  }
  
  public void setPan(String pan)
  {
    this.pan = pan;
  }
  
  public String getPvki()
  {
    return this.pvki;
  }
  
  public void setPvki(String pvki)
  {
    this.pvki = pvki;
  }
  
  public String getPvv_offset()
  {
    return this.pvv_offset;
  }
  
  public void setPvv_offset(String pvv_offset)
  {
    this.pvv_offset = pvv_offset;
  }
  
  public String getCvv_cvc()
  {
    return this.cvv_cvc;
  }
  
  public void setCvv_cvc(String cvv_cvc)
  {
    this.cvv_cvc = cvv_cvc;
  }
  
  public void setAccount_number(String account_number)
  {
    this.account_number = account_number;
  }
  
  public String getAccount_number()
  {
    return this.account_number;
  }
  
  public String getTrack2()
  {
    return this.track2;
  }
  
  public void setTrack2(String track2)
  {
    this.track2 = track2;
  }
  
  public String toString()
  {
    return "Card DATA: \n\n\r\nExpiration date: " + this.expiration_date + "\r\\nService Code: " + this.service_code + "\r\nPan: " + this.pan + "\r\nAccount number: " + this.account_number + "\r\nPVKI: " + this.pvki + "\r\nPVV / Offset: " + this.pvv_offset + "\r\nCVV / CVC: " + this.cvv_cvc+ "\r\nTrack2: " + this.track2;
  }
}
