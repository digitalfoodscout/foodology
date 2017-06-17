package me.digitalfoodscout

import org.semanticweb.HermiT.ReasonerFactory
import org.semanticweb.owlapi.apibinding.OWLManager
import org.semanticweb.owlapi.model.*
import org.semanticweb.owlapi.reasoner.OWLReasoner

import java.io.File
import java.util.ArrayList

class OWLScout @Throws(OWLOntologyCreationException::class)
constructor() {

    private val reasoner: OWLReasoner
    private val ontology: OWLOntology
    private val dataFactory: OWLDataFactory
    private val manager: OWLOntologyManager

    init {
        // initialize OWLAPI
        manager = OWLManager.createOWLOntologyManager()
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

    data class OccuresByResult(val often: Array<String>, val seldom: Array<String>)

    fun getOccuresBy(symptom: String): OccuresByResult {
        val herzrasen = dataFactory.getOWLNamedIndividual(prefix + symptom)

        val occuresOftenBy = dataFactory.getOWLObjectProperty(prefix + "Tritt_Häufig_Auf_Bei")
        val occuresSeldomBy = dataFactory.getOWLObjectProperty(prefix + "Tritt_Selten_Auf_Bei")

        val intolerancesOftenNodes = reasoner.getObjectPropertyValues(herzrasen, occuresOftenBy)
        val intolerancesSeldomNodes = reasoner.getObjectPropertyValues(herzrasen, occuresSeldomBy)

        val intolerancesOften = ArrayList<String>()

        intolerancesOftenNodes.forEach { propertyValue -> intolerancesOften.add(propertyValue.representativeElement.iri.shortForm) }

        val intolerancesSeldom = ArrayList<String>()

        intolerancesSeldomNodes.forEach { propertyValue -> intolerancesSeldom.add(propertyValue.representativeElement.iri.shortForm) }

        return OccuresByResult(intolerancesOften.toTypedArray(), intolerancesSeldom.toTypedArray())
    }

    companion object {
        private val prefix = "http://digitalfoodscout.me/symptom#"
    }
}
