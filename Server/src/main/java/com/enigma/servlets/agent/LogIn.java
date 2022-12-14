package com.enigma.servlets.agent;

import com.engine.enums.AgentStatus;
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

@WebServlet("/agent/login")
public class LogIn extends HttpServlet {
    private final Gson GSON_SERVICE = new Gson();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = req.getParameter("name");
        UUID allieId = UUID.fromString(req.getParameter("allie"));
        int numOfTask = Integer.parseInt(req.getParameter("numOfTasks"));
        UserManager userManager = ServletsUtils.getUserManager(getServletContext());
        synchronized (this){
            LogInAnswer answer = new LogInAnswer();
            if(!userManager.isAgentExists(userName)){
                Agent newAgent = userManager.addNewAgent(userName, numOfTask,allieId);
                boolean flag = userManager.getAllieById(allieId).addAgent(newAgent);
                if(flag)
                    resp.setStatus(200);
                else
                    resp.setStatus(206);
                setAgentStatus(flag, newAgent);
                answer.setId(newAgent.getId());
                answer.setMessage("New agent created");
                answer.setSuccess(true);
            }else {
                answer.setSuccess(false);
                answer.setMessage("User name is already taken");
            }

            resp.getWriter().println(GSON_SERVICE.toJson(answer));
        }
    }

    private void setAgentStatus(boolean mode, Agent newAgent){
        if (mode) {
            newAgent.setStatus(AgentStatus.ACTIVE);
        } else {
            newAgent.setStatus(AgentStatus.AWAITING);
        }
    }
}
