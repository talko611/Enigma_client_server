package com.enigma.servlets.allie;

import com.engine.users.battlefield.Battlefield;
import com.engine.users.UserManager;
import com.enigma.dtos.dataObjects.GameDetailsObject;
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

@WebServlet("/allie/get_battlefields")
public class GetBattlefields extends HttpServlet {
    private final Gson GSON_SERVICE = new Gson();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            UUID userId = UUID.fromString(req.getParameter("id"));
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            if(userManager.getAllieById(userId)!= null){
                Map<UUID, Battlefield> battlefieldMap = userManager.getBattlefields();
                synchronized (UserManager.getBattlefieldLock()){
                    GetMapOfData<GameDetailsObject> responseBody = buildResponseBody(battlefieldMap, userManager);
                    if(responseBody.getData().isEmpty())
                        responseBody.setMessage("No battlefields are loaded yet!");
                    resp.getWriter().println(GSON_SERVICE.toJson(responseBody));
                }
            }else{
                throw new NullPointerException("User is not exist");
            }
        }catch (NullPointerException e){
            resp.setStatus(404);
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
            object.setGameStatus(battlefield.getGameStatus());
            body.addUser(id, object);
        });
        return body;
    }
}
