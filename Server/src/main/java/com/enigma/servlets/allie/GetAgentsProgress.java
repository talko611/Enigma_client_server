package com.enigma.servlets.allie;

import com.engine.users.Agent;
import com.engine.users.Allie;
import com.engine.users.UserManager;
import com.enigma.dtos.dataObjects.AgentProgressObject;
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

@WebServlet("/allie/get_agents_progress")
public class GetAgentsProgress extends HttpServlet {
    private static final Gson GSON_SERVICE = new Gson();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            UUID allieId = UUID.fromString(req.getParameter("id"));
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            Allie allie = userManager.getAllieById(allieId);
            List<AgentProgressObject> agentProgressList = new ArrayList<>();
            allie.getActiveAgents().forEach(agent -> agentProgressList.add(new AgentProgressObject(agent.getNumOfTaskAssigned(),
                    agent.getNumOfTaskAccepted(),
                    agent.getNumOfCandidatesProduced(),
                    agent.getName())));
            resp.getWriter().println(GSON_SERVICE.toJson(agentProgressList));
        }catch (NullPointerException e){
            resp.setStatus(404);
        }
    }
}
