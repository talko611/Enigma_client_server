package com.enigma.servlets.uBoat;

import com.engine.users.Uboat;
import com.engine.users.UserManager;
import com.engine.users.battlefield.Battlefield;
import com.enigma.dtos.Enums.GameStatus;
import com.enigma.dtos.dataObjects.Candidate;
import com.enigma.servlets.ServletsUtils;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebServlet("/uBoat/get_candidates")
public class GetCandidates extends HttpServlet {
    private static final Gson GSON_SERVICE = new Gson();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            UUID uBoatId = UUID.fromString(req.getParameter("id"));
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            Uboat uboat = userManager.getUBoatById(uBoatId);
            Battlefield battlefield = userManager.getBattlefieldById(uboat.getBattlefieldId());
            List<Candidate> candidateList = new ArrayList<>();
            battlefield.getCandidates().drainTo(candidateList);
            candidateList.forEach(candidate -> {
                if(candidate.getDecryption().equalsIgnoreCase(battlefield.getDecryptedMessage()) &&
                candidate.getConfiguration().equals(battlefield.getMessageConfiguration())){
                    synchronized (battlefield){
                        battlefield.setGameStatus(GameStatus.ENDING);
                        if(battlefield.getWinners().equals(""))
                            battlefield.setWinners(candidate.getTeamName());
                    }
                }
            });
            resp.getWriter().println(GSON_SERVICE.toJson(candidateList));
        }catch (NullPointerException e){
            //todo -redirect to login
        }
    }
}
