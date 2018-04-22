package ru.zuma;

import ru.zuma.data.RPILogManager;
import ru.zuma.utils.JSONConverter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class PostLogServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        char[] buffer = new char[4096];
        int dataSize = reader.read(buffer);

        String json = new String(buffer, 0, dataSize);
        resp.getWriter().print(json);

        String log = JSONConverter.jsonToLog(json);

        System.out.println("doPost: " + log);
        RPILogManager.instance().addLogRecord(log);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
