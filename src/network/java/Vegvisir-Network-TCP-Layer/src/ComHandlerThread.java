import java.net.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

public class ComHandlerThread extends Thread {
    protected Socket socket;
    public ComHandlerThread(Socket clientSocket) {
        this.socket = clientSocket;
    }

    public void send(String message)
    {
        try {
            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(message);
            System.out.println("Message sent to the client is " + message);
            bw.flush();
        }
        catch (IOException e){ e.printStackTrace(); }
    }

    public void run() {
        InputStream inp = null;
        BufferedReader brinp = null;
        DataOutputStream out = null;

        while (true) {
            try {
                // get input stream
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String received_message = br.readLine();
                System.out.println("Received a message " + received_message);

            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            String toSendMessage  = "block";

            // try sending a message
            this.send(toSendMessage);

            // sleep for ten seconds

            try
            {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) { e.printStackTrace(); }

        }
    }
}
