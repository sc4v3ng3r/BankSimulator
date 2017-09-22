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
public class Client {
    
    private String m_name;
    private Account m_account;
    private long m_id;
    private boolean m_haveAccount = false;
    
    public Client(String name){
        setName(name);
    }
    
    public Client(String name, Account account){
        setName(name);
        setAccount(account);
        account.setOwner(this);
    }
    
    protected void setAccount(Account account){ 
        if (account == null)
            return;
        m_account = account;
        m_haveAccount = true;
    }
    
    public void setName(String name){ m_name = name; }
    public void setId(long id){ m_id = id; }
    
    public String getName(){ return m_name; }
    public Account getAccount(){ return m_account; }
    public long getId(){ return m_id; }
    
    public boolean haveAccount(){
        return m_haveAccount;
    }
    
}
