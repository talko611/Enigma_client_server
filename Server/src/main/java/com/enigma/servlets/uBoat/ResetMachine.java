package com.enigma.servlets.uBoat;

import com.engine.users.Uboat;
import com.engine.users.UserManager;
import com.engine.users.battlefield.Battlefield;
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

@WebServlet("/uBoat/reset_machine")
public class ResetMachine extends HttpServlet {
    private static final Gson GSON_SERVICE = new Gson();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            UUID userId = UUID.fromString(req.getParameter("id"));
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            Uboat user = userManager.getUBoatById(userId);
            Battlefield battlefield = userManager.getBattlefieldById(user.getBattlefieldId());
            battlefield.getMachine().reset();
            RequestServerAnswer answer = new RequestServerAnswer();
            answer.setMessage(battlefield.getMachine().getCurrentConfiguration());
            resp.getWriter().println(GSON_SERVICE.toJson(answer));
        }catch (NullPointerException e){
            resp.setStatus(404);
        }
    }
}
