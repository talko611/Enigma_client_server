package com.enigma.servlets.agent;

import com.engine.users.Agent;
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
import java.util.UUID;

@WebServlet("/agent/is_stated")
public class isGameStarted extends HttpServlet {
    private final Gson GSON_SERVICE = new Gson();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            UUID userId = UUID.fromString(req.getParameter("id"));
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            Agent user = userManager.getAgentById(userId);
            RequestServerAnswer answer = new RequestServerAnswer();
            if(user.isInActiveGame()){
                answer.setSuccess(true);
                answer.setMessage("Active");
            }else {
                answer.setMessage("Awaiting");
                answer.setSuccess(false);
            }
            resp.getWriter().println(GSON_SERVICE.toJson(answer));
        }catch (NullPointerException e){
            //Todo - redirect to login
        }
    }
}
