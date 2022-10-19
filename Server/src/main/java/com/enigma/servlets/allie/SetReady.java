package com.enigma.servlets.allie;

import com.engine.users.Agent;
import com.engine.users.Allie;
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

@WebServlet("/allie/set_ready")
public class SetReady extends HttpServlet {
    private final Gson GSON_SERVICE = new Gson();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            UUID userId = UUID.fromString(req.getParameter("id"));
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            Allie allie = userManager.getAllieById(userId);

            if(allie == null){
                throw new NullPointerException("something went wrong please login again");
            }
            Battlefield battlefield = userManager.getBattlefieldById(allie.getBattlefieldId());
            RequestServerAnswer answer = new RequestServerAnswer();
            if(battlefield == null){
                answer.setSuccess(false);
                answer.setMessage("Please sign in to battlefield first");
                resp.setStatus(401);
            } else if( allie.getTaskSize() > 0){
                if(isAgentsReady(allie)){
                    synchronized (battlefield){
                        allie.setReadyToPlay(true);
                        battlefield.updateActivateGame(userManager);
                    }
                    answer.setSuccess(true);
                    answer.setMessage("Ready to play");
                }else{
                    resp.setStatus(401);
                    answer.setMessage("One of the agent is not acquired the machine/n please try again in a few seconds");
                    answer.setSuccess(false);
                }
            }else {
                answer.setMessage("task Size is not configure");
                answer.setSuccess(false);
                resp.setStatus(401);
            }
            resp.getWriter().println(GSON_SERVICE.toJson(answer));
        }catch (NullPointerException e){
            //Todo - redirect to login page
        }
    }

    private boolean isAgentsReady(Allie allie){
        boolean answer = true;
        synchronized (allie){
            for(Agent agent : allie.getActiveAgents()){
                if(!agent.isReadyToPlay()){
                    answer = false;
                    break;
                }
            }
        }
        return answer;
    }
}
