package com.enigma.servlets.agent;

import com.engine.users.Agent;
import com.engine.users.UserManager;
import com.enigma.dtos.ServletAnswers.RequestServerAnswer;
import com.enigma.servlets.ServletsUtils;
import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;

@WebServlet("/agent/get_game_status")
public class GetGameStatus extends HttpServlet {
    private final Gson GSON_SERVICE = new Gson();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            UUID userId = UUID.fromString(req.getParameter("id"));
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            Agent user = userManager.getAgentById(userId);
            req.setAttribute("id", user.getAllieId().toString());
            getServletContext().getRequestDispatcher("/allie/get_game_status").forward(req, resp);
        }catch (NullPointerException e){
            //Todo - redirect to login
        }
    }
}
