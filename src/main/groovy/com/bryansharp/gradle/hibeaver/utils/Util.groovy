package com.bryansharp.gradle.hibeaver.utils

import java.util.regex.Pattern

/**
 * Created by bryansharp(bsp0911932@163.com) on 2016/5/10.
 *
 * @author bryansharp
 * Project: FirstGradle
 * introduction:
 */
public class Util {
    public static boolean regMatch(String pattern, String target) {
        if (isEmpty(pattern) || isEmpty(target)) {
            return false;
        }
        return Pattern.matches(pattern, target);
    }

    public static int typeString2Int(String type) {
        if (type == null || Const.VALUE_ALL.equals(type)) {
            return Const.MT_FULL;
        } else if (Const.VALUE_REGEX.equals(type)) {
            return Const.MT_REGEX;
        } else if (Const.VALUE_WILDCARD.equals(type)) {
            return Const.MT_WILDCARD;
        } else {
            return Const.MT_FULL;
        }
    }

    public static boolean isPatternMatch(String pattern, String type, String target) {
        if (isEmpty(pattern) || isEmpty(target)) {
            return false;
        }
        int intType = typeString2Int(type);
        switch (intType) {
            case Const.MT_FULL:
                if (target.equals(pattern)) {
                    return true;
                }
                break;
            case Const.MT_REGEX:
                if (regMatch(pattern, target)) {
                    return true;
                }
                break;
            case Const.MT_WILDCARD:
                if (wildcardMatch(pattern, target)) {
                    return true;
                }
                break;
        }
        return false;
    }

    public static boolean isEmpty(String text) {
        return text == null || text.trim().length() < 1;
    }

    public static boolean isNotEmpty(String text) {
        return !isEmpty(text);
    }
    /**
     * 星号匹配
     * @param pattern
     * @param target
     * @return
     * new StringBuilder()
     .append(wildcardMatch("com.**.act.*.github.*Activity", "com.jj.act.jj.github.MainActivity")).append(",") //true
     .append(wildcardMatch("*Activity", "com.jj.act.jj.github.MainActivity")).append(",")//true
     .append(wildcardMatch("*Activity", "com.jj.act.jjActivity")).append(",")//true
     .append(wildcardMatch("*Activity*", "com.jj.act.jjActivity")).append(",")//false
     .append(wildcardMatch(".*Activity", "com.Activity")).append(",")//false
     .append(wildcardMatch("com.**.a*t.*.github.*Activity", "com.jj.act.jj.github.MainActivity")).append(",")//true
     .append(wildcardMatch("com.**.act.*.gi*ub.*Act*vity", "com.jj.MainActivity.act")).append(",")//false
     .append(wildcardMatch("com.**.act.*.gi*ub.*Act*vity", "com.jj.act.jj.github.Mactivity")).append(",")//false
     .toString()
     */
    public static boolean wildcardMatch(String pattern, String target) {
        if (isEmpty(pattern) || isEmpty(target)) {
            return false;
        }
        try {
            String[] split = pattern.split("\\*{1,3}");
            //如果以分隔符开头和结尾，第一位会为空字符串，最后一位不会为空字符，所以*Activity和*Activity*的分割结果一样
            if (pattern.endsWith("*")) {//因此需要在结尾拼接一个空字符
                List<String> strings = new LinkedList<>(Arrays.asList(split));
                strings.add("");
                split = new String[strings.size()];
                strings.toArray(split);
            }
            for (int i = 0; i < split.length; i++) {
                String part = split[i];
                if (isEmpty(target)) {
                    return false;
                }
                if (i == 0 && isNotEmpty(part)) {
                    if (!target.startsWith(part)) {
                        return false;
                    }
                }
                if (i == split.length - 1 && isNotEmpty(part)) {
                    if (!target.endsWith(part)) {
                        return false;
                    } else {
                        return true;
                    }
                }
                if (part == null || part.trim().length() < 1) {
                    continue;
                }
                int index = target.indexOf(part);
                if (index < 0) {
                    return false;
                }
                int newStart = index + part.length() + 1;
                if (newStart < target.length()) {
                    target = target.substring(newStart);
                } else {
                    target = "";
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}