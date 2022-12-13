package com.task2;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class UtilClass {
    private static final String STRING_PROPERTY = "stringProperty";
    private static final String NUMBER_PROPERTY = "numberProperty";
    private static final String TIME_PROPERTY = "timeProperty";

    public static <T> T toBuild(Class<T> clazz, Path path) throws NoSuchMethodException, IOException, IllegalAccessException, BuildException, InvocationTargetException, InstantiationException {
        T t = clazz.getDeclaredConstructor().newInstance();
        Properties properties = new Properties();
        FileReader fileReader = new FileReader(path.toFile());
        properties.load(fileReader);
        fileReader.close();
        String stringPropertyValue = properties.getProperty(STRING_PROPERTY);
        int numberPropertyValue = Integer.parseInt(properties.getProperty(NUMBER_PROPERTY));

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            switch (checkField(field)) {
                case INCORRECT_FIELD -> throw new BuildException(new Throwable().getCause());
                case FIELD_STRING_HAS_SUITABLE_NAME -> field.set(t, stringPropertyValue);
                case FIELD_NUMBER_HAS_SUITABLE_NAME -> field.set(t, numberPropertyValue);
                case FIELD_STRING_HAS_SUITABLE_ANNOTATION -> field.set(t, stringPropertyValue);
                case FIELD_NUMBER_HAS_SUITABLE_ANNOTATION -> field.set(t, numberPropertyValue);
                case FIELD_TIME_HAS_SUITABLE_NAME_OR_SUITABLE_ANNOTATION ->
                        field.set(t, LocalDateTime.parse(properties.getProperty(TIME_PROPERTY),
                                DateTimeFormatter.ofPattern(field.getAnnotation(Property.class).format())));

            }

        }
        return t;
    }

    private static FieldMatching checkField(Field field) {
        Type fieldType = field.getType();
        String fieldName = field.getName();
        boolean isNumberType = fieldType.equals(int.class) || fieldType.equals(long.class);
        if (field.getAnnotation(Property.class) == null) {
            if (fieldType.equals(String.class) && fieldName.equals(STRING_PROPERTY)) {
                return FieldMatching.FIELD_STRING_HAS_SUITABLE_NAME;
            } else if (isNumberType && fieldName.equals(NUMBER_PROPERTY)) {
                return FieldMatching.FIELD_NUMBER_HAS_SUITABLE_NAME;
            } else {
                return FieldMatching.INCORRECT_FIELD;
            }
        } else if (field.getAnnotation(Property.class) != null) {
            if (fieldType.equals(String.class) && field.getAnnotation(Property.class).name().equals(STRING_PROPERTY)) {
                return FieldMatching.FIELD_STRING_HAS_SUITABLE_ANNOTATION;
            } else if (isNumberType && field.getAnnotation(Property.class).name().equals(NUMBER_PROPERTY)) {
                return FieldMatching.FIELD_NUMBER_HAS_SUITABLE_ANNOTATION;
            } else if (fieldType.equals(LocalDateTime.class) && (fieldName.equals(TIME_PROPERTY) || field.getAnnotation(Property.class).name().equals("timeProperty"))) {
                return FieldMatching.FIELD_TIME_HAS_SUITABLE_NAME_OR_SUITABLE_ANNOTATION;
            } else {
                return FieldMatching.INCORRECT_FIELD;
            }
        }
        return FieldMatching.INCORRECT_FIELD;
    }
}

