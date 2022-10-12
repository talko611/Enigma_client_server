package com.enigma.servlets.uBoat;

import com.engine.Engine;
import com.engine.battlefield.Battlefield;
import com.engine.enigmaParts.machineParts.MachineParts;
import com.engine.users.UserManager;
import com.enigma.dtos.dataObjects.ManualConfigStrings;
import com.enigma.dtos.EngineAnswers.InputOperationAnswer;
import com.enigma.dtos.ServletAnswers.RequestServerAnswer;
import com.enigma.machine.Machine;
import com.enigma.servlets.ServletsUtils;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/UBoat/Config")
public class ConfigMachine extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            UUID clientId = UUID.fromString(req.getParameter("id"));
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            Battlefield battlefield = userManager.getBattlefieldById(userManager.getUBoatById(clientId).getBattlefieldId());
            RequestServerAnswer answer = new RequestServerAnswer();
            if(battlefield == null){
                answer.setSuccess(false);
                answer.setMessage("Battlefield is not loaded");
                resp.setStatus(401);
            }else{
                Engine engine = ServletsUtils.getEngine(getServletContext());
                synchronized (battlefield.getMachine()){
                    InputOperationAnswer configurationAnswer = engine.autoConfig(battlefield.getMachine(), battlefield.getEnigmaParts().getMachineParts());
                    if(configurationAnswer.isSuccess()){
                        answer.setSuccess(true);
                        answer.setMessage("Machine is configure");
                        resp.setStatus(200);
                    }else {
                        answer.setSuccess(false);
                        answer.setMessage(configurationAnswer.getMessage());
                        resp.setStatus(401);
                    }
                }
            }
            Gson gson = new Gson();
            resp.getWriter().println(gson.toJson(answer));
        }catch (NullPointerException e){
            //Todo - redirect to login page
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            UUID clientId = UUID.fromString(req.getParameter("id"));
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            Battlefield battlefield = userManager.getBattlefieldById(userManager.getUBoatById(clientId).getBattlefieldId());
            RequestServerAnswer answer = new RequestServerAnswer();
            if(battlefield == null){
                answer.setSuccess(false);
                answer.setMessage("Battlefield is not loaded");
                resp.setStatus(400);
            }else{
                Engine engine = ServletsUtils.getEngine(getServletContext());
                Gson gson = new Gson();
                ManualConfigStrings configStrings = gson.fromJson(req.getReader(),ManualConfigStrings.class);
                InputOperationAnswer configurationAnswer = manualConfigMachine(configStrings,
                        battlefield.getMachine(),
                        battlefield.getEnigmaParts().getMachineParts());
                if(configurationAnswer.isSuccess()){
                    answer.setSuccess(true);
                    answer.setMessage("Machine is configure successfully");
                    resp.setStatus(200);
                }
                else{
                    answer.setSuccess(false);
                    answer.setMessage(configurationAnswer.getMessage());
                    resp.setStatus(400);
                }
                resp.getWriter().println(gson.toJson(answer));
            }
        }catch (NullPointerException e){
            //Todo - redirect to login page
        }
    }

    private InputOperationAnswer manualConfigMachine(ManualConfigStrings configStrings, Machine machine, MachineParts machineParts){
        Engine engine = ServletsUtils.getEngine(getServletContext());
        InputOperationAnswer rotorsConfigAnswer = engine.manualConfigRotors(machine, machineParts, configStrings.getRotorConfigLine());
        if(rotorsConfigAnswer.isSuccess()){
            InputOperationAnswer offsetConfigAnswer = engine.manualConfigOffsets(machine, configStrings.getOffsetConfigLine());
            if (offsetConfigAnswer.isSuccess()){
                InputOperationAnswer reflectorConfigAnswer = engine.manualConfigReflector(machine,machineParts, configStrings.getReflectorConfigLine());
                if(reflectorConfigAnswer.isSuccess()){
                    return engine.manualConfigPlugBoard(machine, configStrings.getPlugBoardConfigLine());
                }else {
                    return reflectorConfigAnswer;
                }
            }else {
                return offsetConfigAnswer;
            }
        }else {
            return rotorsConfigAnswer;
        }
    }

}
