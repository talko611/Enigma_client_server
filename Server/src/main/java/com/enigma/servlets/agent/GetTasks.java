package com.enigma.servlets.agent;

import com.engine.users.UserManager;
import com.enigma.dtos.dataObjects.DecryptionTaskData;
import com.enigma.servlets.ServletsUtils;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@WebServlet("/agent/get_tasks")
public class GetTasks extends HttpServlet {
    private final Gson GSON_SERVICE = new Gson();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

            List<DecryptionTaskData> decryptionTaskDataList = new ArrayList<>();
            DecryptionTaskData decryptionTaskData = new DecryptionTaskData();
            decryptionTaskData.setTaskSize(1000);
            decryptionTaskData.setReflectorId(1);
            decryptionTaskData.setOffsets(Arrays.asList(24,21,4,1,0));
            decryptionTaskData.setRotorsId(Arrays.asList(1,2,4,3,5));
            decryptionTaskDataList.add(decryptionTaskData);
            decryptionTaskData = new DecryptionTaskData();
            decryptionTaskData.setTaskSize(995);
            decryptionTaskData.setReflectorId(2);
            decryptionTaskData.setOffsets(Arrays.asList(4,1,34,11,10));
            decryptionTaskData.setRotorsId(Arrays.asList(1,2,5,3,9));
            decryptionTaskDataList.add(decryptionTaskData);
            resp.getWriter().println(GSON_SERVICE.toJson(decryptionTaskDataList));
    }
}
