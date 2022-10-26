package com.enigma.servlets.allie;

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

@WebServlet("/allie/get_teams")
public class GetGameParticipants extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            UUID userId = UUID.fromString(req.getParameter("id"));
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            Allie user = userManager.getAllieById(userId);
            UUID uBoatId = userManager.getBattlefieldById(user.getBattlefieldId()).getUBoatId();
            req.setAttribute("id", uBoatId);
            getServletContext().getRequestDispatcher("/uBoat/get_allies").forward(req, resp);
        }catch (NullPointerException e){
            resp.setStatus(404);
        }
    }
}
