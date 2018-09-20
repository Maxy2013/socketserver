package web;

import java.io.*;
import java.net.Socket;

/**
 * @author lz
 */
public class WebClient implements Runnable{


    private Socket socket;

    public WebClient(Socket socket){
        this.socket = socket;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        System.out.println(socket + "connected!");
        try {
            LineNumberReader lineReader = new LineNumberReader(new InputStreamReader(socket.getInputStream()));
            String lineInput;
            String requestPage = null;
            while ((lineInput = lineReader.readLine()) != null) {
                System.out.println("line info" + lineInput);
                if (lineReader.getLineNumber() == 1){
                    requestPage=lineInput.substring(lineInput.indexOf("/")+1,lineInput.lastIndexOf(' '));
                    System.out.println("requestPage:" + requestPage);
                }else {
                    if(lineInput.isEmpty()){
                        System.out.println("connection finished...");
                        doResponseGet(requestPage,socket);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void doResponseGet(String requestPage, Socket socket) throws IOException {

        File file=new File("F:",requestPage);
        OutputStream out=socket.getOutputStream();
        if (file.exists()){
            InputStream fileIn=new FileInputStream(file);
            System.out.println(fileIn.available());
            byte[] buf=new byte[fileIn.available()];
            fileIn.read(buf);
            fileIn.close();
            out.write(buf);
            out.flush();
            socket.close();
            System.out.println("complete");
        }else {
            String msg="I can't find bao zang cry   \r\n";
            String response="HTTP/1.1 200 OK\r\n";
            response+="Content-Length: "+(msg.length()-4)+"\r\n";
            response+="\r\n";
            response+=msg;
            out.write(response.getBytes());
            out.flush();
        }
    }
}
