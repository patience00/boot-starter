package com.linchtech.boot.starter.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * BeanUtils扩展类，支持递归深度拷贝
 */
public final class BeanUtilsExt extends BeanUtils {
    private BeanUtilsExt() {

    }

    /**
     * Bean深度拷贝
     *
     * @param source    源对象
     * @param targetCls 目标对象类
     * @param <S>       泛型
     * @param <T>       泛型
     * @return 泛型
     * @throws BeansException 拷贝失败
     */
    public static <S, T> T deepCopy(S source, Class<T> targetCls) throws BeansException {
        return deepCopy(source, targetCls, null, (String[]) null);
    }

    /**
     * Bean深度拷贝
     *
     * @param source           源对象
     * @param targetCls        目标对象类
     * @param ignoreProperties 忽略属性数组
     * @param <S>              泛型
     * @param <T>              泛型
     * @return 泛型
     * @throws BeansException 如果拷贝失败
     */
    public static <S, T> T deepCopy(S source, Class<T> targetCls, String... ignoreProperties) throws BeansException {
        return deepCopy(source, targetCls, null, ignoreProperties);
    }

    /**
     * Bean深度拷贝
     *
     * @param source           源对象
     * @param targetCls        目标对象类
     * @param editable         设置限制属性的类（或接口）
     * @param ignoreProperties 忽略属性数组
     * @param <S>              泛型
     * @param <T>              泛型
     * @return 泛型
     * @throws BeansException 如果拷贝失败
     */
    public static <S, T> T deepCopy(S source, Class<T> targetCls, Class<?> editable,
                                    String... ignoreProperties) throws BeansException {
        if (source == null) {
            return null;
        }
        try {
            T target = targetCls.newInstance();
            copyProperties(source, target, editable, ignoreProperties);
            return target;
        } catch (Exception e) {
            throw new RuntimeException("BEAN_COPY_SYSTEM_EXCEPTION", e);
        }
    }

    /**
     * 深度拷贝List
     *
     * @param source    源对象
     * @param targetCls 目标对象类
     * @param <S>       泛型
     * @param <T>       泛型
     * @return 泛型
     * @throws BeansException 如果拷贝失败
     */
    public static <S, T> List<T> deepCopyList(List<S> source, Class<T> targetCls) throws BeansException {
        return deepCopyList(source, targetCls, null, (String[]) null);
    }

    /**
     * 深度拷贝List
     *
     * @param source           源对象
     * @param targetCls        目标对象类
     * @param ignoreProperties 忽略属性数组
     * @param <S>              泛型
     * @param <T>              泛型
     * @return 泛型
     * @throws BeansException 如果拷贝失败
     */
    public static <S, T> List<T> deepCopyList(List<S> source, Class<T> targetCls,
                                              String... ignoreProperties) throws BeansException {
        return deepCopyList(source, targetCls, null, ignoreProperties);
    }

    /**
     * 深度拷贝List
     *
     * @param source           源对象
     * @param targetCls        目标对象类
     * @param editable         设置限制属性的类（或接口）
     * @param ignoreProperties 忽略属性数组
     * @param <S>              泛型
     * @param <T>              泛型
     * @return 泛型
     * @throws BeansException 如果拷贝失败
     */
    public static <S, T> List<T> deepCopyList(List<S> source, Class<T> targetCls, Class<?> editable,
                                              String... ignoreProperties) throws BeansException {
        if (source == null || source.isEmpty()) {
            return new ArrayList<>();
        }
        return source.stream().map(s -> deepCopy(s, targetCls, editable, ignoreProperties)).collect(Collectors.toList());
    }

    /**
     * 将给定源bean的属性值赋值到目标bean中
     *
     * @param source           源对象
     * @param target           目标对象
     * @param editable         设置限制属性的类（或接口）
     * @param ignoreProperties 忽略属性数组
     * @throws BeansException 如果拷贝失败
     */
    private static void copyProperties(Object source, Object target, @Nullable Class<?> editable,
                                       @Nullable String... ignoreProperties) throws BeansException {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        Class<?> actualEditable = target.getClass();
        if (editable != null) {
            if (!editable.isInstance(target)) {
                throw new IllegalArgumentException("Target class [" + target.getClass().getName()
                        + "] not assignable to Editable class [" + editable.getName() + "]");
            }
            actualEditable = editable;
        }
        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
        List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);

        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod == null || (ignoreList != null && ignoreList.contains(targetPd.getName()))) {
                continue;
            }
            PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
            if (sourcePd == null) {
                continue;
            }
            Method readMethod = sourcePd.getReadMethod();
            if (readMethod == null) {
                continue;
            }
            try {
                if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                    readMethod.setAccessible(true);
                }
                Object value = readMethod.invoke(source);
                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                    writeMethod.setAccessible(true);
                }
                // 判断源(source)与目标(target)类型是否匹配
                Class<?> sourceType = readMethod.getReturnType();
                Class<?> targetType = writeMethod.getParameterTypes()[0];
                if (ClassUtils.isAssignable(targetType, sourceType)) {
                    if (value instanceof Collection) {
                        // 针对Field的类型为Collection类型的处理
                        Type writeType = writeMethod.getGenericParameterTypes()[0];
                        Type readType = readMethod.getGenericReturnType();
                        Type writeActualType = ((ParameterizedType) writeType).getActualTypeArguments()[0];
                        Type readActualType = ((ParameterizedType) readType).getActualTypeArguments()[0];
                        if (!canCopy((Class<?>) writeActualType, (Class<?>) readActualType)) {
                            // 如果不能拷贝则跳过
                            continue;
                        }
                        // 当 source 中 Collection类型的Field的泛型和target中的Field的泛型类型不一致的时候 需要进行深度拷贝
                        Collection sourceList = (Collection) value;
                        // 这里只是简单的调用反射来实例化，因此原集合类型必须要有无参构造，比如Arrays.asList创建的集合则无法拷贝，会抛出异常
                        Collection actualValue = (Collection) value.getClass().newInstance();
                        actualValue.clear();
                        String prefix = targetPd.getName() + ".";
                        String[] subFilter = ignoreProperties;
                        if (!CollectionUtils.isEmpty(ignoreList)) {
                            subFilter = ignoreList.stream().filter(ignore -> ignore.startsWith(prefix))
                                    .map(ignore -> ignore.substring(prefix.length())).toArray(String[]::new);
                        }
                        for (Object subSource : sourceList) {
                            Object subTarget;
                            if (writeActualType != readActualType && isJavaBean((Class<?>) writeActualType) && isJavaBean((Class<?>) readActualType)) {
                                subTarget = ((Class) writeActualType).newInstance();
                                copyProperties(subSource, subTarget, editable, subFilter);
                            } else {
                                subTarget = subSource;
                            }
                            actualValue.add(subTarget);
                        }
                        value = actualValue;
                    } else if (value instanceof Map) {
                        // 针对Field的类型为Map类型的处理
                        Map sourceMap = (Map) value;
                        if (sourceMap == null || sourceMap.size() < 1) {
                            continue;
                        }
                        Type writeType = writeMethod.getGenericParameterTypes()[0];
                        Type readType = readMethod.getGenericReturnType();
                        Type writeKeyType = ((ParameterizedType) writeType).getActualTypeArguments()[0];
                        Type writeValueType = ((ParameterizedType) writeType).getActualTypeArguments()[1];
                        Type readKeyType = ((ParameterizedType) readType).getActualTypeArguments()[0];
                        Type readValueType = ((ParameterizedType) readType).getActualTypeArguments()[1];
                        if (isJavaBean((Class<?>) writeKeyType) || isJavaBean((Class<?>) readKeyType)) {
                            // 如果Map的key不为基本类型或String类型则跳过
                            continue;
                        }
                        if (!canCopy((Class<?>) writeValueType, (Class<?>) readValueType)) {
                            // 如果Map的value不能拷贝则跳过
                            continue;
                        }
                        Map targetMap = (Map) value.getClass().newInstance();
                        String prefix = targetPd.getName() + ".";
                        String[] subFilter = ignoreProperties;
                        if (!CollectionUtils.isEmpty(ignoreList)) {
                            subFilter = ignoreList.stream().filter(ignore -> ignore.startsWith(prefix))
                                    .map(ignore -> ignore.substring(prefix.length())).toArray(String[]::new);
                        }
                        Iterator it = sourceMap.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry entry = (Map.Entry) it.next();
                            Object targetKey;
                            Object targetValue;
                            if (writeKeyType != readKeyType && isJavaBean((Class<?>) writeKeyType) && isJavaBean((Class<?>) readKeyType)) {
                                targetKey = ((Class) writeKeyType).newInstance();
                                copyProperties(entry.getKey(), targetKey, editable, subFilter);
                            } else {
                                targetKey = entry.getKey();
                            }
                            if (writeValueType != readValueType && isJavaBean((Class<?>) writeValueType) && isJavaBean((Class<?>) readValueType)) {
                                targetValue = ((Class) writeValueType).newInstance();
                                copyProperties(entry.getValue(), targetValue, editable, subFilter);
                            } else {
                                targetValue = entry.getValue();
                            }
                            targetMap.put(targetKey, targetValue);
                        }
                        value = targetMap;
                    }
                    writeMethod.invoke(target, value);
                } else if (value != null && isJavaBean(sourceType) && isJavaBean(targetType)) {
                    // 源(source)与目标(target)类型不匹配时，且源(source)类型和目标(target)类型都不为基本类型或String类型时
                    Object subTarget = writeMethod.getParameterTypes()[0].newInstance();
                    copyProperties(value, subTarget, editable, ignoreProperties);
                    writeMethod.invoke(target, subTarget);
                }
            } catch (Throwable ex) {
                throw new FatalBeanException("Could not deep copy property '" + targetPd.getName() + "' from source to target", ex);
            }
        }
    }

    /**
     * 判断是否为JavaBean
     *
     * @param type 类型
     * @return true/false
     */
    private static boolean isJavaBean(Class<?> type) {
        // 不为基本类型或String类型
        return !(ClassUtils.isPrimitiveWrapper(type) || String.class.equals(type));
    }

    /**
     * 判断两个类型是否可以拷贝
     *
     * @param lhsType the target type
     * @param rhsType the value type that should be assigned to the target type
     * @return 可以拷贝返回true，否则返回false
     */
    private static boolean canCopy(Class<?> lhsType, Class<?> rhsType) {
        if (ClassUtils.isAssignable(lhsType, rhsType)) {
            // 源(source)与目标(target)类型匹配，可进行拷贝
            return true;
        }
        if (isJavaBean(lhsType) && isJavaBean(rhsType)) {
            // 源(source)与目标(target)都是JavaBean，可递归进行拷贝
            return true;
        }
        return false;
    }
}