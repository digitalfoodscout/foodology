package me.digitalfoodscout

import org.junit.rules.ExternalResource
import spark.Spark.awaitInitialization
import spark.Spark.stop

class SparkServerRule : ExternalResource() {
    override fun before() {
        initRoutes()
        awaitInitialization()
    }

    override fun after() {
        stop()
    }
}