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

        configClass = getConfigClass(annotations, configClass);

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
                setDefaultValueToField(o, field, configClass, configInstance);
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при установке значений по умолчания", e);
        }

        return o;
    }

    private static <T> void setDefaultValueToField(T o, Field field, Class<?> configClass, Object configInstance) throws NoSuchFieldException, IllegalAccessException {
        field.setAccessible(true);
        try {
        // Получаем значение по умолчанию из аннотации
            Field configField = configClass.getDeclaredField(field.getName());
            configField.setAccessible(true);
            // Устанавливаем значение по умолчанию в поле объекта
            Object defaultValue = configField.get(configInstance);
            field.set(o, defaultValue);
        } catch (NoSuchFieldException e) {
            // Поле отсутствует в Config — ничего не делаем
        }
    }

    private static Class<?> getConfigClass(Annotation[] annotations, Class<?> configClass) {
        for (Annotation ann : annotations) {
            if (ann instanceof Default) {
                configClass = ((Default) ann).value();
                break;
            }
        }
        return configClass;
    }
}