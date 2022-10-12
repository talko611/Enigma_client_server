package com.enigma.servlets.agent;

import com.engine.users.Agent;
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
import java.util.UUID;

@WebServlet("/Agent/join_team")
public class JoinAllieTeam extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            UUID userId = UUID.fromString(req.getParameter("id"));
            UUID allieId = UUID.fromString(req.getParameter("allie"));
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            Agent user = userManager.getAgentById(userId);
            Allie allie = userManager.getAllieById(allieId);
            //TODO - handle cases
            allie.addAgent(user);
            user.setAllieId(allieId);
            RequestServerAnswer answer = new RequestServerAnswer();
            answer.setSuccess(true);
            answer.setMessage("Join team");
            Gson gson = new Gson();
            resp.getWriter().println(gson.toJson(answer));
        }catch (NullPointerException e){
            //TODO - redirect to login page
            resp.setStatus(401);
        }


    }
}
