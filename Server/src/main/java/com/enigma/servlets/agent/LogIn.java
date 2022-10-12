package com.enigma.servlets.agent;

import com.engine.users.Agent;
import com.engine.users.UserManager;
import com.enigma.dtos.ServletAnswers.LogInAnswer;
import com.enigma.servlets.ServletsUtils;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/Agent/LogIn")
public class LogIn extends HttpServlet {
    private final Gson gsonService = new Gson();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = req.getParameter("name");
        UUID allieId = UUID.fromString(req.getParameter("allie"));
        int numOfWorkers = Integer.parseInt(req.getParameter("workers"));
        UserManager userManager = ServletsUtils.getUserManager(getServletContext());
        synchronized (this){
            LogInAnswer answer = new LogInAnswer();
            if(!userManager.isAgentExists(userName)){
                Agent newAgent = userManager.addNewAgent(userName, numOfWorkers);
                userManager.getAllieById(allieId).addAgent(newAgent);
                answer.setId(newAgent.getId());
                answer.setMessage("New agent created");
                answer.setSuccess(true);
            }else {
                answer.setSuccess(false);
                answer.setMessage("User name is already taken");
            }
            Gson gson = new Gson();
            resp.getWriter().println(gson.toJson(answer));
        }
    }
}
