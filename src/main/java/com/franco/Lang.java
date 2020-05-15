package com.franco;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * javaLang工具类
 *
 * @author franco
 */
public class Lang {

    private static final Map<Class<?>, Object> DEFAULT_VALUE = new HashMap<Class<?>, Object>();

    static {
        DEFAULT_VALUE.put(int.class, 0);
        DEFAULT_VALUE.put(short.class, 0);
        DEFAULT_VALUE.put(long.class, 0);
        DEFAULT_VALUE.put(boolean.class, false);
        DEFAULT_VALUE.put(float.class, 0);
        DEFAULT_VALUE.put(double.class, 0);
        DEFAULT_VALUE.put(char.class, 0);
        DEFAULT_VALUE.put(byte.class, 0);
    }

    public static <A extends Annotation> A getAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        A annotation = null;
        while(clazz != null) {
            annotation = (A) clazz.getAnnotation(annotationClass);
            if (annotation != null) {
                return annotation;
            }
            clazz = clazz.getSuperclass();
        }
        return annotation;
    }

    public static <A extends Annotation> A getAnnotation(Class<?> clazz, Method type, Class<? extends Annotation> annotationClass) {
        A annotation = null;
        while(clazz != null) {
            for(Method method : clazz.getMethods()) {
                if(!method.getName().equalsIgnoreCase(type.getName())) {
                    continue;
                }
                annotation = (A) method.getAnnotation(annotationClass);
                if(annotation != null) {
                    return annotation;
                }
            }
            clazz = clazz.getSuperclass();
        }
        return annotation;
    }

    public static Object getDefaultValue(Class<?> clazz) {
        if(clazz.isPrimitive()) {
            return DEFAULT_VALUE.get(clazz);
        }
        return null;
    }

    public static <T> T castTo(Object src, Class<T> tClass) {
        if (null == src) {
            return (T) Lang.getDefaultValue(tClass);
        }
        return castTo(src, src.getClass(), tClass);
    }

    public static <F, T> T castTo(Object src, Class<F> fClass, Class<T> tClass) {
        if(fClass.getName().equals(tClass.getName())) {
            return (T) src;
        } else if(tClass.isAssignableFrom(fClass)) {
            return (T) src;
        }

        if(String.class == fClass) {
            return (T) parseString2Object((String) src, tClass);
        }
        if(fClass.isArray() && !tClass.isArray()) {
            return castTo(Array.get(src, 0), tClass);
        }
        if(fClass.isArray() && tClass.isArray()) {
            int len = Array.getLength(src);
            Object result = Array.newInstance(tClass, len);
            for(int i = 0; i < len; i++) {
                Array.set(result, i, castTo(src, tClass));
            }
            return (T) result;
        }
        return (T) getDefaultValue(tClass);
    }

    public static <T> Object parseString2Object(String src, Class<T> tClass) {
        try {
            if(isBoolean(tClass)) {
                return Boolean.valueOf(src);
            }
            if(isInteger(tClass)) {
                return Integer.valueOf(src);
            }
            if(isLong(tClass)) {
                return Long.valueOf(src);
            }
            if(isShort(tClass)) {
                return Short.valueOf(src);
            }
            if(isDouble(tClass)) {
                return Double.valueOf(src);
            }
            if(isFloat(tClass)) {
                return Float.valueOf(src);
            }
            if(isString(tClass)) {
                return src;
            }
            if(isStringLike(tClass)) {
                return ((CharSequence) src).toString();
            }
        } catch (Throwable t) {

        }
        return getDefaultValue(tClass);
    }

    public static boolean isBoolean(Class<?> clazz) {
        return is(clazz, boolean.class) || is(clazz, Boolean.class);
    }

    public static boolean isInteger(Class<?> clazz) {
        return is(clazz, int.class) || is(clazz, Integer.class);
    }

    public static boolean isLong(Class<?> clazz) {
        return is(clazz, long.class) || is(clazz, Long.class);
    }

    public static boolean isShort(Class<?> clazz) {
        return is(clazz, short.class) || is(clazz, Short.class);
    }

    public static boolean isByte(Class<?> clazz) {
        return is(clazz, byte.class) || is(clazz, Byte.class);
    }

    public static boolean isDouble(Class<?> clazz) {
        return is(clazz, double.class) || is(clazz, Double.class);
    }

    public static boolean isFloat(Class<?> clazz) {
        return is(clazz, float.class) || is(clazz, Float.class);
    }

    public static boolean isString(Class<?> clazz) {
        return is(clazz, String.class);
    }

    public static boolean isStringLike(Class<?> clazz) {
        return CharSequence.class.isAssignableFrom(clazz);
    }

    public static boolean is(Class<?> clazz1, Class<?> clazz2) {
        return clazz1 == clazz2;
    }
}
