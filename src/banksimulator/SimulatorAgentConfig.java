/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banksimulator;

/**
 *
 * @author scavenger
 */
public class SimulatorAgentConfig {
    public long m_operationsNumber = 300;
    
    public int m_foundsFaultLimit = 3;
    public long m_sleepTime = 1;//ms
    
    public int m_maxDraftValue = 2000;
    public int m_maxDepositValue = 1000;
    public int m_maxTransferValue = 1500;
    public int m_selfDepositValueLimit = 2000;
    public int m_selfDepositNumberLimit = 3;
    
    public SimulatorAgentConfig(){}
    public SimulatorAgentConfig(long operationNumber){
        m_operationsNumber = operationNumber;
    }
    public SimulatorAgentConfig(long operationNumber, 
            long sleepTime, int foundFaultMax, 
            int maxDraftValue, int maxDepositValue,
            int maxTransferValue, int selfDepositLimit,
            int selfDepositCounter){
        
        m_operationsNumber = operationNumber;
        m_sleepTime = sleepTime;
        m_foundsFaultLimit = foundFaultMax;
        m_maxDraftValue = maxDraftValue;
        m_maxDepositValue = maxDepositValue;
        m_maxTransferValue = maxTransferValue;
        m_selfDepositValueLimit = selfDepositLimit;
        m_selfDepositNumberLimit = selfDepositCounter;
    }
    
}
