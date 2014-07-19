/**
  *This code belongs to Riverbank Solutions Kenya.
  */


package zizi.family.transactions.POS;

/**
 *
 * @author ignatius ojiambo
 */
public class FundsTransferPOSBeanCardPresent  implements ZiziCardTransactionIF{
    private String terminalID;
  private String terminalUser;
  private String cardNumber;
  private double amount;
  private String type;
  private String pinBlock;
  private String customerName;
  private String account1;
  private String account2;
  private String track2;
  private String narration;
  
    /**
     * Get the terminal Id that users used to  swap the card
     * @return 
     */
   @Override
    public String getTerminalID(){
      return this.terminalID;
    }
  
    /**
     * Set the terminal Id card acquire used
     * @param terminalID 
     */
    public void setTerminalID(String terminalID) {
      this.terminalID = terminalID;
    }
    /**
     * Get the terminal user 
     * @return 
     */
    public String getTerminalUser() {
      return this.terminalUser;
    }
    
    /**
     * Set the terminal user
     * @param terminalUser 
     */
    public void setTerminalUser(String terminalUser){
      this.terminalUser = terminalUser;
    }
    /**
     * Get the acquire card number
     * @return 
     */
    public String getCardNumber(){
      return this.cardNumber;
    }
    /**
     * Set the number of card number
     * @param cardNumber 
     */
    public void setCardNumber(String cardNumber) {
      this.cardNumber = cardNumber;
    }
    /**
     * Get the amount to be transfered
     * @return 
     */
    @Override
    public double getAmount(){
      return this.amount;
    }
    /**
     * Set the amount to be transfered
     * @param amount 
     */
    public void setAmount(double amount){
      this.amount = amount;
    }
    /**
     * Get the type of transaction
     * @return 
     */
    @Override
    public String getType()
    {
      return this.type;
    }
    /**
     * Set the type of transaction
     * @param type 
     */
    public void setType(String type){
      this.type = type;
    }
    /**
     * Get the first account number
     * @return 
     */
    public String getAccount1(){
      return this.account1;
    }
    /**
     * Set the first account
     * @param account1 
     */
    public void setAccount1(String account1)
    {
      this.account1 = account1;
    }
    /**
     * Get account two
     * @return 
     */
    public String getAccount2()
    {
      return this.account2;
    }
    /**
     * Set the account two
     * @param account2 
     */
    public void setAccount2(String account2)
    {
      this.account2 = account2;
    }
    /**
     * Get track two
     * @return 
     */
    @Override
    public String getTrack2()
    {
      return this.track2;
    }
  
    public void setTrack2(String track2)
    {
      this.track2 = track2;
    }
  
    @Override
    public String getPinBlock()
    {
      return this.pinBlock;
    }
  
    public void setPinBlock(String pinBlock)
    {
      this.pinBlock = pinBlock;
    }
  
    public String getNarration()
    {
      return this.narration;
    }
  
    public void setNarration(String narration)
    {
      this.narration = narration;
    }
  
      
    @Override
    public String getAccount()
    {
      return this.account2;
    }

 }
