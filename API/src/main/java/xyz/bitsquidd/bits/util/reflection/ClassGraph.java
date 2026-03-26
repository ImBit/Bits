/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.util.reflection;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Utility class for common reflection operations and classpath scanning.
 * Provides caching for methods, fields, and constructors to improve performance,
 * as well as helper methods for primitive type conversions and package scanning.
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public final class ClassGraph {
    private ClassGraph() {}

    private static Function<? super String, ? extends io.github.classgraph.ClassGraph> CLASSGRAPH_SUPPLIER = packageName -> new io.github.classgraph.ClassGraph()
      .enableClassInfo()
      .enableAnnotationInfo()
      .acceptPackages(packageName);

    public static void setSupplier(Function<? super String, ? extends io.github.classgraph.ClassGraph> supplier) {
        if (supplier == null) throw new IllegalArgumentException("Supplier cannot be null");
        CLASSGRAPH_SUPPLIER = supplier;
    }


    /**
     * Helper class for converting between Java primitive types and their corresponding wrapper classes.
     */
    public static final class Primitive {
        private Primitive() {}

        public static final HashBiMap<Class<?>, Class<?>> WRAPPER_TO_PRIMITIVE = HashBiMap.create(Map.of(
          Boolean.class, boolean.class,
          Byte.class, byte.class,
          Character.class, char.class,
          Short.class, short.class,
          Integer.class, int.class,
          Long.class, long.class,
          Float.class, float.class,
          Double.class, double.class
        ));
        public static final BiMap<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER = WRAPPER_TO_PRIMITIVE.inverse();

        public static Class<?> to(Class<?> wrapper) {
            Class<?> primitive = WRAPPER_TO_PRIMITIVE.get(wrapper);
            if (primitive == null) throw new IllegalArgumentException("Provided class is not a wrapper: " + wrapper);
            return primitive;
        }

        public static Class<?> toOrSelf(Class<?> clazz) {
            return WRAPPER_TO_PRIMITIVE.getOrDefault(clazz, clazz);
        }

        public static Class<?> from(Class<?> primitive) {
            Class<?> wrapper = PRIMITIVE_TO_WRAPPER.get(primitive);
            if (wrapper == null) throw new IllegalArgumentException("No wrapper for primitive: " + primitive);
            return wrapper;
        }

        public static Class<?> fromOrSelf(Class<?> clazz) {
            return PRIMITIVE_TO_WRAPPER.getOrDefault(clazz, clazz);
        }

    }

    /**
     * Helper class for invoking methods via reflection.
     */
    public static final class Method {
        private Method() {}

        private static final Map<MethodKey, java.lang.reflect.Method> INNER_METHOD_CACHE = new ConcurrentHashMap<>();

        private record MethodKey(
          Class<?> clazz,
          String methodName,
          Class<?>[] parameterTypes
        ) {
            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                if (!(obj instanceof MethodKey(Class<?> clazz1, String name, Class<?>[] types))) return false;
                return clazz.equals(clazz1) && methodName.equals(name) && Arrays.equals(parameterTypes, types);
            }

            @Override
            public int hashCode() {
                return Objects.hash(clazz, methodName, Arrays.hashCode(parameterTypes));
            }

        }

        private static java.lang.reflect.Method cacheMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes) {
            if (clazz == null || methodName == null || methodName.isEmpty()) throw new IllegalArgumentException("Class and methodName must be non-null and non-empty");

            MethodKey key = new MethodKey(clazz, methodName, parameterTypes);

            return INNER_METHOD_CACHE.computeIfAbsent(
              key, k -> {
                  Class<?> search = clazz;
                  while (search != null) {
                      try {
                          java.lang.reflect.Method m = search.getDeclaredMethod(methodName, parameterTypes);
                          m.setAccessible(true);
                          return m;
                      } catch (NoSuchMethodException ignored) {
                          search = search.getSuperclass();
                      }
                  }

                  throw new ReflectionException("Method not found: " + methodName + " in " + clazz.getName());
              }
            );
        }

        @SuppressWarnings("unchecked")
        public static <T> T invoke(Object object, String methodName, Class<?>[] paramTypes, Class<T> returnType, Object... args) {
            if (object == null) throw new IllegalArgumentException("Object cannot be null");

            try {
                return (T)cacheMethod(object.getClass(), methodName, paramTypes).invoke(object, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new ReflectionException("Unable to invoke method: " + methodName, e);
            }
        }

        @SuppressWarnings("unchecked")
        public static <T> T invoke(Class<?> clazz, String methodName, Class<?>[] paramTypes, Class<T> returnType, Object... args) {
            if (clazz == null) throw new IllegalArgumentException("class cannot be null");

            try {
                return (T)cacheMethod(clazz, methodName, paramTypes).invoke(null, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new ReflectionException("Unable to invoke static method: " + methodName, e);
            }
        }

    }

    /**
     * Helper class for accessing and modifying fields via reflection.
     */
    public static final class Value {
        private Value() {}

        private static final Map<FieldKey, Field> INNER_FIELD_CACHE = new ConcurrentHashMap<>();

        private record FieldKey(
          Class<?> clazz,
          String fieldName
        ) {

            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                if (!(obj instanceof FieldKey(Class<?> clazz1, String name))) return false;
                return clazz.equals(clazz1) && fieldName.equals(name);
            }

        }

        private static Field cacheField(Class<?> clazz, String fieldName) {
            if (clazz == null || fieldName == null || fieldName.isEmpty()) throw new IllegalArgumentException("Class and fieldName must be non-null and non-empty");

            FieldKey key = new FieldKey(clazz, fieldName);

            return INNER_FIELD_CACHE.computeIfAbsent(
              key, k -> {
                  Class<?> search = clazz;
                  while (search != null) {
                      try {
                          Field f = search.getDeclaredField(fieldName);
                          f.setAccessible(true);
                          return f;
                      } catch (NoSuchFieldException ignored) {
                          search = search.getSuperclass();
                      }
                  }

                  throw new ReflectionException("Field not found: " + fieldName + " in " + clazz.getName());
              }
            );
        }

        private static Field cacheField(Object object, String fieldName) {
            if (object == null) throw new IllegalArgumentException("Object must be non-null");
            return cacheField(object.getClass(), fieldName);
        }

        @SuppressWarnings("unchecked")
        public static <T> T get(Object object, String fieldName, Class<T> fieldType) {
            try {
                return (T)cacheField(object, fieldName).get(object);
            } catch (IllegalAccessException e) {
                throw new ReflectionException("Unable to access field: " + fieldName, e);
            }
        }

        @SuppressWarnings("unchecked")
        public static <T> T getStatic(Class<?> clazz, String fieldName, Class<T> fieldType) {
            try {
                return (T)cacheField(clazz, fieldName).get(null);
            } catch (IllegalAccessException e) {
                throw new ReflectionException("Unable to access static field: " + fieldName, e);
            }
        }

        public static <T> void set(Object object, String fieldName, T value) {
            try {
                cacheField(object, fieldName).set(object, value);
            } catch (IllegalAccessException e) {
                throw new ReflectionException("Unable to access field: " + fieldName, e);
            }
        }

        public static <T> void setStatic(Class<?> clazz, String fieldName, T value) {
            try {
                cacheField(clazz, fieldName).set(null, value);
            } catch (IllegalAccessException e) {
                throw new ReflectionException("Unable to access static field: " + fieldName, e);
            }
        }

    }

    /**
     * Helper class for instantiating objects via reflection.
     */
    public static final class Instance {
        private Instance() {}

        private static final Map<ConstructorKey, Constructor<?>> CONSTRUCTOR_CACHE = new ConcurrentHashMap<>();

        private record ConstructorKey(
          Class<?> clazz,
          Class<?>[] parameterTypes
        ) {
            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                if (!(obj instanceof ConstructorKey(Class<?> clazz1, Class<?>[] types))) return false;
                return clazz.equals(clazz1) && Arrays.equals(parameterTypes, types);
            }

            @Override
            public int hashCode() {
                return Objects.hash(clazz, Arrays.hashCode(parameterTypes));
            }

        }

        private static Class<?>[] getParameterTypes(Object... args) {
            Class<?>[] paramTypes = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                paramTypes[i] = Primitive.fromOrSelf(args[i].getClass());
            }
            return paramTypes;
        }

        @SuppressWarnings("unchecked")
        private static <T> Constructor<T> cacheConstructor(Class<T> clazz, Class<?>[] parameterTypes) throws ReflectionException {
            if (clazz == null) throw new IllegalArgumentException("Class must be non-null");

            ConstructorKey key = new ConstructorKey(clazz, parameterTypes);

            return (Constructor<T>)CONSTRUCTOR_CACHE.computeIfAbsent(
              key, k -> {
                  try {
                      Constructor<T> constructor = clazz.getDeclaredConstructor(parameterTypes);
                      constructor.setAccessible(true);
                      return constructor;
                  } catch (NoSuchMethodException e) {
                      throw new ReflectionException("Constructor not found: " + clazz.getName() + " with parameters: " + Arrays.toString(parameterTypes), e);
                  }
              }
            );
        }

        public static <T> T create(Class<T> clazz, Object... args) throws ReflectionException {
            try {
                Class<?>[] paramTypes = getParameterTypes(args);
                Constructor<T> constructor = cacheConstructor(clazz, paramTypes);
                return constructor.newInstance(args);
            } catch (InvocationTargetException e) {
                Throwable cause = e.getCause();
                throw new ReflectionException("Constructor failed for " + clazz.getName(), cause != null ? cause : e);
            } catch (Exception e) {
                throw new ReflectionException("Failed to create instance of " + clazz.getName(), e);
            }
        }

        public static <T> Supplier<T> supplier(Class<T> clazz, Object... args) throws ReflectionException {
            Class<?>[] paramTypes = getParameterTypes(args);
            Constructor<T> constructor = cacheConstructor(clazz, paramTypes);

            return () -> {
                try {
                    return constructor.newInstance(args);
                } catch (Exception e) {
                    throw new ReflectionException("Reflection Failed.");
                }
            };
        }

    }

    /**
     * Helper class for scanning the classpath using the io.github.classgraph library.
     */
    public static final class Scanner {
        private Scanner() {}

        public record ScannerFlags(
          boolean includeAbstract,
          boolean includeDeprecated,
          boolean includeInnerClasses
        ) {
            public static final ScannerFlags DEFAULT = new ScannerFlags(false, false, false);

            public ScannerFlags withAbstract() {
                return new ScannerFlags(true, includeDeprecated, includeInnerClasses);
            }

            public ScannerFlags withDeprecated() {
                return new ScannerFlags(includeAbstract, true, includeInnerClasses);
            }

            public ScannerFlags withInnerClasses() {
                return new ScannerFlags(includeAbstract, includeDeprecated, true);
            }

        }

        public static <T> List<Class<? extends T>> getClasses(String packageName, Class<? extends T> baseClass) {
            return getClasses(packageName, baseClass, ScannerFlags.DEFAULT);
        }

        public static <T> List<Class<? extends T>> getClasses(String packageName, Class<? extends T> baseClass, ScannerFlags flags) throws ReflectionException {
            List<Class<? extends T>> classes = new ArrayList<>();

            try (ScanResult scanResult = CLASSGRAPH_SUPPLIER.apply(packageName).enableClassInfo().scan()) {
                ClassInfoList classInfoList;
                if (baseClass.isInterface()) {
                    classInfoList = scanResult.getClassesImplementing(baseClass.getName());
                } else {
                    classInfoList = scanResult.getSubclasses(baseClass.getName());
                }

                for (io.github.classgraph.ClassInfo classInfo : classInfoList) {
                    if (!flags.includeAbstract && (classInfo.isAbstract() || classInfo.isInterface())) continue;
                    if (!flags.includeDeprecated && classInfo.hasAnnotation(Deprecated.class.getName())) continue;
                    if (!flags.includeInnerClasses && classInfo.isInnerClass()) continue;
                    classes.add(classInfo.loadClass(baseClass));
                }
            } catch (Exception e) {
                throw new ReflectionException("Failed to scan package: " + packageName, e);
            }

            return classes;
        }

        public static List<Class<?>> getAnnotatedClasses(String packageName, Class<? extends Annotation> annotation) {
            List<Class<?>> classes = new ArrayList<>();
            try (ScanResult scanResult = CLASSGRAPH_SUPPLIER.apply(packageName).enableAnnotationInfo().scan()) {
                ClassInfoList classInfoList = scanResult.getClassesWithAnnotation(annotation.getName());
                for (io.github.classgraph.ClassInfo classInfo : classInfoList) {
                    classes.add(classInfo.loadClass());
                }
            }
            return classes;
        }

    }

}
