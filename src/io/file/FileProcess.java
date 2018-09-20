package io.file;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lz
 * 2018/9/20 11:06
 */
public class FileProcess {

    public static void main(String[] args) {
        try {
            new FileProcess().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() throws Exception{
        String pathDev = "resource/dev_file.sql";
        String pathStage = "resource/sys_auth.sql";
        String destFile = "resource/update.sql";

        File fileDev = new File(pathDev);
        File fileStage = new File(pathStage);

        FileReader readerDev = new FileReader(fileDev);
        FileReader readerStage = new FileReader(fileStage);

        BufferedReader dev = new BufferedReader(readerDev);
        BufferedReader stage = new BufferedReader(readerStage);

        String lineDev = null;
        String lineStage = null;

        Map<String, String> mapDev = new HashMap<>();
        Map<String, String> mapStage = new HashMap<>();

        while ((lineDev = dev.readLine()) != null){
            String content = lineDev.substring(lineDev.lastIndexOf("values (") + 8, lineDev.lastIndexOf(')'));
            String[] column = content.split(",");
            mapDev.put(column[0], column[column.length-1]);
        }
        readerDev.close();

        while ((lineStage = stage.readLine()) != null){
            String content = lineStage.substring(lineStage.lastIndexOf("values (") + 8, lineStage.lastIndexOf(')'));
            String[] column = content.split(",");
            mapStage.put(column[0], column[column.length-1]);
        }
        readerStage.close();


        StringBuilder content = new StringBuilder();
        for(Map.Entry<String, String> entryDev : mapDev.entrySet()){
            String keyDev = entryDev.getKey();
            String valueDev = entryDev.getValue();
            for(Map.Entry<String, String> entryStage : mapStage.entrySet()){
                String keyStage = entryStage.getKey();
                String valueStage = entryStage.getValue();
                if(keyDev.equals(keyStage) && !valueDev.equals(valueStage)){
                    content.append("UPDATE SYS_AUTH SET AUTH_URL = " + valueDev + " WHERE AUTH_ID = " + keyDev + ";\r\n");
                }
            }
        }

        File file = new File(destFile);
        FileOutputStream fos = new FileOutputStream(file);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"), 1024);
        bufferedWriter.write(content.toString());
        bufferedWriter.flush();
        fos.close();

    }
}
