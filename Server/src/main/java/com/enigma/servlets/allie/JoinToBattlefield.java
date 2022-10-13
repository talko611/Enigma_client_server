package com.enigma.servlets.allie;

import com.engine.battlefield.Battlefield;
import com.engine.users.Allie;
import com.engine.users.UserManager;
import com.enigma.dtos.ServletAnswers.RequestServerAnswer;
import com.enigma.servlets.ServletsUtils;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;


@WebServlet("/allie/join")
public class JoinToBattlefield extends HttpServlet {
    private static final Gson GSON_SERVICE = new Gson();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            UUID clientId  = UUID.fromString(req.getParameter("id"));
            UUID battlefieldId = UUID.fromString(req.getParameter("battlefield"));
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            Allie client = userManager.getAllieById(clientId);
            //Todo - handle cases client is not found
            Battlefield battlefield = userManager.getBattlefieldById(battlefieldId);
            //Todo - handle cases battlefield is not found
            synchronized (battlefield){
                RequestServerAnswer answer = validateRequest(client, battlefield);
                if(answer.isSuccess()) {
                    removeFromPreviousBattlefield(userManager, client);
                    battlefield.addNewAllie(client);
                    client.setBattlefieldId(battlefieldId);
                    answer.setMessage("Join to battlefield");
                }
                resp.getWriter().println(GSON_SERVICE.toJson(answer));
            }

        }catch (NullPointerException e){
            //Todo - redirect to log in page
        }
    }

    private void removeFromPreviousBattlefield(UserManager userManager, Allie allie){
        Battlefield battlefield = userManager.getBattlefieldById(allie.getBattlefieldId());
        if(battlefield != null)
            battlefield.removeAllie(allie);
    }

    private RequestServerAnswer validateRequest(Allie user, Battlefield battlefield){
        RequestServerAnswer answer = new RequestServerAnswer();
        answer.setSuccess(true);
        if(battlefield.isGameStarted()){
            answer.setSuccess(false);
            answer.setMessage("Game already started");
        } else if(user.getAgentList().size() == 0){
            answer.setSuccess(false);
            answer.setMessage("Cannot join game without agents");
        } else if (battlefield.getTeams().size() == battlefield.getEnigmaParts().getBattlefieldParts().getNumOfAllies()) {
            answer.setSuccess(false);
            answer.setMessage("Game is already full");
        }
        return answer;
    }
}
