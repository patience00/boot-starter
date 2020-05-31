package com.linchtech.boot.starter.utils;

import org.springframework.util.StringUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinchStringUtils {
    private static final String UNDERLINE = "_";

    /**
     * 默认将inputStream转换为utf-8 字符串
     * @param in
     * @return utf-8 字符串
     * @throws UnsupportedEncodingException
     */
    public static String convertStreamToString(InputStream in) throws UnsupportedEncodingException {
        return convertStreamToString(in, "UTF-8");
    }

    /**
     * 将inputStream转换为字符串
     * @param in
     * @param charset 编码格式
     * @return 字符串
     * @throws UnsupportedEncodingException
     */
    public static String convertStreamToString(InputStream in,
                                               String charset) throws UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("/n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 替换字符串中的特殊字符
     *
     * @return 去除特殊字符后的字符串
     */
    public static String replaceOtherSymbol(String str) {
        String regEx = "([\\u4e00-\\u9fa5]|\\w)+";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            String group = m.group();
            sb.append(group);
        }
        return sb.toString();
    }

    /**
     * 去除string空格
     *
     * @param object
     * @throws Exception
     */
    public static void deleteSpace(Object object) throws Exception {
        Class formClass = object.getClass();

        // 去除空格
        Field[] formClassDeclaredFields = formClass.getDeclaredFields();
        for (Field field : formClassDeclaredFields) {
            field.setAccessible(true);
            if (String.class.isAssignableFrom(field.getType())) {
                String val = (String) field.get(object);
                if (!StringUtils.isEmpty(val)) {
                    field.set(object, val.trim());
                }
            }
        }
    }


    /**
     * 驼峰格式字符串转换为下划线格式字符串
     *
     * @param param
     * @return
     */
    public static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(UNDERLINE);
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 将字母大写转小写,小写转大写
     *
     * @param title
     * @return
     */
    public static String changeUpAndLow(String title) {
        byte[] encode = Base64.getEncoder().encode(title.getBytes());
        String s = new String(encode);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isUpperCase(c)) {
                stringBuilder.append(Character.toLowerCase(c));
            } else {
                stringBuilder.append(Character.toUpperCase(c));
            }
        }
        return stringBuilder.toString();
    }
}
