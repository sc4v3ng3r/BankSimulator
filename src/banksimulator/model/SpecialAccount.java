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
public class SpecialAccount extends Account {
    private double m_credit = 1000;
    /* IMPLEMENTACAO DO CREDITO É REGRA DE NECÓCIO, NÃO DEVE FICAR DENTRO DA CLASSE!*/
    
    public SpecialAccount(Client owner, long number, 
            double balance, double credit) {
        super(owner ,number);
        setBalance(balance);
        setType(ACCOUNT_SPECIAL);
        setCredit(credit);
    }
    
    public SpecialAccount(Client owner){
        super(owner);
        setType(ACCOUNT_SPECIAL);
    }
    
    public final void setCredit(double credit){ m_credit = credit;}
    public final double getCredit(){ return m_credit; }
    
    public double getBalanceTotal(){
        m_locker.lock();
        try{
            return getCredit() + super.getBalance() ;
        } finally{
            m_locker.unlock();
        }
    }
}
