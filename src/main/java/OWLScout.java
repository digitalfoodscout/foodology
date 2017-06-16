import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OWLScout {
    private static final String prefix = "http://digitalfoodscout.me/symptom#";

    private OWLReasoner reasoner;
    private OWLOntology ontology;
    private OWLDataFactory dataFactory;
    private OWLOntologyManager manager;

    public OWLScout() throws OWLOntologyCreationException {
        // initialize OWLAPI
        manager = OWLManager.createOWLOntologyManager();
        dataFactory = manager.getOWLDataFactory();

        // load ontology from file (mapped by ontop)
        ontology = manager.loadOntologyFromOntologyDocument(new File("src/main/resources/symptom.owl"));

        // Create reasoner
        //reasoner = new JFactFactory().createReasoner(ontology);
        reasoner = new  ReasonerFactory().createReasoner(ontology);

        // check if ontology is consistent
        if (!reasoner.isConsistent()) {
            throw new OntologyNotConsistentException();
        }
    }

    public OccuresByResult getOccuresBy(String symptom) {
        OWLNamedIndividual herzrasen = dataFactory.getOWLNamedIndividual(prefix + symptom);

        OWLObjectProperty occuresOftenBy = dataFactory.getOWLObjectProperty(prefix + "Tritt_Häufig_Auf_Bei");
        OWLObjectProperty occuresSeldomBy = dataFactory.getOWLObjectProperty(prefix + "Tritt_Selten_Auf_Bei");

        NodeSet<OWLNamedIndividual> intolerancesOftenNodes = reasoner.getObjectPropertyValues(herzrasen, occuresOftenBy);
        NodeSet<OWLNamedIndividual> intolerancesSeldomNodes = reasoner.getObjectPropertyValues(herzrasen, occuresSeldomBy);

        List<String> intolerancesOften = new ArrayList<>();

        intolerancesOftenNodes.forEach(propertyValue -> {
            intolerancesOften.add(propertyValue.getRepresentativeElement().getIRI().getShortForm());
        });

        List<String> intolerancesSeldom = new ArrayList<>();

        intolerancesSeldomNodes.forEach(propertyValue -> {
            intolerancesSeldom.add(propertyValue.getRepresentativeElement().getIRI().getShortForm());
        });

        return new OccuresByResult(intolerancesOften.toArray(new String[intolerancesOften.size()]), intolerancesSeldom.toArray(new String[intolerancesSeldom.size()]));
    }
}
