/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banksimulator.model.operations;
import java.text.SimpleDateFormat;
import java.util.Date;
import banksimulator.interfaces.BankingOperationLog;
import banksimulator.model.Account;
import banksimulator.model.SpecialAccount;
import java.util.concurrent.locks.ReentrantLock;
/**
 *
 * @author scavenger
 * Classe utilizada para representar Operações bancárias que movimentam
 * valores entre contas, DEPÓSITO, SAQUE e TRANSFERÊNCIAS (MOVIMENTAÇÕES BANCÁRIAS).
 */

public class BankingMovimentation extends Operation implements BankingOperationLog {
    private double m_value;
    private String m_movimentationType;
    private Account m_currentAccount;
    private final ReentrantLock m_locker = new ReentrantLock();
    
    
    public static final String BANKING_MOVIMENTATION_DEPOSIT = "DEPÓSITO";
    public static final String BANKING_MOVIMENTATION_TRANSFER = "TRANSFERÊNCIA";
    public static final String BANKING_MOVIMENTATION_DRAFT = "SAQUE";
    
    public BankingMovimentation(String Operationtype, 
            String movimentationType, double value, Account ownerAccount) {
        super(Operationtype);
        this.setMovimentationType(movimentationType);
        this.setValue(value);
        this.setAccount(ownerAccount);
    }
    
    public BankingMovimentation(String movimentationType, 
            double value, Account owner){
            super();
            this.setValue(value);
            this.setMovimentationType(movimentationType);
            this.setAccount(owner);
            switch(movimentationType){
                case BANKING_MOVIMENTATION_DEPOSIT:
                    this.setType(OPERATION_TYPE_CREDIT);
                    break;
                case BANKING_MOVIMENTATION_DRAFT:
                    this.setType(OPERATION_TYPE_DEBIT);
                    break;
            }   
    }
    
    public void setValue(double value){ this.m_value = value; }
    public void setMovimentationType(String type){ this.m_movimentationType = type;}
    private void setAccount(Account owner){ this.m_currentAccount = owner;}
    
    public Account getAccount(){ return this.m_currentAccount; }
    public String getMovimentationType(){ return this.m_movimentationType; }
    public double getValue(){ return this.m_value; }

    @Override
    public String getDetails() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
        Date date = new Date(this.getDate());
        
        return  "CLIENTE: " + this.m_currentAccount.getOwner().getName() +
                "\nOPERAÇÃO: " + this.getMovimentationType() + "  |  " + this.getType() +
                "\nID OPERAÇÃO: " + this.getId() +
                "\nCONTA NÚMERO: " + this.m_currentAccount.getNumber() +
                "\nCONTA TIPO: " + this.m_currentAccount.getAccountDescription() +
                "\nVALOR: " + this.getValue() +
                "\nREALIZADA EM: " + this.getDateString();           
    }

    @Override
    public boolean execute() {
           /*System.out.println( this.m_currentAccount.getOwner().getName() + 
                   "EXECUTANDO OPERACAO: " + this);
           */
       switch(this.getMovimentationType()) {
           
           case BANKING_MOVIMENTATION_DEPOSIT:
               //System.out.println("DEPOSITO " + this.m_currentAccount.getOwner().getName());
               if (m_value <= 0 ){
                   //System.out.println("DEPOSITO valor < 0");
                   return false;
               }
               //System.out.println("EXECUTANDO DEPOSITO!");
               m_locker.lock();
               try{
                    this. m_currentAccount.setBalance( 
                        (m_currentAccount.getBalance() + m_value) );
                    //System.out.println(this.m_currentAccount.getOwner().getName() + " REgistrando!");
                    this.m_currentAccount.registerLog(this);
                    return true;
               }
               finally{ m_locker.unlock();}
                   
           case BANKING_MOVIMENTATION_DRAFT:
               //System.out.println("SAQUE " + this.m_currentAccount.getOwner().getName());
               switch(m_currentAccount.getType()) {
                   
                   case Account.ACCOUNT_NORMAL:
                       //System.out.println("SAQUE CONTA NORMAL" + this.m_currentAccount.getOwner().getName());
                       m_locker.lock();
                       try{
                            if ( this.m_currentAccount.getBalance() >= m_value ){
                                this.m_currentAccount.setBalance( (this.m_currentAccount.getBalance() - m_value) );
                                this.m_currentAccount.registerLog(this);
                                return true;
                            } 
                            break;
                        } 
                        finally{
                            m_locker.unlock();
                        }
                   case Account.ACCOUNT_SPECIAL:
                       m_locker.lock();
                       try{
                            double balance = this.m_currentAccount.getBalance();
                            if( m_value <= balance){
                          
                                this.m_currentAccount.setBalance( balance - m_value );
                                this.m_currentAccount.registerLog(this);
                                return true;
                            } 
                            
                           else if (m_value <= ((SpecialAccount)m_currentAccount).getBalanceTotal() ){
                                double balanceTotal = ((SpecialAccount)m_currentAccount).getBalanceTotal();
                                this.m_currentAccount.setBalance(balance - m_value);
                                this.m_currentAccount.registerLog(this);
                                return true;
                            }
                            break;
                       }
                       finally{
                           m_locker.unlock();
                       }
               }
               
           default:
               /*System.out.println("OPERAÇÃO NÃO REALIZADA!: " +
                "CLIENTE: " + this.m_currentAccount.getOwner().getName() +
                "\nOPERAÇÃO: " + this.getMovimentationType() + "  |  " + this.getType() +
                "\nID OPERAÇÃO: " + this.getId() +
                "\nCONTA NÚMERO: " + this.m_currentAccount.getNumber() +
                "\nCONTA TIPO: " + this.m_currentAccount.getAccountDescription() +
                "\nVALOR: " + this.getValue() +
                "\nNÃO REALIZADA EM: " + this.getDateString() + "\n\n");*/
               break;
       }
       return false;
       
    }
    
}