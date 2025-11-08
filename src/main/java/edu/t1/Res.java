package edu.t1;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Res {
    //суть метода в том, что он смотрит на классе объекта аннотацию
    // и если она есть - то скидывает значения всех полей до состояния
    //указанного в конфиг классе этой аннотации
    public void reset(Object... obj) throws Exception {
        Default def;
        Class def_class_value = null;
        if (obj != null) {
            for (Object o : obj) {
                List<Field> fields =new ArrayList<>();
                Class clz;
                clz = o.getClass();
                while (clz != null) {
                    Field[] fields2 = clz.getDeclaredFields();
                    fields.addAll(Arrays.stream(fields2).toList());
                    clz = clz.getSuperclass();
                }

                for (Field f : fields) {
                    if ( o.getClass().isAnnotationPresent(Default.class)) {
                        def = o.getClass().getAnnotation(Default.class);
                        def_class_value = def.value();
                    }

                    Class<?> fieldType = f.getType();
                    if (def_class_value != null) {
                        Field[] defValueFields = def_class_value.getDeclaredFields();
                        for (Field f_def : defValueFields) {
                            if (fieldType.equals(f_def.getType())) {
                                f.set(o, f_def.get(def_class_value.newInstance()));
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

}

