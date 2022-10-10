package com.enigma.dtos.ServletAnswers;

import javafx.util.Pair;

import java.util.List;
import java.util.UUID;

public class GetBattlefieldsAnswer {
    private List<Pair<UUID, String>> battlefieldList;

    public List<Pair<UUID, String>> getBattlefieldList() {
        return battlefieldList;
    }

    public void setBattlefieldList(List<Pair<UUID, String>> battlefieldList) {
        this.battlefieldList = battlefieldList;
    }
}
