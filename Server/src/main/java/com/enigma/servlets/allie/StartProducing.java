package com.enigma.servlets.allie;

import com.engine.Engine;
import com.engine.users.Allie;
import com.engine.users.UserManager;
import com.engine.users.battlefield.Battlefield;
import com.enigma.dtos.Enums.GameStatus;
import com.enigma.machine.parts.rotor.Rotor;
import com.enigma.servlets.ServletsUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;
import java.util.stream.Collectors;

@WebServlet("/allie/start_producing")
public class StartProducing extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            UUID userId = UUID.fromString(req.getParameter("id"));
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            Allie user = userManager.getAllieById(userId);
            Engine engine = ServletsUtils.getEngine(getServletContext());
            Battlefield battlefield = userManager.getBattlefieldById(user.getBattlefieldId());
            if(battlefield.getGameStatus() == GameStatus.RUNNING){
                user.setProducer(engine.launchTaskProducer(user.getName(),
                        battlefield.getEnigmaParts().getBattlefieldParts().getDifficulty(),
                        user.getActiveAgents(),
                        (int) user.getTaskSize(),
                        battlefield.getEnigmaParts().getMachineParts(),
                        battlefield.getMachine().getRotors().stream().map(Rotor::getId).collect(Collectors.toList()),
                        battlefield.getMachine().getReflector().getId(),
                        battlefield.getEncryptedMessage()));
            }else{
                resp.setStatus(401);
            }
        }catch (NullPointerException e){
            resp.setStatus(404);
        }
    }
}
