/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banksimulator.controller;

import banksimulator.model.operations.Operation;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author scavenger
 
public class OperationManager {
    private static OperationManager m_instance = null;
    private CopyOnWriteArrayList<Operation> m_list;
    private ReentrantLock m_locker;
    
    private OperationManager(){
        m_list = new CopyOnWriteArrayList<>();
        m_locker = new ReentrantLock();
    }
    
    public synchronized static OperationManager getInstance(){
        if (m_instance == null)
            m_instance = new OperationManager();
        return m_instance;
    }
    
    public void addOperation(Operation op){
        m_locker.lock();
        try{
            m_list.add(op);
        }
        finally{
            m_locker.unlock();
        }
    }
    
    public CopyOnWriteArrayList<Operation> getList(){
        m_locker.lock();
        try{
            return m_list;
        }
        finally{
            m_locker.unlock();
        }
    }
}
*/