package me.digitalfoodscout

import org.semanticweb.HermiT.ReasonerFactory
import org.semanticweb.owlapi.apibinding.OWLManager
import org.semanticweb.owlapi.model.*
import org.semanticweb.owlapi.reasoner.OWLReasoner

import java.io.File
import java.util.ArrayList

class OWLScout @Throws(OWLOntologyCreationException::class)
constructor() {

    data class OccursWithRelation(val intolerance: String, val min_appearance_after: Int, val min_appearance_until: Int, val commonness: String)
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

    fun getOccursWith(symptomName: String): Array<OccursWithRelation> {
        val occursWith = dataFactory.getOWLObjectProperty(prefix + "occurs_with")
        val occursWithIntolerances = dataFactory.getOWLObjectProperty(prefix + "occurs_with_intolerance")
        val occursWithCommonness = dataFactory.getOWLObjectProperty(prefix + "occurs_with_commonness")
        val min_appearance_after = dataFactory.getOWLDataProperty(prefix + "min_appearance_after")
        val min_appearance_until = dataFactory.getOWLDataProperty(prefix + "min_appearance_until")

        val symptom = dataFactory.getOWLNamedIndividual(prefix + symptomName)

        val intoleranceRelations = reasoner.getObjectPropertyValues(symptom, occursWith)

        val intolerances = ArrayList<OccursWithRelation>()

        intoleranceRelations.forEach { relation ->
            val repRelation = relation.representativeElement

            val min_appearance_after = reasoner.getDataPropertyValues(repRelation, min_appearance_after).first().parseInteger()
            val min_appearance_until = reasoner.getDataPropertyValues(repRelation, min_appearance_until).first().parseInteger()

            val intoleranceIRI = reasoner.getObjectPropertyValues(repRelation, occursWithIntolerances).first().representativeElement.iri.shortForm
            val commonnnessIRI = reasoner.getObjectPropertyValues(repRelation, occursWithCommonness).first().representativeElement.iri.shortForm


            val intolerance = OccursWithRelation(intoleranceIRI, min_appearance_after, min_appearance_until, commonnnessIRI)

            intolerances.add(intolerance)
        }

        return intolerances.toTypedArray()
    }
}
