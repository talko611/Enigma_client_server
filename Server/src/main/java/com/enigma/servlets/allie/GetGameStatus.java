package com.enigma.servlets.allie;

import com.engine.users.Allie;
import com.engine.users.UserManager;
import com.engine.users.battlefield.Battlefield;
import com.enigma.dtos.Enums.GameStatus;
import com.enigma.dtos.dataObjects.GameDetailsObject;
import com.enigma.servlets.ServletsUtils;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/allie/get_game_status")
public class GetGameStatus extends HttpServlet {
    private final Gson GSON_SERVICE = new Gson();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            UUID userId = UUID.fromString(req.getParameter("id"));
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            Allie allie = userManager.getAllieById(userId);
            if(allie == null){
                userId = UUID.fromString(String.valueOf(req.getAttribute("id")));
                allie = userManager.getAllieById(userId);
                if(allie == null){
                    throw new NullPointerException("User is not found");
                }
            }
            Battlefield battlefield = userManager.getBattlefieldById(allie.getBattlefieldId());
            GameDetailsObject gameStatus;
            synchronized (battlefield){
                if(battlefield.getGameStatus() == GameStatus.RUNNING){
                    updateAgentsGameStarted(allie);
                    allie.setInActiveGame(true);
                }
                //Todo -  decide what to do in other situations
                gameStatus = ServletsUtils.getGameStatus(battlefield, userManager);
            }
            resp.getWriter().println(GSON_SERVICE.toJson(gameStatus));
        }catch (NullPointerException e){
            //Todo - redirect to login
        }
    }
    private void updateAgentsGameStarted(Allie allie){
        allie.getActiveAgents().forEach(agent -> agent.setInActiveGame(true));
    }
}
