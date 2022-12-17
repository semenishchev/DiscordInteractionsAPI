package me.mrfunny.interactionapi.annotation;

public @interface Group {
    String name();
    String description() default "No description provided.";
}
