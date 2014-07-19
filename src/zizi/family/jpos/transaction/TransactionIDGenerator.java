/**
  *This code belongs to Riverbank Solutions Kenya.
  */


package zizi.family.jpos.transaction;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

/**
 *  Generation of random data
 * @author ignatius ojiambo
 */
public class TransactionIDGenerator {
    private static Random random = new Random();
    private static SecureRandom secureRandom = null;
    /**
     * Generate a random cryptographically strong random number
     */
    public TransactionIDGenerator(){
      secureRandom = new SecureRandom();
    }
    
    /**
     * Generate the next random String.
     * @return
     */
    public static String nextStringTransactionId()    {
        return new BigInteger(60, new SecureRandom((System.currentTimeMillis() + "").getBytes())).toString(32);
    }
    /**
     * Generate the next Random integer.
     * @return
     */
    public static String nextIntegerTransactionId()
    {
        int next = random.nextInt();
        next = next < 0 ? next * -1 : next;
        return ("" + next).substring(0, 6);
    }
    /**
     * Generate the next Random integer.
     * @return
     */
    public static String getRandomRRN(){
        return nextIntegerTransactionId() + nextIntegerTransactionId();
    }
    /**
     * Generates a 4 character random String that is meant
     * @return
     */
    public static String nextHSMTransactionID() {
        return getVariableSizeRandomString(4);
    }
    
    /**
     * Generates a random string with length
     * <code>length</code>.
     * @param length - length of the string to be generated.
     * @return
     */
    private static String getVariableSizeRandomString(int length)    {
        return new BigInteger(60, new SecureRandom((System.currentTimeMillis() + "").getBytes())).toString(32).substring(0, length);
    }
    /**
     * 
     * @return 
     */
    private static byte[] getSeed(){
        return (System.currentTimeMillis() + "").getBytes();
    }
}
