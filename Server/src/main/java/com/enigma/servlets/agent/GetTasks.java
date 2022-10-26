package com.enigma.servlets.agent;

import com.engine.users.Agent;
import com.engine.users.Allie;
import com.engine.users.UserManager;
import com.enigma.dtos.dataObjects.DecryptionTaskData;
import com.enigma.servlets.ServletsUtils;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

@WebServlet("/agent/get_tasks")
public class GetTasks extends HttpServlet {
    private final Gson GSON_SERVICE = new Gson();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<DecryptionTaskData> tasksTaken = new ArrayList<>();
        int numberOfTasksTaken;
        UUID agentId;
        Agent agent;
        try{
            agentId = UUID.fromString(req.getParameter("id"));
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            agent = userManager.getAgentById(agentId);
            Allie allie = userManager.getAllieById(agent.getAllieId());
            BlockingQueue<DecryptionTaskData> assignTasks = agent.getTasksToPreform();
            if(allie.isProducerStillRunning() || !assignTasks.isEmpty()){
                numberOfTasksTaken = assignTasks.drainTo(tasksTaken, agent.getNumOfTaskCanAccept());
                agent.addToTasksAccepted(numberOfTasksTaken);
                resp.getWriter().println(GSON_SERVICE.toJson(tasksTaken));
            }else{
                resp.setStatus(206);
            }
        }catch (NullPointerException e){
            resp.setStatus(404);
        } catch (IllegalArgumentException e) {
            throw new IOException("failed to take tasks");
        }
    }
}
