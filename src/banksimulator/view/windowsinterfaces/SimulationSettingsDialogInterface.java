/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banksimulator.view.windowsinterfaces;

import banksimulator.SimulationConfig;

/**
 *
 * @author scavenger
 */
public interface SimulationSettingsDialogInterface {
    public void onConfirm(SimulationConfig settings);
    public void onCancel(SimulationConfig settings);
}
