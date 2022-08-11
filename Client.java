import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {

    public static void main(String[] argv) {

        try {
            Socket s = new Socket("localhost", 3000); // create a socket
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            DataInputStream din = new DataInputStream(s.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Kullanıcı adınızı giriniz : ");
            String username = br.readLine();
            dout.writeUTF(username); // send the username to the server
            dout.flush();

            byte[] bytes = new byte[8192];

            int count;
            while ((count = din.read(bytes)) > 0) { // read the bytes of file from server and print it
                System.out.println(new String(bytes));
            }

            s.close();
        } catch (IOException e) {
            System.out.println(e);
        }

    }
}
