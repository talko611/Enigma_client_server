package com.enigma.servlets.agent;

import com.engine.users.Agent;
import com.engine.users.UserManager;
import com.engine.users.battlefield.Battlefield;
import com.enigma.servlets.ServletsUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/agent/get_enigma_parts")
public class GetEnigmaParts extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            UUID agentId = UUID.fromString(req.getParameter("id"));
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            Agent agent = userManager.getAgentById(agentId);
            Battlefield battlefield = userManager.getBattlefieldById(userManager.getAllieById(agent.getAllieId()).getBattlefieldId());
            if(battlefield == null){
                resp.setStatus(401);
            }else {
                synchronized (battlefield){
                    if(battlefield.getEnigmaParts() == null){
                        resp.setStatus(204);
                    }else{
                        resp.setStatus(200);
                        resp.getWriter().println(battlefield.getFileContent());
                    }
                }
            }
        }catch (NullPointerException e){
            resp.setStatus(404);
        }

    }
}
