package com.engine.enigmaParts.battlefieldParts;

import com.engine.enums.DecryptionDifficulty;
import com.engine.generated.CTEBattlefield;

import java.util.Locale;

public class BattlefieldParts {
    private String name;
    private int numOfAllies;
    private DecryptionDifficulty difficulty;

    public void saveBattlefieldParts(CTEBattlefield cteBattlefield){
        this.name = cteBattlefield.getBattleName().trim();
        this.numOfAllies = cteBattlefield.getAllies();
        this.difficulty = saveDifficulty(cteBattlefield.getLevel());
    }
    private DecryptionDifficulty saveDifficulty(String level){
        level = level.toUpperCase();
        switch (level){
            case "EASY":
                return DecryptionDifficulty.EASY;
            case "MEDIUM":
                return DecryptionDifficulty.MEDIUM;
            case "HARD":
                return DecryptionDifficulty.HARD;
            case "IMPOSSIBLE":
                return DecryptionDifficulty.IMPOSSIBLE;
        }
        return DecryptionDifficulty.EASY;
    }

    public String getName() {
        return name;
    }

    public int getNumOfAllies() {
        return numOfAllies;
    }

    public DecryptionDifficulty getDifficulty() {
        return difficulty;
    }
}
