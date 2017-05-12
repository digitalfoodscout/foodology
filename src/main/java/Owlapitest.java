import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import uk.ac.manchester.cs.jfact.JFactFactory;

import java.io.File;

public class Owlapitest {
    private static final String prefix = "http://www.semanticweb.org/user/ontologies/2017/4/dfs_ontology";

    public static void main(String args[]) {
        // initialize OWLAPI
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLDataFactory dataFactory = manager.getOWLDataFactory();

        OWLOntology ontology = null;

        // load ontology from file (mapped by ontop)
        try {
            ontology = manager.loadOntologyFromOntologyDocument(new File("src/main/resources/dump.owl"));
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
            System.exit(3);
        }

        // Create reasoner
        OWLReasoner reasoner = new JFactFactory().createReasoner(ontology);

        // check if ontology is consistent
        if(!reasoner.isConsistent()) {
            throw new RuntimeException("Ontology is not consistent!");
        }

        // Get specific individual
        OWLNamedIndividual individual =  dataFactory.getOWLNamedIndividual(IRI.create(prefix + "#Vollkornbrot5401"));

        // Get types of individual
        NodeSet<OWLClass> individualTypes = reasoner.getTypes(individual, false);

        // Print types of individual
        for(Node node : individualTypes) {
            System.out.println(node.toString());
        }

        // get "Viel_Histamin" owl class
        OWLClass vielHistamin = dataFactory.getOWLClass(prefix + "#Viel_Histamin");

        // Print if product has "Viel Histamin"
        System.out.println(individualTypes.containsEntity(vielHistamin));
    }
}
