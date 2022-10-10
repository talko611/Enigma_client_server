package com.enigma.servlets.allie;

import com.engine.battlefield.Battlefield;
import com.engine.users.UserManager;
import com.enigma.dtos.ServletAnswers.GetBattlefieldsAnswer;
import com.enigma.servlets.ServletsUtils;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javafx.util.Pair;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@WebServlet("/Allie/Get_battlefields")
public class GetBattlefields extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            UUID clientId = UUID.fromString(req.getParameter("id"));
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            List<Battlefield> availableBattlefields = userManager.getAllAvailableBattlefields();
            List<Pair<UUID, String>> answerData = availableBattlefields
                    .stream()
                    .map(battlefield -> new Pair<UUID, String>(battlefield.getId(), battlefield.getName()))
                    .collect(Collectors.toList());

            GetBattlefieldsAnswer answer = new GetBattlefieldsAnswer();
            answer.setBattlefieldList(answerData);
            Gson gson = new Gson();
            resp.getWriter().println(gson.toJson(answer));
        }catch (NullPointerException e){
            //Todo - redirect to login page
        }
    }
}
