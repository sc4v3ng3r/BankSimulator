/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banksimulator;

import banksimulator.model.Account;
import banksimulator.model.Client;
import banksimulator.model.NormalAccount;
import banksimulator.model.SpecialAccount;
import banksimulator.model.operations.BankingMovimentation;
import banksimulator.model.operations.BankingOperation;
import banksimulator.model.operations.Operation;
import banksimulator.model.operations.Transfer;

/**
 *
 * @author scavenger
 */
public class OperationGenerator {
    

    public OperationGenerator() { }
    
    public BankingMovimentation createDraft(double value, Client client){
        BankingMovimentation draft = new BankingMovimentation(
                BankingMovimentation.BANKING_MOVIMENTATION_DRAFT, 
                value, client.getAccount());
        
        draft.setId( (long) draft.hashCode());
        
        return draft;
    }
    
    public BankingMovimentation createDraft(double value, Account account){
        BankingMovimentation draft = new BankingMovimentation(
                BankingMovimentation.BANKING_MOVIMENTATION_DRAFT, 
                value, account);
        
        draft.setId( (long) draft.hashCode());
        
        return draft;
    }
    
    public Transfer createTransfer(Account from, double value, Account to){
        Transfer t  = new Transfer(to, from, value);
        t.setId( (long) t.hashCode());
        return t;
    }
    
    public Transfer createTransfer(Client from, double value, Client to){
        Transfer t = new Transfer(to.getAccount(), from.getAccount(), value);
        
        t.setId( (long) t.hashCode());
        return t;
    }
    
    public BankingMovimentation createDeposit(Client client, double value){
        BankingMovimentation deposit = new BankingMovimentation(
                             BankingMovimentation.BANKING_MOVIMENTATION_DEPOSIT,
                             value, client.getAccount() );
        
        deposit.setId( (long) deposit.hashCode());
        return deposit;
    }
    
    public BankingMovimentation createDeposit(Account account, double value){
         BankingMovimentation deposit = new BankingMovimentation(
                             BankingMovimentation.BANKING_MOVIMENTATION_DEPOSIT,
                             value, account );
         
        deposit.setId( (long) deposit.hashCode());
        return deposit;
    }
     
    public BankingOperation createQuery(Client client){
        BankingOperation query = new BankingOperation(
                 Operation.OPERATION_TYPE_QUERY, client);
        
        query.setId( (long) query.hashCode());
        return query;
     }
    
    public BankingOperation createQuery(Account account){
        BankingOperation query = new BankingOperation(
                 Operation.OPERATION_TYPE_QUERY, account.getOwner());
        
        query.setId( (long) query.hashCode());
        return query;
    }
    
    /*deve mesmo ser synchronized?*/
    public synchronized static void createAccount(Client client, byte accountType, double credit){
        BankingOperation op = null;
        Account account = null;
        
        switch (accountType) {
            case Account.ACCOUNT_NORMAL:
                account = new NormalAccount(client);
                account.setNumber((long) account.hashCode());

                op = new BankingOperation(
                        Operation.OPERATION_TYPE_CREATION, client);
                op.setId( (long) op.hashCode());
                account.registerLog(op);
                break;

            //return op;
            case Account.ACCOUNT_SPECIAL:
                //Random r = new Random();
                /*DEFINIR UMA MANEIRA DE COMO FAZER COM QUE O
                  CREDITO PARA CONTA ESPECIAIS SEJA DIFERENTE PARA
                  DIVERSAS CONTAS. 
                 */

                //double credit = 1000;
                //accountNumber = this.m_accountList.size() + 1;
                account = new SpecialAccount(client);
                account.setNumber((long) account.hashCode());
                ((SpecialAccount) account).setCredit(credit);

                //this.m_accountList.add(account);
                //m_specialAccountsNumber += 1;
                op = new BankingOperation(
                        Operation.OPERATION_TYPE_CREATION, client);
                
                op.setId( (long) op.hashCode());
                account.registerLog(op);
                break;
            default:
                break;
        }
    }
    
}
