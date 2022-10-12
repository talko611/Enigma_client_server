package com.enigma.servlets.agent;

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

@WebServlet("/Agent/LogIn")
public class LogIn extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = req.getParameter("name");
        UserManager userManager = ServletsUtils.getUserManager(getServletContext());
        synchronized (this){
            LogInAnswer answer = new LogInAnswer();
            if(!userManager.isAgentExists(userName)){
                answer.setId(userManager.addNewAgent(userName));
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
