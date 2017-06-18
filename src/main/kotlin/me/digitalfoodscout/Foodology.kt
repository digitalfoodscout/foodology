package me.digitalfoodscout

import org.semanticweb.owlapi.model.OWLOntologyCreationException
import spark.Spark.*
import com.google.gson.Gson

fun main(args: Array<String>) {
    val owlScout: OWLScout
    val gson = Gson()

    try {
        owlScout = OWLScout()

        get("/occuresBy/:symptom", { request, _ ->
            val symptom = request.params(":symptom")

            val intolerances = owlScout.getOccuresBy(symptom)

            intolerances
        }, gson::toJson )
    } catch (e: OWLOntologyCreationException) {
        e.printStackTrace()
        return
    }
}
