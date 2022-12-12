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
    public static <T>T toBuild(Class<T> clazz, Path path) throws NoSuchMethodException, IOException, IllegalAccessException, BuildException, InvocationTargetException, InstantiationException {
        T t = clazz.getDeclaredConstructor().newInstance();
        Properties properties = new Properties();
        FileReader fileReader = new FileReader(path.toFile());
        properties.load(fileReader);


        String stringProperty = "stringProperty";
        String numberProperty = "numberProperty";
        String timeProperty = "timeProperty";
        String stringPropertyValue = properties.getProperty(stringProperty);
        int numberPropertyValue = Integer.parseInt(properties.getProperty(numberProperty));
        fileReader.close();
        boolean hasAnnotation;
        String fieldName;
        String annotationFieldName;
        Type fieldType;

        Field[] fields = clazz.getDeclaredFields();
        for (Field field: fields) {
            field.setAccessible(true);
            fieldName = field.getName();
            fieldType = field.getType();
            hasAnnotation = field.getAnnotation(Property.class) != null;
            boolean typeIsLongOrInteger = fieldType.equals(int.class)||fieldType.equals(long.class);
            if(hasAnnotation){
                annotationFieldName = field.getAnnotation(Property.class).name();
                //если есть аннотация + её имя и тип поля норм, то стринг в студию
                if(fieldType.equals(String.class) && annotationFieldName != null && annotationFieldName.equals(stringProperty)){
                    field.set(t, stringPropertyValue);
                } else if (typeIsLongOrInteger && annotationFieldName != null && annotationFieldName.equals(numberProperty)) {
                    field.set(t, numberPropertyValue);
                } else if (fieldType.equals(LocalDateTime.class) && annotationFieldName != null && annotationFieldName.equals(timeProperty)) {
                    field.set(t, LocalDateTime.parse(properties.getProperty(timeProperty), DateTimeFormatter.ofPattern(field.getAnnotation(Property.class).format())));
                } else if (fieldType.equals(LocalDateTime.class) && fieldName.equals(timeProperty)) {
                    field.set(t, LocalDateTime.parse(properties.getProperty(timeProperty), DateTimeFormatter.ofPattern(field.getAnnotation(Property.class).format())));
                }
                else {
                    throw new BuildException(new Throwable().getCause());
                }
            }else {
                if(fieldType.equals(String.class) && fieldName.equals(stringProperty)){
                    field.set(t, stringPropertyValue);
                }else if (typeIsLongOrInteger && fieldName.equals(numberProperty)){
                    field.set(t, numberPropertyValue);
                }
                else{
                    throw new BuildException(new Throwable().getCause());
                }
            }

        }
        return t;
    }
}

