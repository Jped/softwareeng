package edu.cooper.ee.ece366.events.util;

import com.google.gson.*;
import spark.ResponseTransformer;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

class LocalDateTimeSerializer implements JsonSerializer <LocalDateTime> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public JsonElement serialize(LocalDateTime localDateTime, Type srcType, JsonSerializationContext context) {
        return new JsonPrimitive(formatter.format(localDateTime));
    }
}

public class JsonTransformer implements ResponseTransformer {

    private final Gson gson =
            new GsonBuilder()
                    .enableComplexMapKeySerialization()
                    .setPrettyPrinting()
                    .excludeFieldsWithoutExposeAnnotation()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
                    //.registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                     //   @Override
                     //   public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                     //       Instant instant = Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong());
                    //        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                    //    }
                    //})
                    .create();

    @Override
    public String render(final Object model) throws Exception {
        return gson.toJson(model);
    }
}
