import com.google.gson.Gson;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;

import java.io.*;
import java.net.Socket;

public class OWLClientHandler extends Thread {

    private Socket socket;
    private OWLScout scout;

    public OWLClientHandler(OWLScout scout, Socket socket) {
        this.scout = scout;
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintStream printStream = new PrintStream(socket.getOutputStream())
        ) {
            String command = bufferedReader.readLine();

            switch (command) {
                case "getOccuresBy":
                    String symptom = bufferedReader.readLine();
                    OccuresByResult intolerances = scout.getOccuresBy(symptom);

                    Gson gson = new Gson();
                    printStream.print(gson.toJson(intolerances));

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
