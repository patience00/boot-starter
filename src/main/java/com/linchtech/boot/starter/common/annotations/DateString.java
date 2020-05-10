package com.linchtech.boot.starter.common.annotations;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@JsonSerialize(using = DateStringSerializer.class)
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@Documented
public @interface DateString {
}
