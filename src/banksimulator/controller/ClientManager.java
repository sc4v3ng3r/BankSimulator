/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banksimulator.controller;

import banksimulator.model.Client;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author scavenger
 *
public class ClientManager {
    private static ClientManager m_instance = null;
    private ArrayList<Client> m_clientsList;
    private ReentrantLock m_locker;
    
    private ClientManager(){
        m_clientsList = new ArrayList<>();
        m_locker = new ReentrantLock();
    }
    
    public static synchronized ClientManager getInstance(){
        if (m_instance == null)
            m_instance = new ClientManager();
        
        return m_instance;
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
    
    public Client createClient(String name, byte accountType, double balance){
        
        m_locker.lock();
        try{ 
            Client client = new Client(name);
            System.out.println("CRIANDO CLIENTE NUMERO: " + (m_clientsList.size()+1) );
            client.setId( m_clientsList.size()+1);
            //AccountManager.getInstance().createAccount(accountType, client, balance);
            m_clientsList.add(client);
            return client;
        }
        finally{
            m_locker.unlock();
        }
    }
   
    public synchronized ArrayList<Client> getClientList(){
        m_locker.lock();
        try{
            return m_clientsList;
        }
        finally {
            m_locker.unlock();
        }
    }
    
}*/