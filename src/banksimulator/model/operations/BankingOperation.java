/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banksimulator.model.operations;

import banksimulator.interfaces.BankingOperationLog;
import banksimulator.model.Account;
import banksimulator.model.Client;
import banksimulator.model.SpecialAccount;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author scavenger
 * Essa classe é utilizada como representação de operações bancárias 
 * que não são MOVIMENTAÇÕES BANCÁRIAS, ou seja, não são operações
 * que envolvem VALORES em suas execuções, tais como CRIAR UMA CONTA,
 * SALDO DE UMA CONTA.
 */
public class BankingOperation extends Operation implements BankingOperationLog {
    private Client m_client;
    private double m_oldBalance;
    private final ReentrantLock m_locker = new ReentrantLock();
    
    public BankingOperation(String type, Client client) {
        super(type);
        m_client = client;
    }
    
    public Client getClient(){ return m_client; }
     
    @Override
    public String getDetails() {
        m_locker.lock();
        try{
            Account account = this.m_client.getAccount();
            switch(this.getType()){
                case OPERATION_TYPE_QUERY: 
                
                    switch(this.m_client.getAccount().getType()){
                        case Account.ACCOUNT_NORMAL:
                            return
                                "CLIENTE: " + this.m_client.getName() +
                                "\nOPERAÇÃO: " + this.getType() +
                                "\nID OPERAÇÃO: " + this.getId() +
                                "\nCONTA NÚMERO: " + account.getNumber() +
                                "\nCONTA TIPO: " + account.getAccountDescription() +
                                "\nSALDO: " + this.m_oldBalance + //armengue! todo operacao guarda o estado anterior do saldo da conta!
                                "\nEXECUTADA EM: " + this.getDateString();
                    
                        case Account.ACCOUNT_SPECIAL:
                            double credit = ((SpecialAccount)account).getCredit();
                            return
                                "CLIENTE: " + this.m_client.getName() +
                                "\nOPERAÇÃO: " + this.getType() +
                                "\nID OPERAÇÃO: " + this.getId() +
                                "\nCONTA NÚMERO: " + account.getNumber() +
                                "\nCONTA TIPO: " + account.getAccountDescription() +
                                "\nCRÉDITO: " +  credit +
                                "\nSALDO PARCIAL: " + this.m_oldBalance +
                                "\nSALDO TOTAL: " +  (credit + m_oldBalance) +//armengue! todo operacao guarda o estado anterior do saldo da conta!
                                "\nEXECUTADA EM: " + this.getDateString();
                    }
                
                default:
                    return 
                    "CLIENTE: " + m_client.getName() +
                    "\nOPERAÇÃO: " + this.getType() +
                    "\nID OPERAÇÃO: " + this.getId() +
                    "\nCONTA NÚMERO: " + account.getNumber() +
                    "\nCONTA TIPO: " + account.getAccountDescription() +
                    "\nEXECUTADA EM: " + this.getDateString();
            }
        }
        finally{
            m_locker.unlock();
        }
    }
    
    @Override
    public boolean execute() {
        m_locker.lock();
        try{
            m_oldBalance = m_client.getAccount().getBalance();
        //System.out.println(this.m_client.getName() + " EXECUTANDO " + this);
            m_client.getAccount().registerLog(this);
        //m_client.getAccount().
            return true;
        }
        finally{
            m_locker.unlock();
        }
    }
    
}