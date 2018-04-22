package ru.zuma.data;

import ru.zuma.Client;

import java.util.HashMap;
import java.util.Map;

public class RPILogManager {
    private int recordIndex;
    private HashMap<Integer, String> logRecords;
    private boolean isNewRecords;

    public RPILogManager() {
        logRecords = new HashMap<>();
    }

    public synchronized void addLogRecord(String record) {
        if (logRecords.size() > 100) {
            // Remove smolest index
            logRecords.remove(recordIndex - logRecords.size());
        }

        logRecords.put(recordIndex++, record);
        this.notifyAll();
    }

    public synchronized int getLastRecordIndex() {
        return recordIndex;
    }

    public synchronized HashMap<Integer, String> getNewLogRecords(Client client) {
        HashMap<Integer, String> result = new HashMap<>();

        for (Map.Entry<Integer, String> entry: logRecords.entrySet()) {
            if (entry.getKey() > client.lastReceivedRecord) {
                result.put(entry.getKey(), entry.getValue());
            }
        }

        return result;
    }

    public synchronized HashMap<Integer, String> getNewLogRecords(int lastReceivedRecord) {
        HashMap<Integer, String> result = new HashMap<>();

        for (Map.Entry<Integer, String> entry: logRecords.entrySet()) {
            if (entry.getKey() > lastReceivedRecord) {
                result.put(entry.getKey(), entry.getValue());
            }
        }

        return result;
    }

    public synchronized HashMap<Integer,String> waitNewLogRecords(int lastReceivedRecord, int maxDelay) {
        boolean existNewKeys = false;

        for (Integer key: logRecords.keySet()) {
            if (key > lastReceivedRecord) {
                return getNewLogRecords(lastReceivedRecord);
            }
        }

        try {
            int oldRecordIndex = recordIndex;
            long currTime = System.currentTimeMillis();
            long beginTime = currTime;

            while ( recordIndex <= oldRecordIndex & currTime - beginTime < maxDelay ) {
                long delay = maxDelay - (currTime - beginTime);
                delay = delay > 0 ? delay : 0;
                this.wait(delay);
                currTime = System.currentTimeMillis();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return getNewLogRecords(lastReceivedRecord);
    }

    public synchronized HashMap<Integer, String> getRecords() {
        return (HashMap<Integer, String>) logRecords.clone();
    }

    private static RPILogManager instance;
    static {
        instance = new RPILogManager();
    }

    public static RPILogManager instance() {
        return instance;
    }
}
