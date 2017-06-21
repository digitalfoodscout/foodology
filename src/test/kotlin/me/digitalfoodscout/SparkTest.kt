package me.digitalfoodscout

import com.mashape.unirest.http.Unirest
import org.junit.ClassRule
import org.junit.Test

import org.assertj.core.api.Assertions.*
import org.json.JSONArray

class SparkTest {
    companion object {
        @ClassRule @JvmField
        val sparkServer = SparkServerRule()
    }

    @Test
    fun testBasicRule() {
        val jsonResponse = Unirest.get("http://localhost:4567/occursWith/Erbrechen")
                .asJson()

        assertThat(jsonResponse.status).isEqualTo(200)

        val jsonNode = jsonResponse.body
        assertThat(jsonNode.isArray).isFalse()

        val jsonObject = jsonNode.`object`

        val listOften = jsonObject.get("often") as JSONArray
        val listSeldom = jsonObject.get("seldom") as JSONArray

        assertThat(listOften).hasSize(1).contains("Lactose")
        assertThat(listSeldom).hasSize(0)
    }
}