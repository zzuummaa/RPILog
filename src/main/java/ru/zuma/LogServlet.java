package ru.zuma;

import ru.zuma.data.RPILogManager;
import ru.zuma.utils.JSONConverter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class LogServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("doGet");

        boolean clearHistory = Boolean.parseBoolean(req.getParameter("clear_history"));
        if (clearHistory) {
            RPILogManager.instance().clearLogHistory();
            return;
        }

        HashMap<Integer, String> rpiRecords;

        String lastRecord = req.getParameter("last_record");
        lastRecord = lastRecord == null ? "" : lastRecord;

        int lastRecordIndex;

        if (!lastRecord.equals("")) {
            try {
                lastRecordIndex = Integer.parseInt(lastRecord);
            } catch (NumberFormatException e) {
                lastRecordIndex = -1;
            }
        } else {
            lastRecordIndex = -1;
        }

        if (lastRecordIndex < 0) {

            System.out.println("new client");
            rpiRecords = RPILogManager.instance().getRecords();
            if (rpiRecords.isEmpty()) {
                rpiRecords = RPILogManager.instance().waitNewLogRecords(lastRecordIndex, 30_000);
            }

        } else {

            System.out.println("old client");
            rpiRecords = RPILogManager.instance().waitNewLogRecords(lastRecordIndex, 30_000);

        }

        String logAsJson = JSONConverter.fromLogRecords(rpiRecords, lastRecordIndex);
        System.out.println("json: " + logAsJson);
        resp.getWriter().print(logAsJson);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}
