/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banksimulator.model;

//import banksimulator.controller.LogManager;
import banksimulator.interfaces.BankingOperationLog;

/**
 *
 * @author scavenger
 */

public class Log {
    BankingOperationLog m_operation;
    
    public Log(BankingOperationLog operation){
        m_operation = operation;
        //LogManager.getInstance().addLog(this);
    }
    
    public void dump(){
        System.out.println(m_operation.getDetails());
    }
    
    public String getReport(){
        return this.m_operation.getDetails() + "\n===============================\n\n";
    }    
}
