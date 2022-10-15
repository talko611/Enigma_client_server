package com.enigma.servlets.allie;

import com.engine.Engine;
import com.engine.users.battlefield.Battlefield;
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

import java.io.IOException;
import java.util.UUID;

@WebServlet("/allie/set_task")
public class SetTaskSize extends HttpServlet {
    private static final Gson GSON_SERVICE = new Gson();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestServerAnswer answer = new RequestServerAnswer();
        try{
            UUID clientId = UUID.fromString(req.getParameter("id"));
            long taskSize = Long.parseLong(req.getParameter("taskSize"));
            UserManager userManager = ServletsUtils.getUserManager(getServletContext());
            Engine engine = ServletsUtils.getEngine(getServletContext());
            Allie user = userManager.getAllieById(clientId);
            Battlefield battlefield = userManager.getBattlefieldById(user.getBattlefieldId());
            if(validateRequest(battlefield,taskSize)){
                CalculationOperationAnswer<Long> calcNumOfTasks = engine.calculateNumberOfTasks(battlefield.getEnigmaParts().getBattlefieldParts().getDifficulty(),
                        taskSize,
                        battlefield.getEnigmaParts().getMachineParts(),
                        battlefield.getMachine().getRotors().size());
                if(calcNumOfTasks.isSuccess()){
                    user.setTaskSize(taskSize);
                    user.setNumOfTasks(calcNumOfTasks.getData());
                    answer.setSuccess(true);
                    answer.setMessage("Task size is set");
                }
                else {
                    answer.setSuccess(false);
                    answer.setMessage(calcNumOfTasks.getMessage());
                }
            }else{
                answer.setMessage("UBoat is not define machine yet please try later");
                answer.setSuccess(false);
            }
            resp.getWriter().println(GSON_SERVICE.toJson(answer));
        }catch (NullPointerException e){
            //Todo- redirect to login page
        }catch (NumberFormatException e){
            answer.setSuccess(false);
            answer.setMessage("Program do not support that size of task size");
            resp.getWriter().println(GSON_SERVICE.toJson(answer));
        }
    }

    private boolean validateRequest(Battlefield battlefield, long taskSize){
        return battlefield.getMachine().getInitialConfiguration() != null
        && taskSize > 0;
    }
}
