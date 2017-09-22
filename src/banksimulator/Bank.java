/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banksimulator;

import banksimulator.controller.BankClientDataBase;
import banksimulator.model.Client;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author scavenger
 */
public class Bank {
    private String m_name;
    private long m_executedOperationsNumber = 0;
    private double m_inCashValue = 0;
    private final BankClientDataBase m_clientDataBase = new BankClientDataBase();
    private final ReentrantLock m_locker = new ReentrantLock();
    
    
    public Bank(){}
    
    public Bank(String name) {
        setBankName(name);
    }
    
    public void setBankName(final String name){ m_name = name;}
    
    public String getBankName(){ return m_name;}
    
    public synchronized long getExecutedOperationsNumber(){ 
        return m_executedOperationsNumber;
    }
    
    public synchronized void setExecutedOperationsNumber(final long number){
        m_executedOperationsNumber = number;
    }
    
    public synchronized void addClient(Client c){
        /*Quando adicionamos um cliente ao banco,
          ele deve ter sua conta criada!.
        */
        
        m_clientDataBase.addClient(c);
    }
    
    public synchronized Client getClient(final int index){
        return m_clientDataBase.getClient(index);
    }
    
    public synchronized void updateCashValue(final double value){
        m_inCashValue = value;
    }
    
    public synchronized double getBankCashValue(){ return m_inCashValue;}
    
      
}