package com.enigma.servlets.uBoat;

import com.engine.users.battlefield.Battlefield;
import com.engine.users.UserManager;
import com.enigma.dtos.dataObjects.EncryptMessageData;
import com.enigma.machine.Machine;
import com.enigma.machine.parts.keyboard.Keyboard;
import com.enigma.servlets.ServletsUtils;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@WebServlet("/uBoat/encrypt")
public class EncryptMessage extends HttpServlet {
    private final Gson GSON_SERVICE = new Gson();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try{
            UUID clientId = UUID.fromString(req.getParameter("id"));
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            Battlefield battlefield = userManager.getBattlefieldById(userManager.getUBoatById(clientId).getBattlefieldId());
            if(battlefield == null){
                resp.setStatus(401);
            } else if (battlefield.getMachine().getInitialConfiguration() == null) {
                resp.setStatus(402);
            } else{
                Machine machine = battlefield.getMachine();
                EncryptMessageData clientData = GSON_SERVICE.fromJson(req.getReader(), EncryptMessageData.class);
                synchronized (battlefield){
                    if(isLettersAreValid(clientData.getSource(), machine.getKeyboard()) &&
                            isAllWordAreInDic(battlefield.getEnigmaParts().getDmParts().getDictionary(), clientData.getSource())){
                        String currentConfiguration = machine.getCurrentConfiguration();
                        clientData = ServletsUtils.getEngine(getServletContext()).encryptDecrypt(clientData, machine);
                        battlefield.setEncryptedMessage(clientData.getEncrypted());
                        battlefield.setDecryptedMessage(clientData.getSource());
                        battlefield.setMessageConfiguration(currentConfiguration);
                        clientData.setMessage("Message decrypted successfully!");
                        resp.setStatus(200);
                    }else{
                        resp.setStatus(401);
                        clientData.setMessage("Message contains invalid characters or words");
                    }
                    resp.getWriter().println(GSON_SERVICE.toJson(clientData));
                }
            }
        }catch (NullPointerException e){
            //Todo- redirect to login page
        }
    }

    private boolean isLettersAreValid(String src, Keyboard keyboard){
        src = src.toUpperCase();
        for(int i = 0; i < src.length() ; ++i){
            if(!keyboard.isKeyExists(String.valueOf(src.charAt(i)))){
                return false;
            }
        }
        return true;
    }

    private boolean isAllWordAreInDic(Set<String> dictionary, String src){
        src = src.toUpperCase();
        List<String> words = Arrays.asList(src.split(" "));
        for(String word : words){
            if(!dictionary.contains(word)){
                return false;
            }
        }
        return true;
    }
}
