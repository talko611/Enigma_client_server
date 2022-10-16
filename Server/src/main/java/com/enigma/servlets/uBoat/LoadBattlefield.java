package com.enigma.servlets.uBoat;

import com.engine.Engine;
import com.engine.users.battlefield.Battlefield;
import com.engine.enigmaParts.EnigmaParts;
import com.engine.users.Uboat;
import com.engine.users.UserManager;
import com.enigma.dtos.ServletAnswers.LoadFileAnswer;
import com.enigma.servlets.ServletsUtils;
import com.google.gson.Gson;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.Collection;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.UUID;

@WebServlet("/uBoat/load_file")
@MultipartConfig
public class LoadBattlefield extends HttpServlet {
    private final Gson GSON_SERVICE = new Gson();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Collection<Part> parts = req.getParts();
        LoadFileAnswer answer = new LoadFileAnswer();
        try{
            UUID clientId = UUID.fromString( req.getParameter("id"));
            Engine engine = ServletsUtils.getEngine(getServletContext());
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            //Check if got more than 1 file
            if(parts.size() != 1){
                answer.setMessage("Cannot handle request more then one file uploaded");
                answer.setSuccess(false);
                resp.getWriter().println(GSON_SERVICE.toJson(answer));
            }else{
                EnigmaParts enigmaParts = null;
                String fileContent = null;
                for(Part part: parts){
                    fileContent = convertInputStreamToStr(part.getInputStream());
                    enigmaParts = engine.loadGame(new ByteArrayInputStream(fileContent.getBytes()));
                }
                Uboat uboat = userManager.getUBoatById(clientId);
                String battlefieldName = enigmaParts.getBattlefieldParts().getName();

                //Case this battlefield is not exists
                synchronized (this){
                    if(!userManager.isBattlefieldExists(battlefieldName)){
                        setNewBattlefield(enigmaParts, userManager, clientId, fileContent);
                        answer.setSuccess(true);
                        answer.setMessage(battlefieldName);
                        resp.setStatus(200);
                    }
                    //Case client already uploaded this battlefield
                    //Todo - consider remove cause this case cannot happen
                    else if(uboat.getBattlefieldId()!= null &&
                            userManager.getBattlefieldById(uboat.getBattlefieldId()).getName().equals(battlefieldName)){
                        answer.setSuccess(true);
                        answer.setMessage("Already inside battlefield");
                    }
                    //Case this battlefield is already uploaded by different uBoat
                    else{
                        answer.setSuccess(false);
                        answer.setMessage("Battlefield is already exists");
                        resp.setStatus(400);
                    }
                }
            }
        }catch (NullPointerException e){
         //Todo - redirect to login page
        }catch (JAXBException | InputMismatchException e){
            resp.setStatus(400);
            answer.setSuccess(false);
            answer.setMessage(e.getMessage());
        }catch (IOException e){
            answer.setSuccess(false);
            answer.setMessage("Could not close file");
        }
        resp.getWriter().println(GSON_SERVICE.toJson(answer));
    }

    private void setNewBattlefield(EnigmaParts enigmaParts, UserManager userManager, UUID clientId, String fileContent){
        UUID battlefieldId = userManager.addNewBattlefield(enigmaParts.getBattlefieldParts().getName());
        Battlefield battlefield = userManager.getBattlefieldById(battlefieldId);
        synchronized (battlefield){
            battlefield.setEnigmaParts(enigmaParts);
            battlefield.setUBoatId(clientId);
            battlefield.getMachine().setKeyboard(battlefield.getEnigmaParts().getMachineParts().getKeyboard());
            battlefield.setFileContent(fileContent);
            Uboat uboat = userManager.getUBoatById(clientId);
            uboat.setBattlefieldId(battlefieldId);
        }
    }

    private String convertInputStreamToStr(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }

}
