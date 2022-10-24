package com.enigma.servlets.uBoat;

import com.engine.users.Uboat;
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

@WebServlet("/uBoat/reset_game")
public class ResetGame extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            UUID uBoatId = UUID.fromString(req.getParameter("id"));
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            Uboat uboat = userManager.getUBoatById(uBoatId);
            Battlefield battlefield = userManager.getBattlefieldById(uboat.getBattlefieldId());
            battlefield.reset();
            uboat.setReadyToPlay(false);
            uboat.setInActiveGame(false);
        }catch (NullPointerException e){
            resp.setStatus(401);
            //Todo - redirect to login page
        }
    }
}
