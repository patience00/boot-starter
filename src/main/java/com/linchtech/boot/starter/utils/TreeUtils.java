package com.linchtech.boot.starter.utils;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 树结构工具类
 *
 * @author yaolinqi
 * @date 2019-10-31
 * @since 1.0.0
 */
public final class TreeUtils {
    private TreeUtils() {

    }

    /**
     * 集合转树结构（不排序）
     *
     * @param collection 目标集合
     * @param clazz      集合元素类型
     * @param <T>        泛型
     * @return 转换后的树形结构
     */
    public static <T> Collection<T> toTree(@NotNull Collection<T> collection, @NotNull Class<T> clazz) {
        return toTree(collection, null, null, null, null, clazz);
    }

    /**
     * 集合转树结构（排序）
     *
     * @param collection 目标集合
     * @param clazz      集合元素类型
     * @param sort       是否需要排序
     * @param <T>        泛型
     * @return 转换后的树形结构
     */
    public static <T> Collection<T> toTree(@NotNull Collection<T> collection, Boolean sort, @NotNull Class<T> clazz) {
        return toTree(collection, null, null, null, sort, clazz);
    }

    /**
     * 集合转树结构
     *
     * @param collection 目标集合
     * @param id         节点编号字段名称
     * @param parent     父节点编号字段名称
     * @param children   子节点集合属性名称
     * @param sort       是否需要排序
     * @param clazz      集合元素类型
     * @param <T>        泛型
     * @return 转换后的树形结构
     */
    public static <T> Collection<T> toTree(Collection<T> collection,
                                           String id,
                                           String parent,
                                           String children,
                                           Boolean sort,
                                           Class<T> clazz) {
        try {
            if (collection == null || collection.isEmpty()) {
                // 如果目标集合为空,直接返回一个空树
                return null;
            }
            if (StringUtils.isEmpty(id)) {
                // 如果被依赖字段名称为空则默认为id
                id = "id";
            }
            if (StringUtils.isEmpty(parent)) {
                // 如果依赖字段为空则默认为parent
                parent = "parent";
            }
            if (StringUtils.isEmpty(children)) {
                // 如果子节点集合属性名称为空则默认为children
                children = "children";
            }

            // 初始化根节点集合, 支持 Set 和 List
            Collection<T> roots;
            if (collection.getClass().isAssignableFrom(Set.class)) {
                roots = new HashSet<>();
            } else {
                roots = new ArrayList<>();
            }

            // 获取 id 字段, 从当前对象或其父类
            Field idField;
            try {
                idField = clazz.getDeclaredField(id);
            } catch (NoSuchFieldException e1) {
                idField = clazz.getSuperclass().getDeclaredField(id);
            }

            // 获取 parentId 字段, 从当前对象或其父类
            Field parentField;
            try {
                parentField = clazz.getDeclaredField(parent);
            } catch (NoSuchFieldException e1) {
                parentField = clazz.getSuperclass().getDeclaredField(parent);
            }

            // 获取 children 字段, 从当前对象或其父类
            Field childrenField;
            try {
                childrenField = clazz.getDeclaredField(children);
            } catch (NoSuchFieldException e1) {
                childrenField = clazz.getSuperclass().getDeclaredField(children);
            }

            // 设置为可访问
            idField.setAccessible(true);
            parentField.setAccessible(true);
            childrenField.setAccessible(true);

            // 找出所有的根节点
            // for (T c : collection) {
            //     Object parentId = parentField.get(c);
            //     if (rootNodes(parentId)) {
            //         roots.add(c);
            //     }
            // }
            roots.addAll(rootNodes(collection));

            // 从目标集合移除所有根节点
            collection.removeAll(roots);

            // 遍历根节点, 依次添加子节点
            for (T root : roots) {
                addChild(root, collection, idField, parentField, childrenField);
            }

            if (sort != null && sort) {
                // 遍历根节点, 依次对子节点进行排序
                for (T root : roots) {
                    sortChild(root, childrenField);
                }
            }

            // 关闭可访问
            idField.setAccessible(false);
            parentField.setAccessible(false);
            childrenField.setAccessible(false);

            return roots;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 为目标节点添加子节点
     *
     * @param node          目标节点
     * @param collection    目标集合
     * @param idField       ID 字段
     * @param parentField   父节点字段
     * @param childrenField 字节点字段
     * @param <T>           泛型
     * @throws IllegalAccessException 非法访问异常
     */
    private static <T> void addChild(@NotNull T node,
                                     @NotNull Collection<T> collection,
                                     @NotNull Field idField,
                                     @NotNull Field parentField,
                                     @NotNull Field childrenField) throws IllegalAccessException {
        Object id = idField.get(node);
        Collection<T> children = (Collection<T>) childrenField.get(node);
        // 如果子节点的集合为 null, 初始化孩子集合
        if (children == null) {
            if (collection.getClass().isAssignableFrom(Set.class)) {
                children = new HashSet<>();
            } else {
                children = new ArrayList<>();
            }
        }

        for (T t : collection) {
            Object o = parentField.get(t);
            if (id != null && id.equals(o)) {
                // 将当前节点添加到目标节点的孩子节点
                children.add(t);
                // 重设目标节点的孩子节点集合,这里必须重设,因为如果目标节点的孩子节点是null的话,这样是没有地址的,就会造成数据丢失,所以必须重设,如果目标节点所在类的孩子节点初始化为一个空集合,而不是null,则可以不需要这一步,因为java一切皆指针
                childrenField.set(node, children);
                // 递归添加孩子节点
                addChild(t, collection, idField, parentField, childrenField);
            }
        }
    }

    /**
     * 子节点排序
     *
     * @param node          目标节点
     * @param childrenField 字节点字段
     * @param <T>           泛型
     * @throws IllegalAccessException 非法访问异常
     */
    private static <T> void sortChild(@NotNull T node, @NotNull Field childrenField) throws IllegalAccessException {
        Collection<T> children = (Collection<T>) childrenField.get(node);
        // 如果子节点的集合不为 null
        if (CollectionUtils.isNotEmpty(children)) {
            Collections.sort((List) children);
            for (T child : children) {
                sortChild(child, childrenField);
            }
        }
    }

    /**
     * 判断是否是根节点, 判断方式为: 父节点编号为空或为 0, 则认为是根节点. 此处的判断应根据自己的业务数据而定.
     *
     * @param parentId 父节点编号
     * @return 是否是根节点
     */
    private static boolean rootNodes(Object parentId) {
        boolean flag = false;
        if (parentId == null) {
            flag = true;
        } else if (parentId instanceof String && (StringUtils.isEmpty((String) parentId) || parentId.equals("0"))) {
            flag = true;
        } else if (parentId instanceof Integer && Integer.valueOf(0).equals(parentId)) {
            flag = true;
        }
        return flag;
    }

    /**
     * 查找所有根节点, 判断方式为: 从任意节点向上查询,当某一个节点的parentId不存在集合中,则为root节点
     *
     * @param collection 所有元素
     * @return 是否是根节点
     */
    private static <T> Collection<T> rootNodes(Collection<T> collection) {
        Collection<T> roots = Lists.newArrayList();
        try {
            for (T node : collection) {
                T t = preNode(collection, node);
                if (!roots.contains(t) && t != null) {
                    roots.add(t);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roots;
    }

    /**
     * 查找上一级父节点
     *
     * @param collection
     * @param childNode
     * @param <T>
     * @return
     */
    private static <T> T preNode(Collection<T> collection, T childNode) {
        try {
            for (T node : collection) {
                Class<?> nodeClass = node.getClass();
                Class<?> childNodeClass = childNode.getClass();
                Field parentIdField = nodeClass.getDeclaredField("parentId");
                Field idField = nodeClass.getDeclaredField("id");
                parentIdField.setAccessible(true);
                idField.setAccessible(true);
                Integer id = (Integer) idField.get(node);
                Field childNodeField = childNodeClass.getDeclaredField("parentId");
                childNodeField.setAccessible(true);
                Integer parentId = (Integer) childNodeField.get(childNode);
                // 找到了parent
                if (id != null && id.equals(parentId)) {
                    return preNode(collection, node);
                }
            }
            // 遍历完依然没有父节点,则当前childNode就已经是最顶层node
            return childNode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 递归查找所有父节点
     *
     * @param elements      所有元素
     * @param childrenNodes 需要找父节点的节点
     * @param <T>
     * @return 包括子元素的所有父节点
     * @throws Exception
     */
    public static <T> List<T> findParent(List<T> elements, List<T> childrenNodes) {
        List<T> allParent = Lists.newArrayList();
        for (T node : childrenNodes) {
            Class<?> elementClass = node.getClass();
            try {
                Field parentIdField = elementClass.getDeclaredField("parentId");
                parentIdField.setAccessible(true);
                Integer parentId = (Integer) parentIdField.get(node);
                addPreNode(allParent, elements, parentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return allParent;
    }


    /**
     * 查找并添加上一级父节点
     *
     * @param elements 所有元素
     * @param parentId 父id
     * @param <T>
     * @return
     * @throws Exception
     */
    private static <T> void addPreNode(List<T> allNodes, List<T> elements, Integer parentId) throws Exception {
        for (T element : elements) {
            Class<?> elementClass = element.getClass();
            Field idField = elementClass.getDeclaredField("id");
            Field parentIdField = elementClass.getDeclaredField("parentId");
            idField.setAccessible(true);
            parentIdField.setAccessible(true);
            Integer id = (Integer) idField.get(element);
            Integer preParentId = (Integer) parentIdField.get(element);
            if (parentId.equals(id)) {
                addPreNode(allNodes, elements, preParentId);
                allNodes.add(element);
                return;
            }
        }
    }


    public static void main(String[] args) throws Exception {
        System.out.println();
    }
}
