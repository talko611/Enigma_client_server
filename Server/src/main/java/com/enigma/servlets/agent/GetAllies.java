package com.enigma.servlets.agent;

import com.engine.users.Allie;
import com.engine.users.UserManager;
import com.enigma.dtos.ServletAnswers.GetMapOfData;
import com.enigma.servlets.ServletsUtils;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@WebServlet("/agent/get_allies")
public class GetAllies extends HttpServlet {
    private final Gson gson = new Gson();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserManager userManager = ServletsUtils.getUserManager(getServletContext());
        Map<UUID, Allie> allieMap = userManager.getAllies();
        GetMapOfData<String> answer = new GetMapOfData<>();
        allieMap.forEach((id, allie)-> answer.addUser(id, allie.getName()));
        answer.setMessage("Success");
        resp.getWriter().println(gson.toJson(answer));
    }
}
