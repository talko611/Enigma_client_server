package com.enigma.servlets.agent;

import com.engine.users.Agent;
import com.engine.users.Allie;
import com.engine.users.UserManager;
import com.enigma.servlets.ServletsUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/agent/set_ready")
public class SetReady extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            UUID agentId = UUID.fromString(req.getParameter("id"));
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            Agent agent = userManager.getAgentById(agentId);
            Allie allie = userManager.getAllieById(agent.getAllieId());
            if(agent == null){
                resp.setStatus(401);
            }
            else{
                agent.setReadyToPlay(true);
                resp.setStatus(200);
            }
        }catch (NullPointerException e){
            //Todo - redirect to
        }
    }
}
