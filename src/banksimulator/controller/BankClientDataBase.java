/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banksimulator.controller;

import banksimulator.model.Client;
import java.util.ArrayList;

/**
 *
 * @author scavenger
 */
public class BankClientDataBase {
    private final ArrayList<Client> m_dataBase = new ArrayList<>();
    
    public BankClientDataBase(){}
    
    public void addClient(Client c) {
        if (c == null)
            return;
        
        m_dataBase.add(c);
    }
    
    public Client getClient(int index){
        return m_dataBase.get(index);
    }    
    
}
