import org.semanticweb.owlapi.model.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Foodology {
    public static void main(String args[]) {
        OWLScout owlScout;

        try {
            owlScout = new OWLScout();
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
            return;
        }

        try (
                ServerSocket serverSocket = new ServerSocket(23487);
        )
        {
            //noinspection InfiniteLoopStatement
            while (true) {
                Socket socket = serverSocket.accept();

                OWLClientHandler clientHandler = new OWLClientHandler(owlScout, socket);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
