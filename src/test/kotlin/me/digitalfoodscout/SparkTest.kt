package me.digitalfoodscout

import org.junit.ClassRule
import org.junit.Test

class SparkTest {
    companion object {
        @ClassRule @JvmField
        val sparkServer = SparkServerRule()
    }

    @Test
    fun testBasicRule() {
        println("It works!")
    }
}