package com.enigma.servlets.agent;

import com.engine.users.Agent;
import com.engine.users.Allie;
import com.engine.users.UserManager;
import com.enigma.dtos.dataObjects.Candidate;
import com.enigma.servlets.ServletsUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

@WebServlet("/agent/report_candidates")
public class ReportCandidates extends HttpServlet {
    private final Gson GSON_SERVICE = new Gson();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            UUID agentId = UUID.fromString(req.getParameter("id"));
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            Agent agent = userManager.getAgentById(agentId);
            List<Candidate> candidateList = GSON_SERVICE.fromJson(req.getReader(), TypeToken.getParameterized(List.class, Candidate.class).getType());
            agent.setNumOfCandidatesProduced(agent.getNumOfCandidatesProduced() + candidateList.size());
            addAgentNameToCandidates(candidateList, agent.getName());
            Allie allie = userManager.getAllieById(agent.getAllieId());
            BlockingQueue<Candidate> alliCandidates = allie.getCandidates();
            for (Candidate candidate : candidateList) {
                alliCandidates.put(candidate);
            }
            List<Candidate> copyOfCandidatesList = copyCandidatesList(candidateList);
            BlockingQueue<Candidate> allGameCandidates = userManager.getBattlefieldById(allie.getBattlefieldId()).getCandidates();
            for(Candidate candidate : copyOfCandidatesList){
                allGameCandidates.put(candidate);
            }
        }catch (NullPointerException e){
            //todo - redirect
        } catch (InterruptedException e) {
            System.out.println("Report candidates servlet couldn't report of all candidates");
        }
    }

    private void addAgentNameToCandidates(List<Candidate> candidateList, String agentName){
        candidateList.forEach(candidate -> candidate.setAgentName(agentName));
    }

    private List<Candidate> copyCandidatesList(List<Candidate> candidateList){
        return candidateList.stream().map(Candidate::new).collect(Collectors.toList());
    }
}
