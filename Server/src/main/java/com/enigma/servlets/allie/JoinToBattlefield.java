package com.enigma.servlets.allie;

import com.engine.users.battlefield.Battlefield;
import com.engine.users.Allie;
import com.engine.users.UserManager;
import com.enigma.dtos.Enums.GameStatus;
import com.enigma.dtos.ServletAnswers.RequestServerAnswer;
import com.enigma.servlets.ServletsUtils;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.util.UUID;


@WebServlet("/allie/join")
public class JoinToBattlefield extends HttpServlet {
    private final Gson GSON_SERVICE = new Gson();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            UUID clientId  = UUID.fromString(req.getParameter("id"));
            UUID battlefieldId = UUID.fromString(req.getParameter("battlefield"));
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            Allie client = userManager.getAllieById(clientId);
            Battlefield battlefield = userManager.getBattlefieldById(battlefieldId);
            synchronized (battlefield){
                RequestServerAnswer answer = validateRequest(client, battlefield);
                if(answer.isSuccess()) {
                    battlefield.addNewAllie(client);
                    client.setBattlefieldId(battlefieldId);
                    answer.setMessage("Join to battlefield");
                }
                resp.getWriter().println(GSON_SERVICE.toJson(answer));
            }
        }catch (NullPointerException e){
            resp.setStatus(404);
        }
    }

    private RequestServerAnswer validateRequest(Allie user, Battlefield battlefield){
        RequestServerAnswer answer = new RequestServerAnswer();
        answer.setSuccess(true);
        if(battlefield.getGameStatus() == GameStatus.RUNNING || battlefield.getGameStatus() == GameStatus.ENDING){
            answer.setSuccess(false);
            answer.setMessage("Game already started");
        } else if(user.getActiveAgents().size() == 0){
            answer.setSuccess(false);
            answer.setMessage("Cannot join game without agents");
        } else if (battlefield.getTeams().size() == battlefield.getEnigmaParts().getBattlefieldParts().getNumOfAllies()) {
            answer.setSuccess(false);
            answer.setMessage("Game is already full");
        }
        return answer;
    }
}
