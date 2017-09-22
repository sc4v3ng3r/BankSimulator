/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banksimulator;

//import banksimulator.controller.AccountManager;
import banksimulator.interfaces.BankingOperationLog;
import banksimulator.model.Account;
import banksimulator.model.Client;
import banksimulator.model.operations.Operation;
import banksimulator.view.GeneralLogAreaWriterInterface;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author scavenger
 */
public class SimulatorAgent implements Runnable{

    private Client m_selfClient;
    private String m_name;
    private boolean m_pause = false;
    private boolean m_interrupt = false;
    
    private final OperationGenerator m_generator = new OperationGenerator();
    private final Random m_numberGenerator = new Random();
    private final Simulation m_simulation;
    private SimulatorAgentConfig m_settings;
    
    private CopyOnWriteArrayList<Operation> m_operationList;
    private CopyOnWriteArrayList<BankingOperationLog> m_logList = new CopyOnWriteArrayList<>();
    private final ReentrantLock m_locker = new ReentrantLock();
    private GeneralLogAreaWriterInterface m_listener = null;
    public static final String AGENT_NAME_DEFAULT = "SIMULATOR AGENT ";
    
    
    
    /*contadores*/
    private long m_operationsCounter = 0;
    private byte m_foundsFaultCounter = 0;
    private int m_depositCounter = 0;
    
    
    public SimulatorAgent(String name, 
            SimulatorAgentConfig settings, Simulation simulation) {
        
        m_settings = settings;
        m_simulation = simulation;
        setName(name);
        /*A CONTA SERA NORMAL OU ESPECIAL (0,1) RESPECTIVAMENTE!*/
        byte accountType = (byte) m_numberGenerator.nextInt(2);
        double credit = 0;
        
        if (accountType == Account.ACCOUNT_SPECIAL)
            credit = (double) m_numberGenerator.nextInt(1001);
        
        m_selfClient = new Client(name);
        m_selfClient.setId( (long) m_selfClient.hashCode() );
        OperationGenerator.createAccount(m_selfClient, accountType, credit);
        
        m_operationList = new CopyOnWriteArrayList<>();
    }
    
    public void setListener(GeneralLogAreaWriterInterface listener){
        m_listener = listener;
    }
    
    public void setName(String name){ m_name = name;}
    
    public String getName(){ return m_name; }
    public Client getSimulatorClient(){ return m_selfClient; }
    
    private Operation spawnOperation(){
        Operation op = null;
        
        if (m_foundsFaultCounter ==  m_settings.m_foundsFaultLimit){
            m_foundsFaultCounter = 0;
            return m_generator.createDeposit(m_selfClient,
                    (double) m_numberGenerator.nextInt(m_settings.m_maxDepositValue));
        }
        
        /*[0,4[*/
        int operationNumber = m_numberGenerator.nextInt(4);
        /*
          0 -> SALDO
          1 -> DEPOSITO
          2 -> SAQUE
          3 -> TRANSFERENCIA
        */
        switch(operationNumber){
            case 0: /*QUERY*/
                op = m_generator.createQuery(m_selfClient);
                break;
            
            case 1:/*DEPOSIT*/
                /*A cada n depositos da propria conta, 
                  o Agent deposita um determinado
                  valor na conta de outro agent.
                */
                double value = (double)m_numberGenerator.nextInt( m_settings.m_maxDepositValue );
                m_foundsFaultCounter = 0;
                
                if (m_depositCounter == m_settings.m_selfDepositNumberLimit){
                    /*Fazemos um depósito em uma conta de outro "AGENT_NAME_DEFAULT/CLIENT"*/
                    
                    int size = m_simulation.getAgentsNumber();
                    
                    /*AQUI AINDA CORRE O RISCO DE O AGENTE ESCOLHIDO SER O MESMO AGENTE!*/
                    SimulatorAgent agent = m_simulation.getAgentAt(
                            m_numberGenerator.nextInt(size) );
                    
                    op = m_generator.createDeposit(agent.m_selfClient, value);
                    m_depositCounter = 0;
                    
                } else { // depósito em PRÓPRIA CONTA
                    op = m_generator.createDeposit(m_selfClient, value);
                    m_depositCounter+=1;
                }
                break;
            
            case 2:/*DRAFT*/
                value = (double)m_numberGenerator.nextInt( m_settings.m_maxDraftValue );
                op = m_generator.createDraft(value, m_selfClient);
                break;
            
            case 3: /*TRANSFER*/
                value = (double)m_numberGenerator.nextInt( m_settings.m_maxTransferValue );
                
                int agentsSize = m_simulation.getAgentsNumber();
                //deve existir pelo menos 2 agents para acontecer uma transferencia!
                if (agentsSize > 1){
                    int index;
                    Account toTransfer;
                    
                    //garante que a transferencia não seja feita p/ a mesma conta!
                    while(true){
                        index = m_numberGenerator.nextInt( agentsSize );
                        toTransfer = m_simulation.getAgentAt(index).getSimulatorClient().getAccount();
                        if (toTransfer.getNumber() != m_selfClient.getAccount().getNumber())
                            break;
                     }
                    
                    op = m_generator.createTransfer(m_selfClient.getAccount(),
                            value,  toTransfer );
                }
                break;
        }
        
        return op;
    }
    
    public synchronized void pause(){
        m_pause = true;
    }
    
    public synchronized void interrupt(){
        m_interrupt = true;
    }
    
    public synchronized void resume(){
        m_pause = false;
        notify();
    }
    
    @Override
    public void run() {
        /*
            A primeira operação será um depósito?
            se sim: 
              [*] executa o deposito
              [*] registra a operação
              [*] incrementa o contador de operações
              [*] entra no loop !
            se não:
              [*] entra no loop
        
        */
        m_interrupt = false;
        while (m_operationsCounter < m_settings.m_operationsNumber) {
                       
            synchronized(this){
                while(m_pause){
                    try {
                        wait();
                    } 
                    catch (InterruptedException ex) {
                        System.out.println(ex);
                    }
                }
                
                while(m_interrupt){
                    System.out.println(m_name + " RETORNANDO ");
                    return;
                }
            }
            
            Operation op = spawnOperation();
            
            if (op == null) {
                /*VERIFICAR CORRETAMENTE ESSA CONDIÇÃO!*/
                m_operationsCounter -= 1;
            }
            
            else {// se esta tudo certo!
                
                m_operationList.add(op);
                if (!op.execute()) {//se a operação não foi executada com sucesso!
                    /*A gente vai seguir em frente e regsitar
                    como uma operação mal sucedida!*/
                    m_foundsFaultCounter -= 1;
                    m_operationsCounter-=1;
                } 
                
                else {
                    m_logList.add( (BankingOperationLog)op);
                    
                    if (m_listener != null) {
                        String logToWrite = this.m_name
                                + " EXECUTOU OPERAÇÃO DE " + op.getType() + "\n";
                        m_locker.lock();
                        try{
                            m_listener.writeOnLogArea(logToWrite);
                        }
                        finally{
                            m_locker.unlock();
                        }
                    }
                }

                try {
                    Thread.sleep(m_settings.m_sleepTime);
                } 
                
                catch (InterruptedException ex) {
                    System.out.println(ex);
                }
                
                m_operationsCounter += 1;
            }
        
        }// enf of run loop!
        /*GERAR UMA OPERACAO
         *REGISTRAR A OPERACAO
         * EXECUTAR A OPERACAO
         */
    } // end of run Method!
    
    public synchronized CopyOnWriteArrayList<Operation> getOperationList(){
        return m_operationList;
    }
    
    public synchronized CopyOnWriteArrayList<BankingOperationLog> getLogList(){
        return m_logList;
    }
    
}
