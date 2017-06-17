package me.digitalfoodscout

import org.semanticweb.owlapi.model.OWLOntologyCreationException
import java.io.IOException
import java.net.ServerSocket

fun main(args: Array<String>) {
    val owlScout: OWLScout

    try {
        owlScout = OWLScout()
    } catch (e: OWLOntologyCreationException) {
        e.printStackTrace()
        return
    }

    try {
        ServerSocket(23487).use { serverSocket ->
            while (true) {
                val socket = serverSocket.accept()

                val clientHandler = OWLClientHandler(owlScout, socket)
                clientHandler.start()
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}
