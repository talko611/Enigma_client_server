package com.enigma.uBoat;

import com.engine.Engine;
import com.enigma.dtos.LoadFileAnswer;
import com.enigma.servlets.ServletsUtils;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

@WebServlet("/UBoat/load-file")
@MultipartConfig
public class LoadBattlefield extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Collection<Part> parts = req.getParts();
        UUID clientId = UUID.fromString( req.getParameter("id"));
        Engine engine = ServletsUtils.getEngine(getServletContext());
        LoadFileAnswer answer = new LoadFileAnswer();
        if(parts.size() == 1){
            for(Part part : parts){
                System.out.println(part);
                answer = engine.loadGame(clientId, part.getInputStream());
            }
            resp.setStatus(200);
        }else{
            answer.setSuccess(false);
            answer.setMessage("Send more the one file please correct");
            resp.setStatus(400);
        }
        Gson gson = new Gson();
        resp.getWriter().println(gson.toJson(answer));
    }
}
