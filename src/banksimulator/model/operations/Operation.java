/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banksimulator.model.operations;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author scavenger
 */

public abstract class Operation {
    private String m_operationType;
    private long m_date;
    protected long m_executionTime;
    private long m_id;
   // private String m_description;
    public static final String OPERATION_TYPE_CREATION = "CRIAÇÃO";
    public static final String OPERATION_TYPE_QUERY = "CONSULTA";
    public static final String OPERATION_TYPE_CREDIT = "CRÉDITO";
    public static final String OPERATION_TYPE_DEBIT = "DÉBITO";
    
    public Operation(String type) {
        setType(type);
        setDate(System.currentTimeMillis());
        setId((int)this.hashCode());
    }
    
    public Operation() {
        setDate(System.currentTimeMillis());
        setId((int)this.hashCode());
    }
    
    public final void setType(String type ){ m_operationType = type; }
    private void setDate(long date){ m_date = date; }
    //public final void setDescription(String desc){ m_description = desc;}
    public final void setId(long id){ m_id = id; }
    
    public final String getType(){ return m_operationType; }
    public final long getDate(){ return m_date; }
    
    public final String getDateString(){ 
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
        Date date = new Date(m_date);
        return dateFormatter.format(date); 
    }
    //public final String getDescription(){ return m_description; }
    public final long getId(){ return m_id; }
    
    public abstract boolean execute();
}