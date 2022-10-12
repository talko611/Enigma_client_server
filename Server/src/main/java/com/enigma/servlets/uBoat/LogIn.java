package com.enigma.servlets.uBoat;

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

@WebServlet(name = "logInUBoat", urlPatterns = "/UBoat/LogIn")
public class LogIn extends HttpServlet {
    private final Gson gson = new Gson();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        UserManager userManager = ServletsUtils.getUserManager(getServletContext());
        synchronized (this){
            LogInAnswer answer = new LogInAnswer();
            if(!userManager.isUBoatExists(name)){
                answer.setId(userManager.addNewUBoat(name).getId());
                answer.setMessage("New user created");
                answer.setSuccess(true);
            }
            else{
                answer.setSuccess(false);
                answer.setMessage("Username is already taken");
            }
            resp.getWriter().println(gson.toJson(answer));
        }
    }
}
