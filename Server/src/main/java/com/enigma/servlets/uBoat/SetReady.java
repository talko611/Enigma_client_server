package com.enigma.servlets.uBoat;

import com.engine.users.Uboat;
import com.engine.users.UserManager;
import com.engine.users.battlefield.Battlefield;
import com.enigma.dtos.ServletAnswers.RequestServerAnswer;
import com.enigma.servlets.ServletsUtils;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/uBoat/set_ready")
public class SetReady extends HttpServlet {
    private final Gson GSON_SERVICE = new Gson();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            UUID userId = UUID.fromString(req.getParameter("id"));
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            Uboat user = userManager.getUBoatById(userId);
            if(user == null){
                throw new NullPointerException("User is not found");
            }
            Battlefield battlefield = userManager.getBattlefieldById(user.getBattlefieldId());
            RequestServerAnswer answer = new RequestServerAnswer();
            if(battlefield == null){
                throw new NullPointerException("Something went wrong please login again");
            }
            synchronized (battlefield){
                if(battlefield.getEncryptedMessage() != null){
                    user.setReadyToPlay(true);
                    battlefield.updateActivateGame(userManager);
                    answer.setSuccess(true);
                    answer.setMessage("Ready to play");
                }else {
                    answer.setSuccess(false);
                    answer.setMessage("Please decrypt a message first");
                }
            }
            resp.getWriter().println(GSON_SERVICE.toJson(answer));
        }catch (NullPointerException e){
            resp.setStatus(301);
            //TODO - redirect to login page
        }
    }

}
