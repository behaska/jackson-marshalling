package fr.behaska.kotlin.examples.jacksonmarshalling

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.orange.ccmd.katalog.api.domain.models.Catalog
import org.slf4j.LoggerFactory
import java.io.IOException

fun main() {
    val logger = LoggerFactory.getLogger(Main::class.java)

    val json = ("{\n"
            + "\"content\": \"foo\",\n"
            + "\"metadata\":{\n"
            + "\"name\": \"Catalog-foo-bar\",\n"
            + "\"date\": \"2019-05-03T15:14:48.347445\",\n"
            + "\"size\": 8709,\n"
            + "\"hash\": \"2bbd420a3322e7b9360e8ca207af9b0dee8bcb0ef2bc885a349ddd7c0d6cdeb4\",\n"
            + "\"duration\": 6\n"
            + "}\n"
            + "}")

    val mapper = ObjectMapper()
    mapper
            .registerModule(Jdk8Module())
            .registerModule(JavaTimeModule())
            .registerModule(KotlinModule())
            .setSerializationInclusion(JsonInclude.Include.NON_ABSENT)

    val catalog: Catalog

    try {
        catalog = mapper.readValue(json, Catalog::class.java)
        logger.debug("Date du catalogue : {}", catalog.metadata.date)
    } catch (e: IOException) {
        logger.error(" IOException during marshalling : {}", e)
    }

}