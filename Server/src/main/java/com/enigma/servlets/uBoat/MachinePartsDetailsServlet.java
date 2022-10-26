package com.enigma.servlets.uBoat;

import com.engine.users.battlefield.Battlefield;
import com.engine.enigmaParts.machineParts.MachineParts;
import com.engine.users.UserManager;
import com.enigma.dtos.ServletAnswers.MachinePartsAnswer;
import com.enigma.machine.parts.reflector.Reflector;
import com.enigma.machine.parts.rotor.Rotor;
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
import java.util.Map;
import java.util.UUID;

@WebServlet("/uBoat/get_machine_parts")
public class MachinePartsDetailsServlet extends HttpServlet {
    private final Gson GSON_SERVICE = new Gson();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            UUID clientId = UUID.fromString(req.getParameter("id"));
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            Battlefield battlefield = userManager.getBattlefieldById(userManager.getUBoatById(clientId).getBattlefieldId());
            MachinePartsAnswer answer = new MachinePartsAnswer();
            synchronized (battlefield){
                MachineParts parts = battlefield.getEnigmaParts().getMachineParts();
                answer.setRotorIds(getRotorsIds(parts.getAllRotors()));
                answer.setReflectorIds(getReflectorsIds(parts.getAllReflectors()));
                answer.setKeyboardChars(parts.getKeyboard().getAllKeys());
                answer.setRotorsCount(parts.getRotorCount());
            }
            resp.getWriter().println(GSON_SERVICE.toJson(answer));
        }catch (NullPointerException e){
            resp.setStatus(404);
        }
    }

    private List<Integer> getRotorsIds(Map<Integer,Rotor> rotorMap){
        List<Integer> ids = new ArrayList<>();
        rotorMap.forEach((id, rotor)-> ids.add(id));
        return ids;
    }

    private List<Integer> getReflectorsIds(Map<Integer, Reflector> reflectorMap){
        List<Integer> ids = new ArrayList<>();
        reflectorMap.forEach((id, reflector) -> ids.add(id));
        return ids;
    }
}
