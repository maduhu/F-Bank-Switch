/**
  *This code belongs to Riverbank Solutions Kenya.
  */


package zizi.family.transactions.POS;

/**
 *
 * @author ignatius ojiambo
 */
public class Track2Processor {
    public Track2Bean processTrack2(String track2) {

        //split the string into two blocks.
        Track2Bean bean = new Track2Bean();
        try{     
            //hold the string here.
            String[] s = track2.split("=");

        //set track2 data
        bean.setTrack2(track2);       
        
        //set the pan
        bean.setPan(s[0]);

        //set the expiration date
        bean.setExpiration_date(s[1].substring(0, 4));

        //set the service code
        bean.setService_code(s[1].substring(4, 7));

        //set the pvki
        bean.setPvki(s[1].substring(7, 8));

        //set tghe pvv ot offset
        bean.setPvv_offset(s[1].substring(8, 12));

        //set the cvv or cvc        
        bean.setCvv_cvc(s[1].substring(12, 15));

        bean.setAccount_number(panProcessor(bean.getPan()));
        }catch(Exception e){
            
        }
        return bean;
    }

    private String panProcessor(String pan) {
        return pan.substring(3, pan.length() - 1);
//        return pan.substring(4);
    }
}
