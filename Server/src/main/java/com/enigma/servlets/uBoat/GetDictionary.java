package com.enigma.servlets.uBoat;

import com.engine.battlefield.Battlefield;
import com.engine.users.Uboat;
import com.engine.users.UserManager;
import com.enigma.servlets.ServletsUtils;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/UBoat/getDictionary")
public class GetDictionary extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            UUID clientId = UUID.fromString(req.getParameter("id"));
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            Uboat client = userManager.getUBoatById(clientId);
            Battlefield battlefield = userManager.getBattlefieldById(client.getBattlefieldId());
            //Todo - handle cases when uboat is not found and battlefield is not found
            Gson gson = new Gson();
            resp.getWriter().println(battlefield.getEnigmaParts().getDmParts().getDictionary());
        }catch (NullPointerException e){
            //Todo- redirect to login page
        }
    }
}
