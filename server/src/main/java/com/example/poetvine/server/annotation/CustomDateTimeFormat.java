package com.example.poetvine.server.annotation;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
public @interface CustomDateTimeFormat {
}
