package com.proyecto.idat.common;

import java.lang.reflect.Field;

public class GetNotNullProperties {
    public static void copyNonNullProperties(Object source, Object target) {
        Field[] fields = source.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(source);
                if (value != null) {
                    field.set(target, value);
                }
            } catch (IllegalAccessException e) {
                // Manejar la excepción adecuadamente o ignorarla según tu caso de uso
                e.printStackTrace();
            }
        }
    }

}