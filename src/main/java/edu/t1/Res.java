package edu.t1;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Res {
    /**
     * Метод сброса значений полей объекта до значений по умолчанию.
     * @Default это пояснение, которое указывает класс настройки по умолчанию.
     //суть метода в том, что он смотрит на классе объекта аннотацию
     // и если она есть - то скидывает значения всех полей до состояния
     //указанного в конфиг классе этой аннотации
     * @param objects - перечень(список) объектов для сброса значений
     * @throws RuntimeException - если не удалось создать экземпляр класса настройки по умолчанию
     */
    public void reset(Object... objects) throws RuntimeException {
        if (objects == null) {
            //Не обрабатываем пустой список объектов
            return;
        }
        // Для каждого объекта из переданного списка
        for (Object object : objects) {
            if (object == null) {
                //Не обрабатываем пустые объекты
                continue;
            }
            Class<?> objectClass = object.getClass();
            Default defaultAnnotation = objectClass.getAnnotation(Default.class);
            if (defaultAnnotation == null) {
                //Не обрабатываем объект, у которого нет пояснения по умолчанию @Default
                continue;
            }
            // Готовим настройки объекта по умолчанию
            Class<?> configClass = defaultAnnotation.value();
            Object defaultConfigInstance = getDefaultConfigInstance(configClass);

            // Получаем все поля объекта
            List<Field> objectFields = getAllFields(objectClass);

            // Для каждого поля объекта обновляем его значение из настроек по умолчанию
            for (Field objectField : objectFields) {
                updateFieldByDefaultTypeValue(object, objectField, configClass, defaultConfigInstance);
            }
        }
    }

    /**
     * Создаем экземпляр класса конфигурации по умолчанию
     * @param configClass - класс конфигурации по умолчанию
     * @return экземпляр класса конфигурации по умолчанию
     */
    private static Object getDefaultConfigInstance(Class<?> configClass) {
        Object defaultConfigInstance;
        try {
            defaultConfigInstance = configClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Не удалось создать экземпляр класса конфигурации: " + configClass.getSimpleName(), e);
        }
        return defaultConfigInstance;
    }

    /**
     * Обновляем поле объекта по его типу из настройки по умолчанию
     * @param object - объект обновляемый
     * @param objectField - поле объекта
     * @param configClass - класс настроек
     * @param configInstance - экземпляр настроек
     */
    private static void updateFieldByDefaultTypeValue(Object object, Field objectField, Class<?> configClass, Object configInstance) {
        if (java.lang.reflect.Modifier.isStatic(objectField.getModifiers()) ||
            java.lang.reflect.Modifier.isFinal(objectField.getModifiers())) {
            // Пропускаем статические и final поля
            return;
        }

        // Для каждого поля объекта ищем совпадение по типу в Config
        objectField.setAccessible(true);

        Class<?> fieldType = objectField.getType();

        // Ищем поле в Config с тем же типом
        for (Field configField : configClass.getDeclaredFields()) {
            // Если тип совпадает, то копируем значение
            if (configField.getType().equals(fieldType)) {
                configField.setAccessible(true);
                try {
                    Object defaultValue = configField.get(configInstance);
                    objectField.set(object, defaultValue);
                    break; // Обновляем только первое подходящее по типу поле
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Нет доступа к полю: " + configField.getName(), e);
                }
            }
        }
        // Если не нашли — оставляем как есть
    }

    /**
     * Получаем все поля объекта и его предков, включая частные
     * @return список полей
     */
    private static List<Field> getAllFields(Class<?> objectClass) {
        List<Field> objectFields = new ArrayList<>();
        Class<?> currentClass = objectClass;
        while (currentClass != null && currentClass != Object.class) {
            Field[] declaredFields = currentClass.getDeclaredFields();
            objectFields.addAll(Arrays.asList(declaredFields));
            currentClass = currentClass.getSuperclass();
        }
        return objectFields;
    }
}