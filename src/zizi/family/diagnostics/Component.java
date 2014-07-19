/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zizi.family.diagnostics;

/**
 *  Identify the devices on this distributed system.
 * @author ignatius ojiambo
 */
public class Component {
    //Describe the component
    public String narrative = "";
    //Check the availability of the component if it is online talk to ZIZI
    private boolean isOnline = false;
     /**
     * Variable determining whether the component is 'operationally online' i.e.
     * defines the operational correctness of a component.
     */
    private boolean operationallyOnline = true;
    
    /**
     * Returns boolean device On/Off status
     * @return 
     */
    public boolean isOnline()  {
         return this.isOnline;
    }
        
    /**
     * Sets the devices online status
     * @param isOnline 
     */
    public void setIsOnline(boolean isOnline)  {
        this.isOnline = isOnline;
    }
    /**
     * Sets the device description
     * @param narrative 
     */
    public void setNarrative(String narrative)  {
        this.narrative = narrative;
    }
    /**
     * Get the device narrative
     * @return 
     */
    public String getNarrative()  {
     return this.narrative;
    }
    /**
     * Get the status of the device operationally
     * @return 
     */
    public boolean isOperationallyOnline()  {
     return this.operationallyOnline;
    }
    /**
     * Set device operational status
     * @param operationallyOnline 
     */
    public void setOperationallyOnline(boolean operationallyOnline)  {
     this.operationallyOnline = operationallyOnline;
    }
}
