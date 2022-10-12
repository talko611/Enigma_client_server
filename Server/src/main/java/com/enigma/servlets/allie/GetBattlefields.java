package com.enigma.servlets.allie;

import com.engine.battlefield.Battlefield;
import com.engine.users.UserManager;
import com.enigma.dtos.ServletAnswers.GameDetailsObject;
import com.enigma.dtos.ServletAnswers.GetMapOfData;
import com.enigma.servlets.ServletsUtils;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@WebServlet("/Allie/Get_battlefields")
public class GetBattlefields extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            UUID userId = UUID.fromString(req.getParameter("id"));
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            if(userManager.getAllieById(userId)!= null){
                Map<UUID, Battlefield> battlefieldMap = userManager.getBattlefields();
                synchronized (UserManager.getBattlefieldLock()){
                    GetMapOfData<GameDetailsObject> responseBody = buildResponseBody(battlefieldMap, userManager);
                    if(responseBody.getListOfUsers().isEmpty())
                        responseBody.setMessage("No battlefields are loaded yet!");
                    Gson gson = new Gson();
                    resp.getWriter().println(gson.toJson(responseBody));
                }
            }else{
                throw new NullPointerException("User is not exist");
            }
        }catch (NullPointerException e){
            //TODO - redirect to login page
        }
    }

    private GetMapOfData<GameDetailsObject> buildResponseBody(Map<UUID, Battlefield> battlefieldMap, UserManager userManager){
        GetMapOfData<GameDetailsObject> body = new GetMapOfData<>();
        battlefieldMap.forEach((id, battlefield)->{
            GameDetailsObject object = new GameDetailsObject();
            object.setBattlefieldName(battlefield.getName());
            object.setuBoatName(userManager.getUBoatById(battlefield.getUBoatId()).getName());
            object.setDecryptionLevel(battlefield.getEnigmaParts().getBattlefieldParts().getDifficulty().toString());
            object.setParticipantsStatus(battlefield.getTeams().size() + "/" + battlefield.getEnigmaParts().getBattlefieldParts().getNumOfAllies());
            object.setGameStatus(battlefield.isGameStarted()? "Active" : "Awaiting");
            body.addUser(id, object);

        });
        return body;
    }
}
