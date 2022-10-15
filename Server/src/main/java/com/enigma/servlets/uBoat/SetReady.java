package com.enigma.servlets.uBoat;

import com.engine.users.Uboat;
import com.engine.users.UserManager;
import com.enigma.servlets.ServletsUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/uBoat/set_ready")
public class SetReady extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            UUID userId = UUID.fromString(req.getParameter("id"));
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            Uboat user = userManager.getUBoatById(userId);
            //TODO - redirect to login page is user is not found
            user.setReadyToPlay(true);
            resp.setStatus(200);
        }catch (NullPointerException e){
            //TODO - redirect to login page
        }
    }

}
