package com.enigma.uBoat;

import com.engine.Engine;
import com.engine.EngineImp;
import com.enigma.dtos.LogInAnswer;
import com.enigma.servlets.ServletsUtils;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "logInUBoat", urlPatterns = "/UBoat/LogIn")
public class LogInServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        Engine engine = ServletsUtils.getEngine(getServletContext());
        Gson gson = new Gson();
        synchronized (this){
            LogInAnswer answer = engine.uBoatLogIn(name);
            String jasonResp = gson.toJson(answer);
            if(answer.isSuccess()){
                resp.getWriter().println(jasonResp);
            }else{
                resp.getWriter().println(jasonResp);
            }
        }
    }
}
