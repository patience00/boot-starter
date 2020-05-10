package com.linchtech.boot.starter.common.annotations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 107
 * @create 2018-08-07 15:17
 * @desc
 **/
public class DateStringSerializer extends JsonSerializer<Integer> {
    @Override
    public void serialize(Integer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(value);
        Date data = new Date(value * 1000L);
        gen.writeStringField("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(data));
    }
}
