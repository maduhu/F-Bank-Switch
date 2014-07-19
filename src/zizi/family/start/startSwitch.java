package zizi.family.start;

import zizi.family.transaction.env.ZiziInitializer;

/**
 * Establish a connection with the dashboard
 * @author ignatius ojiambo
 */
public class startSwitch {    
        public static void main(String [] args){            
            ZiziInitializer ziziInitializer = ZiziInitializer.getInstance();
                      ziziInitializer.initComponents();
        }
}
