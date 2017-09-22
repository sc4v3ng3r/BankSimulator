/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banksimulator.model;

/**
 *
 * @author scavenger
 */
public class NormalAccount extends Account {
    
    public NormalAccount(Client owner, long number, double balance) {
        super(owner, number);
        this.setType(ACCOUNT_NORMAL);
        setBalance(balance);
    }
    
    public NormalAccount(Client owner){
        super(owner);
        this.setType(ACCOUNT_NORMAL);
        
    }
    
}
