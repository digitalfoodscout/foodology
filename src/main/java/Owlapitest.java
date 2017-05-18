import com.etsy.net.JUDS;
import com.etsy.net.UnixDomainSocket;
import com.etsy.net.UnixDomainSocketServer;
import org.semanticweb.owlapi.model.*;
import java.io.File;
import java.io.IOException;

public class Owlapitest {
    public static void main(String args[]) {
        OWLScout owlScout;
        String socketPath = "owl.socket";

        try {
            owlScout = new OWLScout();
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
            return;
        }

        // Delete existing socket (if present)
        //noinspection ResultOfMethodCallIgnored
        new File(socketPath).delete();

        UnixDomainSocketServer domainSocketServer = null;

        try {
            // Create server
            domainSocketServer = new UnixDomainSocketServer(socketPath, JUDS.SOCK_STREAM, 5);

            // Wait for clients
            //noinspection InfiniteLoopStatement
            while (true) {
                UnixDomainSocket socket = domainSocketServer.accept();

                OWLClientHandler clientHandler = new OWLClientHandler(owlScout, socket);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(domainSocketServer != null)
                domainSocketServer.close();
        }
    }
}
