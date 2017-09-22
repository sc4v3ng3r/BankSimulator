/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banksimulator.model.operations;
import banksimulator.interfaces.BankingOperationLog;
import banksimulator.model.Account;
import banksimulator.model.SpecialAccount;
import java.util.concurrent.locks.ReentrantLock;
/**
 *
 * @author scavenger
 */
public class Transfer extends BankingMovimentation implements BankingOperationLog{
    Account m_to, m_from;
    double m_value;
    private final ReentrantLock m_locker = new ReentrantLock();
    
    public Transfer(Account to, Account from, double value) {
        super(OPERATION_TYPE_DEBIT,
                BankingMovimentation.BANKING_MOVIMENTATION_TRANSFER, 
                value, from);
        setAccountTo(to);
        setAccountFrom(from);
        m_value = value;
    }
    
    public void setAccountFrom(Account from){ m_from = from; }
    public void setAccountTo(Account to) { m_to = to; }
    
    public Account getAccountFrom(){ return m_from; }
    public Account getAccountto(){ return m_to; }
    
    @Override
    public String getDetails(){
        if (this.getType().compareTo(OPERATION_TYPE_DEBIT) == 0){
            return
                "CLIENTE: " + m_from.getOwner().getName() +
                "\nOPERAÇÃO: " + this.getMovimentationType() + " | " + this.getType() +
                "\nID OPERAÇÃO: " + this.getId() +
                "\nVALOR: " + m_value +
                "\nFAVORECIDO: " + m_to.getOwner().getName() + 
                "\nFAVORECIDO CONTA: " + m_to.getNumber() +
                "\nREALIZADA EM: " + this.getDateString();
        }
        
        return
            "CLIENTE: " + m_to.getOwner().getName() +
            "\nOPERAÇÃO: " + this.getMovimentationType() + " | " + this.getType() +
            "\nID OPERAÇÃO: " + this.getId() +
            "\nVALOR: " + m_value +
            "\nEMISSOR: " +this.m_from.getOwner().getName() +
            "\nEMISSOR CONTA: " + this.m_from.getNumber() +
            "\nREALIZADA EM: " + this.getDateString();
    }
    @Override
    public boolean execute(){
        /*System.out.println( this.m_from.getOwner().getName() + 
                   "EXECUTANDO OPERACAO: " + this);
        */
        
        switch(m_from.getType()){
            case Account.ACCOUNT_NORMAL:
                m_locker.lock();
                try{
                    if (m_from.getBalance() >= m_value) {
                        Transfer toTransfer = new Transfer(m_to, m_from, m_value);
                        toTransfer.setType(OPERATION_TYPE_CREDIT);
                        m_from.setBalance((m_from.getBalance() - m_value));
                        //System.out.println(this.m_from.getOwner().getName() + " REgistrando!");
                        m_from.registerLog(this);

                        m_to.setBalance(m_to.getBalance() + m_value);
                        // System.out.println(this.m_to.getOwner().getName() + " REgistrando!");
                        toTransfer.setId(this.getId());
                        m_to.registerLog(toTransfer);
                        return true;
                    }
                break;
                }
                finally{
                    m_locker.unlock();
                }
            
            case Account.ACCOUNT_SPECIAL:
                /*Tenta fazer a transferencia SEM CREDITO!*/
                m_locker.lock();
                try{
                    if (m_from.getBalance() >= m_value){
                        Transfer toTransfer = new Transfer(m_to, m_from, m_value);
                        toTransfer.setType(OPERATION_TYPE_CREDIT);
                        m_from.setBalance((m_from.getBalance() - m_value));
                        //System.out.println(this.m_from.getOwner().getName() + " REgistrando!");
                        m_from.registerLog(this);

                        m_to.setBalance(m_to.getBalance() + m_value);
                        m_to.registerLog(toTransfer);
                        m_to.registerLog(toTransfer);
                        return true;    
                    }
                
                    else if (m_value <= ((SpecialAccount)m_from).getBalanceTotal()){
                        double balanceTotal = ((SpecialAccount)m_from).getBalanceTotal();
                        Transfer toTransfer = new Transfer(m_to, m_from, m_value);
                        toTransfer.setType(OPERATION_TYPE_CREDIT);
                        m_from.setBalance(m_from.getBalance() - m_value);
                        // System.out.println(this.m_from.getOwner().getName() + " REgistrando!");
                        m_from.registerLog(this);
                        m_to.setBalance(m_to.getBalance() + m_value);
                        //System.out.println(this.m_to.getOwner().getName() + " REgistrando!");
                        m_to.registerLog(toTransfer);
                        return true;
                    }
                    
                }
                finally{
                    m_locker.unlock();
                }
        }
        return false;
    }
}