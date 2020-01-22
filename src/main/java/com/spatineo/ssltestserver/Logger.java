package com.spatineo.ssltestserver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private File LOG = null;
    public Logger() {}
    public Logger(String path) {
        initLog(path);
    }

    private void initLog(String logPath) {
        File log = new File(logPath);
        System.out.println("Path to log: " + logPath);

        try {
            if(!log.exists()){
                log.createNewFile();
            }
        } catch(IOException e) {
            System.err.println("The path: \"" + logPath + "\" is invalid");
            return;
        }
        LOG = log;
        write("Logger started");
    }

    public void write(Exception e) {
        write(e.getMessage());
        for(int i = 0; i < e.getStackTrace().length; i++) {
            write(e.getStackTrace()[i].toString());
        }
    }

    public void write(String data) {
        if(LOG != null) {
            try {
                FileWriter fw = new FileWriter(LOG, true);
                fw.write(getTime() + data);
                fw.write("\n");
                fw.flush();
                fw.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    private static String getTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now) + " # ";
    }
}
