package com.engine.enigmaParts.dmParts;

import com.engine.generated.CTEDecipher;
import com.engine.generated.CTEDictionary;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class DmParts {
    private Set<String> dictionary;
    private String excludeChars;

    private static final int maxAgents = 50;

    public void saveDmParts(CTEDecipher cteDecipher){
        this.excludeChars = cteDecipher.getCTEDictionary().getExcludeChars().trim();
        this.dictionary = saveDictionary(cteDecipher.getCTEDictionary());
    }

    private Set<String> saveDictionary(CTEDictionary cteDictionary){
        Set<String> dictionary = new HashSet<>();
        String exclude = "[" + excludeChars + "]";
        Scanner scanner = new Scanner(cteDictionary.getWords().trim());
        String currentWord;
        while(scanner.hasNext()){
            currentWord = scanner.next().replaceAll(exclude,"");
            dictionary.add(currentWord.toUpperCase());
        }
        return dictionary;
    }

    public Set<String> getDictionary() {
        return dictionary;
    }

    public String getExcludeChars() {
        return excludeChars;
    }

    public int getMaxAgents() {
        return maxAgents;
    }
}
