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
        Default defaultAnnotavion;
        Class defaultAnnotationValue = null;
        if (objects != null) {
            for (Object object : objects) {
                if (object == null) {
                    continue;
                }
                List<Field> objectFields = new ArrayList<>();
                Class objectClass;
                objectClass = object.getClass();
                while (objectClass != null) {
                    Field[] currentClassFields = objectClass.getDeclaredFields();
                    objectFields.addAll(Arrays.stream(currentClassFields).toList());
                    objectClass = objectClass.getSuperclass();
                }

                for (Field objectField : objectFields) {
                    if (object.getClass().isAnnotationPresent(Default.class)) {
                        //получаем аннотацию класса со значениями её полей по умолчанию
                        defaultAnnotavion = object.getClass().getAnnotation(Default.class);
                        defaultAnnotationValue = defaultAnnotavion.value();
                    }

                    Class fieldType = objectField.getType();
                    if (defaultAnnotationValue != null) {
                        Field[] defaultAnnotationValueFields = defaultAnnotationValue.getDeclaredFields();
                        for (Field defaultValueField : defaultAnnotationValueFields) {
                            //если тип поля совпадает с типом поля по умолчанию
                            if (fieldType.equals(defaultValueField.getType())) {
                                objectField.set(object, defaultValueField.get(defaultAnnotationValue.newInstance()));
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

}

