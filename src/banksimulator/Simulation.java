/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banksimulator;

//import banksimulator.controller.LogManager;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author scavenger
 */

public class Simulation implements Runnable{
    //private Thread[] m_pool = null;
    private SimulatorAgent[] m_agents = null;
    private Thread m_selfThread;
            
    private Bank m_bank;
    private String m_name;
    private boolean m_pause = false;
    private boolean m_interrupt = false;
    private SimulationListener m_listener = null;
    ExecutorService m_executor;
    
    private SimulationConfig m_currentSettings = null;
    private final ReentrantLock m_locker = new ReentrantLock();
    
    private static final String STATUS_READY = "READY";
    private static final String STATUS_RUNNING = "RUNNING";
    private static final String STATUS_DONE = "DONE";
    private static final String STATUS_PAUSED = "PAUSED";
    private static final String STATUS_INTERRUPTED= "INTERRUPTED";
    private static final String SIMULATION_DEFAULT_NAME = "BANK SIMULATION";
    

    
    private String m_status;
    
    public Simulation(SimulationConfig settings, SimulationListener listener){
            m_currentSettings = settings;
            m_name = SIMULATION_DEFAULT_NAME;
            m_bank = new Bank(this.m_name);
            setSimulationListener(listener);
            
        /*if (this.m_currentSettings.m_poolSize == this.m_currentSettings.m_agentSize){
            this.m_pool = new Thread[this.m_currentSettings.m_poolSize];
            this.m_agents = new SimulatorAgent[this.m_currentSettings.m_agentSize];
            
            /*Criação dos agents e das threads
            for(int i=0; i < this.m_currentSettings.m_poolSize; i++) {
                
                m_agents[i] = new SimulatorAgent(
                        (SimulatorAgent.AGENT_NAME_DEFAULT + i),
                        this.m_currentSettings.m_agentConfig, this);
                
                m_bank.addClient( m_agents[i].getSimulatorClient());
                m_pool[i] = new Thread(m_agents[i]);
            }
        }*/
        
        //else {
            //System.out.println("Simulation::CREATING POOL SIZE EXECUTOR");
            this.m_executor = Executors.newFixedThreadPool(this.m_currentSettings.m_poolSize);
            this.m_agents = new SimulatorAgent[this.m_currentSettings.m_agentSize];
            
            for(int i=0; i < this.m_currentSettings.m_agentSize; i++){
                m_agents[i] = new SimulatorAgent(
                        (SimulatorAgent.AGENT_NAME_DEFAULT + i),
                        this.m_currentSettings.m_agentConfig, this);
                m_bank.addClient( m_agents[i].getSimulatorClient());
            }
        //}
        m_status = STATUS_READY;
        
    }
    
    public synchronized int getSimulatorAgentsNumber(){ return this.m_currentSettings.m_agentSize;}
    
    public int getThreadPoolSize(){ return this.m_currentSettings.m_poolSize; }
    
    public void setSimulationListener(SimulationListener listener){
        m_listener = listener;
    }
    public void setSettings(SimulationConfig settings){
        this.m_currentSettings = settings;
    }
    
    public SimulationConfig getSettings(){ return this.m_currentSettings; }
    
    public synchronized SimulatorAgent[] getAgents(){
        
        return m_agents; 
    }
    
    public synchronized int getAgentsNumber(){
        return m_agents.length;
    }
    
    public SimulatorAgent getAgentAt(int index ){
        m_locker.lock();
        try{
            if (index >=0 && index < this.m_agents.length){
                return this.m_agents[index];
            }
        } 
        finally{
            m_locker.unlock();
        }
        return null;
    }
    
    public synchronized long getTotalOperations(){
        return m_currentSettings.m_agentSize * m_currentSettings.m_agentConfig.m_operationsNumber;
    }

    public boolean isPaused(){
        return m_pause;
    }
    
    public void start(){
        m_selfThread = new Thread(this);
        m_selfThread.start();
        
        m_listener.simulationStart();
    }
    
    public synchronized void pause(){
        m_pause = true;
        
        for (SimulatorAgent agent : m_agents) {
            agent.pause();
        }
        
        m_status = STATUS_PAUSED;
        m_listener.simulationPaused();
    }
    
    public synchronized void resume(){
        m_pause = false;        
        for (SimulatorAgent agent : m_agents) {
            agent.resume();
        }
        
        m_status = STATUS_RUNNING;
        m_listener.simulationResumed();
    }
    
    /*NAO FUCIONA AINDA!*/
    public synchronized void interrupt(){
        m_interrupt = true;
        m_selfThread.interrupt();
    }
    
    private void breakSimulation(){
        for(SimulatorAgent agent: m_agents){
            agent.interrupt();
        }
        //isso gera um null pointer exception em alguns casos
        m_executor.shutdown();
        m_status = STATUS_INTERRUPTED;
    }
    
    @Override
    public void run() {
        m_status = STATUS_RUNNING;

        System.out.println(
                "INICIANDO SIMULAÇÃO\n "
                + "THREADS: " + m_currentSettings.m_poolSize
                + " AGENTS: " + m_currentSettings.m_agentSize
                + " OPERACOESS POR AGENT: " + m_currentSettings.m_agentConfig.m_operationsNumber);

        /*if (m_currentSettings.m_poolSize == m_currentSettings.m_agentSize) {
            for (int i = 0; i < m_pool.length; i++) {
                m_pool[i].start();
            }
            
            for (Thread thread: m_pool) {
                try {
                    thread.join();
                } 
                catch (InterruptedException ex) {
                    System.out.println(ex);
                }
            }       
        } */
        
        //else {
            for (SimulatorAgent agent : m_agents) {
                m_executor.execute(agent);
            }
            
            m_executor.shutdown();
            try {
                /*Aguarda por 1h ate as threads finalizarem! */
                m_executor.awaitTermination(1, TimeUnit.HOURS);
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
            
            synchronized(this){
                while(m_interrupt) {
                    for(SimulatorAgent agent: m_agents)
                        agent.interrupt();
                    
                    m_status = STATUS_INTERRUPTED;
                    break;
                }
            }
            
        //}
        /*ISSO SE EU TIVER numero de threads == numero de agents!
            E se eu tiver um numero diferente ?
        */

        if (m_status == STATUS_INTERRUPTED){
            m_listener.simulationInterrupted();
            return;
        }
        
        m_status = STATUS_DONE;
        System.out.println("SIMULATION " + m_status);
        m_listener.simulationEnd();
    }

}
