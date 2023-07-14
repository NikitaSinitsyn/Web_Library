package com.example.https.AppConfig;

import com.example.https.Entity.Position;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class PositionSerializer extends JsonSerializer<Position> {



    @Override
    public void serialize(Position position, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", position.getId());
        jsonGenerator.writeStringField("name", position.getName());
        jsonGenerator.writeEndObject();
    }
}