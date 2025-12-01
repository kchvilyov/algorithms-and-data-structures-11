package edu.t1;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Res {
    //суть метода в том, что он смотрит на классе объекта аннотацию
    // и если она есть - то скидывает значения всех полей до состояния
    //указанного в конфиг классе этой аннотации
    public void reset(Object... objects) throws Exception {
        if (objects == null) {
            return;
        }

        for (Object object : objects) {
            if (object == null) {
                continue;
            }

            Class<?> objectClass = object.getClass();
            Default defaultAnnotation = objectClass.getAnnotation(Default.class);
            if (defaultAnnotation == null) {
                continue; // Нет аннотации @Default — пропускаем
            }

            Class<?> configClass = defaultAnnotation.value();
            Object configInstance;
            try {
                configInstance = configClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Не удалось создать экземпляр класса конфигурации: " + configClass.getSimpleName(), e);
            }

            // Собираем все поля объекта, включая приватные и от суперклассов
            List<Field> objectFields = new ArrayList<>();
            Class<?> currentClass = objectClass;
            while (currentClass != null && currentClass != Object.class) {
                Field[] declaredFields = currentClass.getDeclaredFields();
                objectFields.addAll(Arrays.asList(declaredFields));
                currentClass = currentClass.getSuperclass();
            }

            // Для каждого поля объекта ищем совпадение по типу в Config
            for (Field objectField : objectFields) {
                if (java.lang.reflect.Modifier.isStatic(objectField.getModifiers()) ||
                    java.lang.reflect.Modifier.isFinal(objectField.getModifiers())) {
                    continue;
                }

                objectField.setAccessible(true);

                Class<?> fieldType = objectField.getType();

                // Ищем поле в Config с тем же типом
                boolean found = false;
                for (Field configField : configClass.getDeclaredFields()) {
                    if (configField.getType().equals(fieldType)) {
                        configField.setAccessible(true);
                        try {
                            Object defaultValue = configField.get(configInstance);
                            objectField.set(object, defaultValue);
                            found = true;
                            break; // Берём первое подходящее по типу
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("Нет доступа к полю: " + configField.getName(), e);
                        }
                    }
                }

                // Если не нашли — можно оставить как есть
            }
        }
    }
}