package com.enigma.servlets.uBoat;

import com.engine.Engine;
import com.engine.battlefield.Battlefield;
import com.engine.enigmaParts.EnigmaParts;
import com.engine.users.Uboat;
import com.engine.users.UserManager;
import com.enigma.dtos.ServletAnswers.LoadFileAnswer;
import com.enigma.servlets.ServletsUtils;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Collection;
import java.util.InputMismatchException;
import java.util.UUID;

@WebServlet("/UBoat/load-file")
@MultipartConfig
public class LoadBattlefield extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Collection<Part> parts = req.getParts();
        LoadFileAnswer answer = new LoadFileAnswer();
        Gson gson = new Gson();
        try{
            UUID clientId = UUID.fromString( req.getParameter("id"));
            Engine engine = ServletsUtils.getEngine(getServletContext());
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            if(parts.size() != 1){
                answer.setMessage("Cannot handle request more then one file uploaded");
                answer.setSuccess(false);
                resp.getWriter().println(gson.toJson(answer));
            }else{
                EnigmaParts enigmaParts = null;
                for(Part part: parts){
                    enigmaParts = engine.loadGame(part.getInputStream());
                }
                if(!userManager.isBattlefieldExists(enigmaParts.getBattlefieldParts().getName())){
                    setNewBattlefield(enigmaParts, userManager, clientId);
                    answer.setSuccess(true);
                    answer.setMessage("Battlefield is loaded");
                    resp.setStatus(200);
                }
                else{
                    answer.setSuccess(false);
                    answer.setMessage("Battlefield is already exists");
                    resp.setStatus(400);
                }

            }
        }catch (NullPointerException e){
         //redirect to log in page
            answer.setMessage("need to redirect to login page");
            answer.setSuccess(false);
        }catch (JAXBException | InputMismatchException e){
            resp.setStatus(400);
            answer.setSuccess(false);
            answer.setMessage(e.getMessage());
        }
        resp.getWriter().println(gson.toJson(answer));
    }

    private synchronized void setNewBattlefield(EnigmaParts enigmaParts, UserManager userManager, UUID clientId){
        UUID battlefieldId = userManager.addNewBattlefield(enigmaParts.getBattlefieldParts().getName());
        Battlefield battlefield = userManager.getBattlefieldById(battlefieldId);
        battlefield.setEnigmaParts(enigmaParts);
        battlefield.getMachine().setKeyboard(battlefield.getEnigmaParts().getMachineParts().getKeyboard());
        Uboat uboat = userManager.getUBoatById(clientId);
        uboat.setBelongToBattlefield(true);
        uboat.setBattlefieldId(battlefieldId);
    }
}
