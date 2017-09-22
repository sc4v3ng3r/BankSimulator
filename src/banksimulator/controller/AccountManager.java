/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banksimulator.controller;

import banksimulator.model.Account;
import banksimulator.model.Client;
import banksimulator.model.NormalAccount;
import banksimulator.model.SpecialAccount;
import banksimulator.model.operations.BankingMovimentation;
import banksimulator.model.operations.BankingOperation;
import banksimulator.model.operations.Operation;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author scavenger
 
public class AccountManager {
    private static AccountManager m_instance = null;
    private ArrayList<Account> m_accountList;
    private final ReentrantLock m_locker = new ReentrantLock();
    private long m_nomalAccountsNumber;
    private long m_specialAccountsNumber;
    
    
    private AccountManager(){
        m_accountList = new ArrayList<>();
        m_nomalAccountsNumber = 0;
        m_specialAccountsNumber = 0;
    }
    
    public synchronized void clearAll(){
        m_locker.lock();
        try{
        m_instance = null;
        }
        finally{
            m_locker.unlock();
        }
    }
    
    public static synchronized AccountManager getInstance(){
        if (m_instance == null)
            m_instance = new AccountManager();
        
        return m_instance;
    }
    
    public Account createAccount(byte accountType, 
            Client owner, double balance){
       
        Account account = null;

        m_locker.lock();
        try {
            switch (accountType) {
                case Account.ACCOUNT_NORMAL:
                    long accountNumber = this.m_accountList.size() + 1;
                    account = new NormalAccount(owner, accountNumber, balance);
                    this.m_accountList.add(account);
                    m_nomalAccountsNumber += 1;
                    account.registerLog(new BankingOperation(
                            Operation.OPERATION_TYPE_CREATION, owner));

                    if (balance > 0) {
                        account.registerLog(new BankingMovimentation(
                                BankingMovimentation.BANKING_MOVIMENTATION_DEPOSIT,
                                balance, account));
                    }
                    return account;

                case Account.ACCOUNT_SPECIAL:
                    //Random r = new Random();
                    /*DEFINIR UMA MANEIRA DE COMO FAZER COM QUE O
                  CREDITO PARA CONTA ESPECIAIS SEJA DIFERENTE PARA
                  DIVERSAS CONTAS. 
                     

                    double credit = 1000;
                    accountNumber = this.m_accountList.size() + 1;
                    account = new SpecialAccount(owner, accountNumber, balance, credit);

                    this.m_accountList.add(account);
                    m_specialAccountsNumber += 1;
                    account.registerLog(new BankingOperation(
                            Operation.OPERATION_TYPE_CREATION, owner));

                    if (balance > 0) {
                        account.registerLog(new BankingMovimentation(
                                BankingMovimentation.BANKING_MOVIMENTATION_DEPOSIT,
                                balance, account));
                    }
                    return account;

                default:
                    return account;
            }
        }// end of try
        
        finally {
            m_locker.unlock();
        }
    }
    
    public ArrayList<Account> getAccountList(){
        m_locker.lock();
        try{
            return m_accountList;
        } 
        finally{
            m_locker.unlock();
        }
    }
    
    public long getSpecialAccountsNumber(){
        m_locker.lock();
        try{
            return m_specialAccountsNumber;
        } 
        finally{
            m_locker.unlock();
        }
    }
    
    public long getNormalAccountsNumber(){
        m_locker.lock();
        try{
            return m_nomalAccountsNumber;
        } 
        finally{
            m_locker.unlock();
        }
        
    }
}
* */

