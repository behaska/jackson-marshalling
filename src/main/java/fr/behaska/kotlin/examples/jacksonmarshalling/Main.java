package fr.behaska.kotlin.examples.jacksonmarshalling;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.kotlin.KotlinModule;

import fr.behaska.examples.kotlin.api.domain.models.Catalog;

public class Main {

    public static void main(String[] args) {

        final Logger logger = LoggerFactory.getLogger(Main.class);

        String json = "{\n"
                + "\"content\": \"foo\",\n"
                + "\"metadata\":{\n"
                + "\"name\": \"Catalog-foo-bar\",\n"
                + "\"date\": \"2019-05-03T15:14:48.347445\",\n"
                + "\"size\": 8709,\n"
                + "\"hash\": \"2bbd420a3322e7b9360e8ca207af9b0dee8bcb0ef2bc885a349ddd7c0d6cdeb4\",\n"
                + "\"duration\": 6\n"
                + "}\n"
                + "}";

        final ObjectMapper mapper = new ObjectMapper();
        mapper
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule())
                .registerModule(new KotlinModule())
                .setSerializationInclusion(JsonInclude.Include.NON_ABSENT);

        final Catalog catalog;

        try {
            catalog = mapper.readValue(json, Catalog.class);
            logger.debug("Date du catalogue : {}", catalog.getMetadata().getDate());
        } catch (IOException e) {
            logger.error("IOException during marshalling", e);
        }

    }
}
