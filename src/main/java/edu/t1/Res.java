package edu.t1;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

public class Res {
    public static <T> T setDefaultValues(T o, Class<?> def_class_value, Map<?, ?> f_def) {
        if (o == null) {
            return o;
        }

        Class<?> clazz = o.getClass();
        Annotation[] annotations = clazz.getAnnotations();
        Class<?> configClass = null;

        for (Annotation ann : annotations) {
            if (ann instanceof Default) {
                configClass = ((Default) ann).value();
                break;
            }
        }

        if (configClass == null) {
            return o;
        }

        try {
            Object configInstance = configClass.getDeclaredConstructor().newInstance();

            for (Field field : clazz.getDeclaredFields()) {
                int modifiers = field.getModifiers();
                if (java.lang.reflect.Modifier.isStatic(modifiers) ||
                        java.lang.reflect.Modifier.isFinal(modifiers)) {
                    continue;
                }

                field.setAccessible(true);

                try {
                    Field configField = configClass.getDeclaredField(field.getName());
                    configField.setAccessible(true);
                    Object defaultValue = configField.get(configInstance);
                    field.set(o, defaultValue);
                } catch (NoSuchFieldException e) {
                    // Поле отсутствует в Config — ничего не делаем
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при установке значений по умолчания", e);
        }

        return o;
    }
}