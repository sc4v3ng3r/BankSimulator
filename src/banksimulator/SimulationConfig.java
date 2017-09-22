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
public class SimulationConfig {
    public int m_poolSize = 300;
    public int m_agentSize = 300;
    public SimulatorAgentConfig m_agentConfig = new SimulatorAgentConfig();

    public SimulationConfig() {
        
    }
    
    SimulationConfig(int threadPoolSize, 
            int agentsNumber, SimulatorAgentConfig agentsSettings){
        m_poolSize = threadPoolSize;
        m_agentSize = agentsNumber;
        m_agentConfig = agentsSettings;
    }
    
    @Override
    public String toString(){
        return "THREADS: " + m_poolSize 
                + "AGENTES: " + m_poolSize
                + "OPERACOES: " + m_agentConfig.m_operationsNumber;
    }
    
}
