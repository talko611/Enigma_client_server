package com.enigma.servlets.allie;

import com.engine.battlefield.Battlefield;
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

@WebServlet("/Allie/Join")
public class JoinToBattlefield extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            UUID clientId  = UUID.fromString(req.getParameter("id"));
            UUID battlefieldId = UUID.fromString(req.getParameter("battlefield"));
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            Allie client = userManager.getAllieById(clientId);
            //Todo - handle cases client is not found
            Battlefield battlefield = userManager.getBattlefieldById(battlefieldId);
            //Todo - handle cases battlefield is not found

            //Case Game is already started
            if(!battlefield.isGameStarted()){
                battlefield.addNewAllie(client);
                client.setBattlefieldId(battlefieldId);
                resp.setStatus(200);
            }else {
                resp.setStatus(401);
            }
        }catch (NullPointerException e){
            //Todo - redirect to log in page
        }
    }
}
