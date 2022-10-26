package com.enigma.servlets.uBoat;

import com.engine.Engine;
import com.engine.users.battlefield.Battlefield;
import com.engine.users.UserManager;
import com.enigma.dtos.ServletAnswers.MachineDetailsAnswer;
import com.enigma.servlets.ServletsUtils;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;


@WebServlet("/uBoat/get_details")
public class MachineDetailsServlet extends HttpServlet {
    private final Gson GSON_SERVICE = new Gson();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            UUID clientId = UUID.fromString(req.getParameter("id"));
            Engine engine = ServletsUtils.getEngine(getServletContext());
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            Battlefield battlefield = userManager.getBattlefieldById(userManager.getUBoatById(clientId).getBattlefieldId());
            MachineDetailsAnswer answer;
            synchronized (battlefield){
                answer = engine.getDetails(battlefield.getMachine(), battlefield.getEnigmaParts().getMachineParts());
            }
            resp.getWriter().println(GSON_SERVICE.toJson(answer));
        }catch (NullPointerException e){
            resp.setStatus(404);
        }
    }
}
