/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banksimulator.controller;

import banksimulator.interfaces.BankingOperationLog;
import banksimulator.model.Log;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author scavenger
 *
public class LogManager {
    private static LogManager m_instance = null;
    private ArrayList<Log> m_systemLogList;
    private ReentrantLock m_locker;
    
    private LogManager(){
        m_systemLogList = new ArrayList<>();
        m_locker = new ReentrantLock();
    }
    
    public synchronized void clearAll(){
        m_instance = null;
    }
    public static synchronized LogManager getInstance(){
        if (m_instance == null)
            m_instance = new LogManager();
        
        return m_instance;
    }
    
    public Log createLog(BankingOperationLog o){
        Log g = new Log(o);
        m_locker.lock();
        try{
            m_systemLogList.add(g);
            return g;
        } 
        finally{
            m_locker.unlock();
            
        }
        
    }
    
    public void addLog(Log log){
        if (log != null){
            m_locker.lock();
            try{
                m_systemLogList.add(log);
            }
            finally{
                m_locker.unlock();
            }
        }
    }
    
    public ArrayList<Log> getSystemLogList(){
        m_locker.lock();
        try{
            return m_systemLogList;
        }
        finally{
            m_locker.unlock();
        }
    }
    
}*/
