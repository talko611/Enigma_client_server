package com.enigma.servlets.allie;

import com.engine.Engine;
import com.engine.battlefield.Battlefield;
import com.engine.users.Allie;
import com.engine.users.UserManager;
import com.enigma.dtos.EngineAnswers.CalculationOperationAnswer;
import com.enigma.dtos.ServletAnswers.RequestServerAnswer;
import com.enigma.servlets.ServletsUtils;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.checkerframework.checker.units.qual.A;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

@WebServlet("/Allie/Join")
public class JoinToBattlefield extends HttpServlet {
    private static final Gson GSON_SERVICE = new Gson();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            UUID clientId  = UUID.fromString(req.getParameter("id"));
            UUID battlefieldId = UUID.fromString(req.getParameter("battlefield"));
            long taskSize = Long.parseLong(req.getParameter("taskSize"));
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            Engine engine = ServletsUtils.getEngine(getServletContext());
            Allie client = userManager.getAllieById(clientId);
            //Todo - handle cases client is not found
            Battlefield battlefield = userManager.getBattlefieldById(battlefieldId);
            //Todo - handle cases battlefield is not found
            RequestServerAnswer answer = validateRequest(client, battlefield,taskSize);
            if(answer.isSuccess()){
               CalculationOperationAnswer calculationOperationAnswer =  engine.calculateNumberOfTasks(battlefield.getEnigmaParts().getBattlefieldParts().getDifficulty(),
                       taskSize,battlefield.getEnigmaParts().getMachineParts(), battlefield.getMachine().getRotors().size());
               if(calculationOperationAnswer.isSuccess()){
                   removeFromPreviousBattlefield(userManager, client);
                   battlefield.addNewAllie(client);
                   client.setBelongToBattlefield(true);
                   client.setBattlefieldId(battlefieldId);
                   client.setTaskSize((Long) calculationOperationAnswer.getData());
                   answer.setSuccess(calculationOperationAnswer.isSuccess());
                   answer.setMessage("Join to battlefield");
               }else {
                   answer.setSuccess(false);
                   answer.setMessage(calculationOperationAnswer.getMessage());
               }
           }
            resp.getWriter().println(GSON_SERVICE.toJson(answer));
        }catch (NullPointerException e){
            //Todo - redirect to log in page
        }catch (NumberFormatException e){
            RequestServerAnswer answer = new RequestServerAnswer();
            answer.setMessage("Number is too big");
            answer.setSuccess(false);
            resp.getWriter().println(GSON_SERVICE.toJson(answer));
        }catch (Exception e){
            System.out.println(e.getCause());
            Arrays.stream(e.getStackTrace()).collect(Collectors.toList()).forEach(System.out::println);
        }
    }

    private void removeFromPreviousBattlefield(UserManager userManager, Allie allie){
        Battlefield battlefield = userManager.getBattlefieldById(allie.getBattlefieldId());
        if(battlefield != null)
            battlefield.removeAllie(allie);
    }

    private RequestServerAnswer validateRequest(Allie user, Battlefield battlefield, long taskSize){
        RequestServerAnswer answer = new RequestServerAnswer();
        answer.setSuccess(true);
        if(battlefield.isGameStarted()){
            answer.setSuccess(false);
            answer.setMessage("Game already started");
        } else if(user.getAgentList().size() == 0){
            answer.setSuccess(false);
            answer.setMessage("Cannot join game without agents");
        }else if(taskSize <= 0){
            answer.setSuccess(false);
            answer.setMessage("Invalid task size. please positive number");
        }
        return answer;
    }
}
