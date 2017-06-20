package me.digitalfoodscout

import com.google.gson.Gson
import org.semanticweb.owlapi.model.OWLOntologyCreationException
import spark.Spark

fun initRoutes() {
    val owlScout: OWLScout
    val gson = Gson()

    try {
        owlScout = OWLScout()

        Spark.get("/occursWith/:symptom", { request, _ ->
            val symptom = request.params(":symptom")
            val intolerances = owlScout.getOccursWith(symptom)

            intolerances
        }, gson::toJson)
    } catch (e: OWLOntologyCreationException) {
        e.printStackTrace()
    }
}