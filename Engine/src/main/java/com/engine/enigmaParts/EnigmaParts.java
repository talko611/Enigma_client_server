package com.engine.enigmaParts;

import com.engine.enigmaParts.battlefieldParts.BattlefieldParts;
import com.engine.enigmaParts.dmParts.DmParts;
import com.engine.enigmaParts.machineParts.MachineParts;
import com.engine.generated.CTEEnigma;

public class EnigmaParts {
    MachineParts machineParts;
    DmParts dmParts;
    BattlefieldParts battlefieldParts;

    public EnigmaParts(){
        machineParts = new MachineParts();
        dmParts = new DmParts();
        battlefieldParts = new BattlefieldParts();
    }

    public void saveEnigmaParts(CTEEnigma cteEnigma){
        machineParts.saveMachineParts(cteEnigma.getCTEMachine());
        dmParts.saveDmParts(cteEnigma.getCTEDecipher());
        battlefieldParts.saveBattlefieldParts(cteEnigma.getCTEBattlefield());
    }

    public MachineParts getMachineParts() {
        return machineParts;
    }

    public DmParts getDmParts() {
        return dmParts;
    }

    public BattlefieldParts getBattlefieldParts() {
        return battlefieldParts;
    }
}
