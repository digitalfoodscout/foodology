package me.digitalfoodscout

import com.google.gson.Gson

import java.io.*
import java.net.Socket

class OWLClientHandler(private val scout: OWLScout, private val socket: Socket) : Thread() {

    override fun run() {
        try {
            BufferedReader(InputStreamReader(socket.getInputStream())).use { bufferedReader ->
                PrintStream(socket.getOutputStream()).use { printStream ->
                    val command = bufferedReader.readLine()

                    when (command) {
                        "getOccuresBy" -> {
                            val symptom = bufferedReader.readLine()
                            val intolerances = scout.getOccuresBy(symptom)

                            val gson = Gson()
                            printStream.print(gson.toJson(intolerances))
                        }
                        else -> printStream.print("Unknown command")
                    }

                    bufferedReader.close()
                }
            }
        } catch (e: IOException) {
            System.err.print(e.message)
            e.printStackTrace()
        }
    }
}
