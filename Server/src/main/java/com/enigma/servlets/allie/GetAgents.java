package com.enigma.servlets.allie;

import com.engine.users.Allie;
import com.engine.users.UserManager;
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

@WebServlet("/allie/get_agents")
public class GetAgents extends HttpServlet {
    private final Gson GSON_SERVICE = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            UUID userId = UUID.fromString(req.getParameter("id"));
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            Allie user = userManager.getAllieById(userId);
            if(user == null){
                throw new NullPointerException();
            }
            synchronized (user){
                List<String> agentNames = new ArrayList<>();
                user.getActiveAgents().forEach((agent)-> agentNames.add(agent.getName()));
                resp.getWriter().println(GSON_SERVICE.toJson(agentNames));
            }
        }catch (NullPointerException e){
            resp.setStatus(404);
        }

    }
}
