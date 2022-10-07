package com.enigma.servlets.uBoat;

import com.engine.battlefield.Battlefield;
import com.engine.users.UserManager;
import com.enigma.dtos.ClientDataTransfer.EncryptMessageData;
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

@WebServlet("/UBoat/encrypt")
public class EncryptMessage extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            UUID clientId = UUID.fromString(req.getParameter("id"));
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            Battlefield battlefield = userManager.getBattlefieldById(userManager.getUBoatById(clientId).getBattlefieldId());
            if(battlefield == null){
                resp.setStatus(401);
            }else{
                Machine machine = battlefield.getMachine();
                Gson gson = new Gson();
                EncryptMessageData data = gson.fromJson(req.getReader(), EncryptMessageData.class);
                synchronized (battlefield.getMachine()){
                    if(isLettersAreValid(data.getMessage(), machine.getKeyboard()) &&
                            isAllWordAreInDic(battlefield.getEnigmaParts().getDmParts().getDictionary(), data.getMessage())){
                        battlefield.setEncryptedMessage(data.getMessage());
                        battlefield.setMessageInitialConfiguration(machine.getCurrentConfiguration());
                        data = ServletsUtils.getEngine(getServletContext()).encryptDecrypt(data.getMessage(), machine);
                        battlefield.setDecryptedMessage(data.getMessage());
                        resp.setStatus(200);
                        data.setMessage("Message decrypted successfully!");
                    }else{
                        resp.setStatus(401);
                        data.setMessage("Message contains invalid characters or words that are not in the game dictionary");
                    }
                    resp.getWriter().println(gson.toJson(data));
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
