package com.enigma.servlets.allie;

import com.engine.users.Allie;
import com.engine.users.UserManager;
import com.enigma.dtos.dataObjects.Candidate;
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

@WebServlet("/allie/get_candidates")
public class GetCandidates extends HttpServlet {
    private static final Gson GSON_SERVICE = new Gson();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            UUID allieId = UUID.fromString(req.getParameter("id"));
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            Allie allie = userManager.getAllieById(allieId);
            List<Candidate> candidates = new ArrayList<>();
            allie.getCandidates().drainTo(candidates);
            resp.getWriter().println(GSON_SERVICE.toJson(candidates));
        }catch (NullPointerException e){
            //todo - redirect to login page
        }
    }
}
