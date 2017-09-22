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
public interface SimulationListener {
    public void simulationStart();
    public void simulationPaused();
    public void simulationResumed();
    public void simulationInterrupted();
    public void simulationEnd();
    
}
