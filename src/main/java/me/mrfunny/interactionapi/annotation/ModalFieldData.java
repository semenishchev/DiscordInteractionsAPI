package me.mrfunny.interactionapi.annotation;

import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ModalFieldData {
    String label();
    String id() default "";
    TextInputStyle style() default TextInputStyle.SHORT;
    int minLength() default 0;
    int maxLength() default 4000;
    boolean required() default true;
    String defaultValue() default "";
    String placeholder() default "";
}
