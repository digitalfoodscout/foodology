package me.digitalfoodscout

import org.semanticweb.HermiT.ReasonerFactory
import org.semanticweb.owlapi.apibinding.OWLManager
import org.semanticweb.owlapi.model.*
import org.semanticweb.owlapi.reasoner.OWLReasoner

import java.io.File
import java.util.ArrayList

class OWLScout @Throws(OWLOntologyCreationException::class)
constructor() {

    data class OccursWithResult(val often: Array<String>, val seldom: Array<String>)
    class OntologyNotConsistentException : RuntimeException()

    private val reasoner: OWLReasoner
    private val ontology: OWLOntology
    private val dataFactory: OWLDataFactory
    private val manager: OWLOntologyManager = OWLManager.createOWLOntologyManager()
    private val prefix = "http://digitalfoodscout.me/symptom#"

    init {
        // initialize OWLAPI
        dataFactory = manager.owlDataFactory

        // load ontology from file (mapped by ontop)
        ontology = manager.loadOntologyFromOntologyDocument(File("src/main/resources/symptom.owl"))

        // Create reasoner
        //reasoner = new JFactFactory().createReasoner(ontology);
        reasoner = ReasonerFactory().createReasoner(ontology)

        // check if ontology is consistent
        if (!reasoner.isConsistent) {
            throw OntologyNotConsistentException()
        }
    }

    fun getOccursWith(symptomName: String): OccursWithResult {
        val symptom = dataFactory.getOWLNamedIndividual(prefix + symptomName)

        val occursOftenWith = dataFactory.getOWLObjectProperty(prefix + "Tritt_Häufig_Auf_Bei")
        val occursSeldomWith = dataFactory.getOWLObjectProperty(prefix + "Tritt_Selten_Auf_Bei")

        val intolerancesOftenNodes = reasoner.getObjectPropertyValues(symptom, occursOftenWith)
        val intolerancesSeldomNodes = reasoner.getObjectPropertyValues(symptom, occursSeldomWith)

        val intolerancesOften = ArrayList<String>()
        val intolerancesSeldom = ArrayList<String>()

        intolerancesOftenNodes.forEach { propertyValue -> intolerancesOften.add(propertyValue.representativeElement.iri.shortForm) }
        intolerancesSeldomNodes.forEach { propertyValue -> intolerancesSeldom.add(propertyValue.representativeElement.iri.shortForm) }

        return OccursWithResult(intolerancesOften.toTypedArray(), intolerancesSeldom.toTypedArray())
    }
}
