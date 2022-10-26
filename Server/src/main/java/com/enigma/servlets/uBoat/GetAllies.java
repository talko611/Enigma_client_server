package com.enigma.servlets.uBoat;

import com.engine.users.Allie;
import com.engine.users.Uboat;
import com.engine.users.UserManager;
import com.engine.users.battlefield.Battlefield;
import com.enigma.dtos.dataObjects.AllieData;
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

@WebServlet("/uBoat/get_allies")
public class GetAllies extends HttpServlet {
    private final Gson GSON_SERVICE = new Gson();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            UUID userId = UUID.fromString(req.getParameter("id"));
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            Uboat user = userManager.getUBoatById(userId);
            if(user == null){
                userId = (UUID) req.getAttribute("id");
                user = userManager.getUBoatById(userId);
                if(user == null){
                    throw new NullPointerException("User is not exists");
                }
            }
            Battlefield battlefield = userManager.getBattlefieldById(user.getBattlefieldId());
            List<AllieData> allieDataList;
            synchronized (battlefield){
                List<Allie> allies = battlefield.getTeams();
                allieDataList = buildResponseBody(allies);
            }
            resp.getWriter().println(GSON_SERVICE.toJson(allieDataList));
        }catch (NullPointerException e){
            resp.setStatus(404);
        }
    }

    private List<AllieData> buildResponseBody(List<Allie> allies){
        List<AllieData> allieDataList = new ArrayList<>(allies.size());
        allies.forEach(allie -> {
            AllieData newAllie = new AllieData();
            newAllie.setAlliName(allie.getName());
            newAllie.setNumOfAgents(allie.getActiveAgents().size());
            newAllie.setTaskSize(allie.getTaskSize());
            allieDataList.add(newAllie);
        });
        return allieDataList;
    }
}
