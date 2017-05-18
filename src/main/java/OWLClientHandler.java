import com.etsy.net.UnixDomainSocket;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;

import java.io.*;

public class OWLClientHandler extends Thread {

    private UnixDomainSocket socket;
    private OWLScout scout;

    public OWLClientHandler(OWLScout scout, UnixDomainSocket socket) {
        this.scout = scout;
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        try (PrintStream printStream = new PrintStream(socket.getOutputStream())) {
            String command = bufferedReader.readLine();

            switch (command) {
                case "getIndividualTypes":
                    String individualID = bufferedReader.readLine();
                    OWLNamedIndividual individual = scout.getIndividualByID(individualID);

                    if(individual ==  null) {
                        printStream.print("Individual not found");
                        break;
                    }

                    // Get types of individual
                    NodeSet<OWLClass> individualTypes = scout.getReasoner().getTypes(individual, false);

                    // Print types of individual
                    for (Node node : individualTypes) {
                        OWLClass repClass = (OWLClass) node.getRepresentativeElement();

                        printStream.println(repClass.getIRI().getShortForm());
                    }

                    break;
                default:
                    printStream.print("Unknown command");
            }

            bufferedReader.close();

        } catch (IOException e) {
            System.err.print(e.getMessage());
            e.printStackTrace();
        }
    }
}
