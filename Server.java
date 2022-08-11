import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static void main(String[] argv) {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(3000); // create a server socket
            System.out.println("Sunucu Çalışıyor...");

            ExecutorService clientPool = Executors.newFixedThreadPool(200); // limit the clients to 200

            while (true) {
                clientPool.execute(new ClientHandler(ss.accept())); // accept the coming client request and start a
                                                                    // thread
            }

        } catch (IOException e) {
            System.out.println(e);
        } finally {
            try {
                if (ss != null)
                    ss.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        System.out.println("Sunucu Durdu.");
    }

    private static class ClientHandler extends Thread { // client handler thread class
        private Socket s;

        ClientHandler(Socket s) { // constructor gets a socket
            this.s = s;
        }

        public void run() {
            try {
                DataInputStream din = new DataInputStream(s.getInputStream());
                DataOutputStream dout = new DataOutputStream(s.getOutputStream());

                String username = din.readUTF(); // get a username from client
                System.out.println("'" + username + "' " + "sunucuya bağlandı.");

                File txtFile = new File("./sendToClient.txt"); // create a file class
                byte[] bytes = new byte[8192];
                InputStream fin = new FileInputStream(txtFile);

                int count;
                while ((count = fin.read(bytes)) > 0) { // read from file and add the bytes array
                    dout.write(bytes, 0, count);
                }

                System.out.println(username + " adlı kullanıcı bağlantıdan ayrıldı.");

                fin.close();
                dout.close();
                s.close();

            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}