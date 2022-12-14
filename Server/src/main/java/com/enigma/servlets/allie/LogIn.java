package com.enigma.servlets.allie;

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

@WebServlet("/allie/login")
public class LogIn extends HttpServlet {
    private final Gson GSON_SERVICE = new Gson();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String clientName = req.getParameter("name");
        UserManager userManager = ServletsUtils.getUserManager(getServletContext());
        synchronized (this){
            LogInAnswer answer = new LogInAnswer();
            if(!userManager.isAllieExists(clientName)){
                answer.setId(userManager.addNewAllie(clientName).getId());
                answer.setMessage("New user created");
                answer.setSuccess(true);
            }else {
                answer.setSuccess(false);
                answer.setMessage("Username is already taken");
            }
            resp.getWriter().println(GSON_SERVICE.toJson(answer));
        }
    }
}
