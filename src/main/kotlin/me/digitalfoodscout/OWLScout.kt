package me.digitalfoodscout

import org.semanticweb.HermiT.ReasonerFactory
import org.semanticweb.owlapi.apibinding.OWLManager
import org.semanticweb.owlapi.model.*
import org.semanticweb.owlapi.reasoner.OWLReasoner

import java.io.File
import java.util.ArrayList

class OWLScout @Throws(OWLOntologyCreationException::class)
constructor() {

    data class OccuresByResult(val often: Array<String>, val seldom: Array<String>)
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

    fun getOccuresBy(symptomName: String): OccuresByResult {
        val symptom = dataFactory.getOWLNamedIndividual(prefix + symptomName)

        val occuresOftenBy = dataFactory.getOWLObjectProperty(prefix + "Tritt_Häufig_Auf_Bei")
        val occuresSeldomBy = dataFactory.getOWLObjectProperty(prefix + "Tritt_Selten_Auf_Bei")

        val intolerancesOftenNodes = reasoner.getObjectPropertyValues(symptom, occuresOftenBy)
        val intolerancesSeldomNodes = reasoner.getObjectPropertyValues(symptom, occuresSeldomBy)

        val intolerancesOften = ArrayList<String>()
        val intolerancesSeldom = ArrayList<String>()

        intolerancesOftenNodes.forEach { propertyValue -> intolerancesOften.add(propertyValue.representativeElement.iri.shortForm) }
        intolerancesSeldomNodes.forEach { propertyValue -> intolerancesSeldom.add(propertyValue.representativeElement.iri.shortForm) }

        return OccuresByResult(intolerancesOften.toTypedArray(), intolerancesSeldom.toTypedArray())
    }
}
