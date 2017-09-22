/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banksimulator.model;
import java.util.ArrayList;
import banksimulator.interfaces.BankingOperationLog;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;
/**
 *
 * @author scavenger
 */
public abstract class Account {
    private long m_number;
    private double m_value = 0;
    private Client m_owner;
    private byte m_type;
    private CopyOnWriteArrayList<Log> m_logList;
    protected ReentrantLock m_locker;
    
    public static final byte ACCOUNT_NORMAL = 0;
    public static final byte ACCOUNT_SPECIAL = 1;
    
    public Account(Client owner,long number) {
       /*ESSE CONSTRUTOR VAI DEIXAR DE EXISTIR!*/
        m_logList = new CopyOnWriteArrayList<>();
        setNumber(number);
        setOwner(owner);    
        owner.setAccount(this);
        m_locker = new ReentrantLock();
    }
    
    public Account(Client owner){
        m_logList = new CopyOnWriteArrayList<>();
        setNumber((long)this.hashCode());
        setOwner(owner);
        owner.setAccount(this);
        m_locker = new ReentrantLock();
    }
    
    public final void setNumber(long number){ m_number = number;}
    public final void setType(byte type){ m_type = type; }
    
    public final void setOwner(Client owner){ 
        m_owner = owner;
        m_owner.setAccount(this);
    }
    
    public final void setBalance(double value){ 
        m_locker.lock();
        try {
            m_value = value; 
        }
        finally{
            m_locker.unlock();
        }
    }
    
    public final long getNumber(){ return m_number; }
    
    public double getBalance(){
        m_locker.lock();
        try{
            return m_value; 
        }
        finally{
            m_locker.unlock();
        }
    }
    
    public final byte getType(){ return m_type; }
    
    public final String getAccountDescription(){
        if (m_type == ACCOUNT_NORMAL)
            return "NORMAL";
        else return "ESPECIAL";
    }
    public final Client getOwner(){ return m_owner; }
    
    public final void registerLog(BankingOperationLog operation){
       m_locker.lock();
       try{
            m_logList.add(new Log(operation));
        }
        finally{
            m_locker.unlock();
        }
    }
    
    public final CopyOnWriteArrayList<Log> getLogList(){ 
        m_locker.lock();
        try{
            return m_logList; 
        }
        finally{
            m_locker.unlock();
        }
    }
    
}
