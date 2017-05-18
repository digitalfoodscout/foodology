import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
//import uk.ac.manchester.cs.jfact.JFactFactory;

import java.io.File;
import java.util.Optional;
import java.util.stream.Stream;

public class OWLScout {
    private static final String prefix = "http://www.semanticweb.org/user/ontologies/2017/4/dfs_ontology#";

    private OWLReasoner reasoner;
    private OWLOntology ontology;
    private OWLDataFactory dataFactory;
    private OWLOntologyManager manager;

    public OWLScout() throws OWLOntologyCreationException {
        // initialize OWLAPI
        manager = OWLManager.createOWLOntologyManager();
        dataFactory = manager.getOWLDataFactory();

        // load ontology from file (mapped by ontop)
        ontology = manager.loadOntologyFromOntologyDocument(new File("src/main/resources/food.owl"));

        // Create reasoner
        //reasoner = new JFactFactory().createReasoner(ontology);
        reasoner = new  ReasonerFactory().createReasoner(ontology);

        // check if ontology is consistent
        if (!reasoner.isConsistent()) {
            throw new OntologyNotConsistentException();
        }
    }

    public OWLNamedIndividual getIndividualByID(String key) {
        // Get individual by id
        OWLDataProperty property = dataFactory.getOWLDataProperty(prefix + "key");
        OWLClassExpression expression = dataFactory.getOWLDataHasValue(property, dataFactory.getOWLLiteral(key));
        Stream<OWLNamedIndividual> individuals = reasoner.getInstances(expression, false).entities();

        Optional<OWLNamedIndividual> optionalIndividual = individuals.findFirst();

        return optionalIndividual.orElse(null);
    }

    public static String getPrefix() {
        return prefix;
    }

    public OWLReasoner getReasoner() {
        return reasoner;
    }

    public OWLOntology getOntology() {
        return ontology;
    }

    public OWLDataFactory getDataFactory() {
        return dataFactory;
    }

    public OWLOntologyManager getManager() {
        return manager;
    }

    // get "Viel_Histamin" owl class
    //OWLClass vielHistamin = owlScout.getDataFactory().getOWLClass(owlScout.getPrefix() + "#Viel_Histamin");

    // Print if product has "Viel Histamin"
    //System.out.println(individualTypes.containsEntity(vielHistamin));
}
