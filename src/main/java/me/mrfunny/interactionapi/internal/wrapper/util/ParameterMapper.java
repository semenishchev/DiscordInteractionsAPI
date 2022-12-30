package me.mrfunny.interactionapi.internal.wrapper.util;

import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.lang.reflect.Method;

public class ParameterMapper {

    private final static String STRING_CLASS_NAME = "java.lang.String";
    private final static String ATTACHMENT_CLASS_NAME = "net.dv8tion.jda.api.entities.Message$Attachment";
    private final static String MENTIONABLE_CLASS_NAME = "net.dv8tion.jda.api.entities.IMentionable";
    private final static String MEMBER_CLASS_NAME = "net.dv8tion.jda.api.entities.Member";
    private final static String USER_CLASS_NAME = "net.dv8tion.jda.api.entities.User";
    private final static String ROLE_CLASS_NAME = "net.dv8tion.jda.api.entities.Role";
    private final static String CHANNEL_CLASS_NAME = "net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion";

    public static Method mapParameter(Class<?> parameterType) throws Exception {
        Class<OptionMapping> optionMappingClass = OptionMapping.class;
        return switch (parameterType.getName()) {
            case "byte", "short", "float" -> throw new IllegalArgumentException("byte, short and floats are not supported by JDA");
            case "long" -> optionMappingClass.getMethod("getAsLong");
            case "int" -> optionMappingClass.getMethod("getAsInt");
            case "double" -> optionMappingClass.getMethod("getAsDouble");
            case "boolean" -> optionMappingClass.getMethod("getAsBoolean");
            case ATTACHMENT_CLASS_NAME -> optionMappingClass.getMethod("getAsAttachment");
            case MENTIONABLE_CLASS_NAME -> optionMappingClass.getMethod("getAsMentionable");
            case MEMBER_CLASS_NAME -> optionMappingClass.getMethod("getAsMember");
            case USER_CLASS_NAME -> optionMappingClass.getMethod("getAsUser");
            case ROLE_CLASS_NAME -> optionMappingClass.getMethod("getAsRole");
            case CHANNEL_CLASS_NAME -> optionMappingClass.getMethod("getAsChannel");
            default -> optionMappingClass.getMethod("getAsString");
        };
    }

    public static OptionType mapParameterToType(Class<?> parameterType) {
        if(parameterType.isEnum()) {
            return OptionType.STRING;
        }
        return switch (parameterType.getName()) {
            case "byte", "short", "float" -> throw new IllegalArgumentException("byte, short and floats are not supported by JDA");
            case "long", "int" -> OptionType.INTEGER;
            case "double" -> OptionType.NUMBER;
            case "boolean" -> OptionType.BOOLEAN;
            case STRING_CLASS_NAME -> OptionType.STRING;
            case ATTACHMENT_CLASS_NAME -> OptionType.ATTACHMENT;
            case MENTIONABLE_CLASS_NAME -> OptionType.MENTIONABLE;
            case MEMBER_CLASS_NAME, USER_CLASS_NAME -> OptionType.USER;
            case ROLE_CLASS_NAME -> OptionType.ROLE;
            case CHANNEL_CLASS_NAME -> OptionType.CHANNEL;
            default -> OptionType.UNKNOWN;
        };
    }

    public static Object mapTypeToNull(Class<?> parameterType) {
        return switch (parameterType.getName()) {
            case "long", "int" -> 0;
            case "double" -> 0.0;
            case "boolean" -> false;
            default -> null;
        };
    }
}
