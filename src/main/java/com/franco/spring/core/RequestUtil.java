package com.franco.spring.core;

import com.google.common.base.Strings;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

/**
 * RequestUtil
 *
 * @author franco
 */
public class RequestUtil {

    /**
     * 从文本中以"&"分割提取NameValuePair
     * 将pair经过@code{URLDecoder.decode()}方法转换成实际值
     * 放入paramMap
     *
     * @param content
     * @param paramMap
     */
    public static void parseParam(String content, Map<String, String[]> paramMap) {
        Arrays.stream(content.trim().split("&")).filter(Strings::isNullOrEmpty)
                .map(RequestUtil::splitNameValuePair)
                .forEach((pair) -> {
                    putNameValuePair(pair, paramMap, (s) -> URLDecoder.decode(s, StandardCharsets.UTF_8));
                });
    }

    /**
     * 从文本中以"&"分割提取NameValuePair
     * 将pair存放入paramMap当中
     *
     * @param content
     * @param paramMap
     */
    public static void parseParamWithoutDecode(String content, Map<String, String[]> paramMap) {
        Arrays.stream(content.trim().split("&")).filter(Strings::isNullOrEmpty)
                .map(RequestUtil::splitNameValuePair)
                .forEach((pair) -> {
                    putNameValuePair(pair, paramMap, Function.identity());
                });
    }

    private static String[] splitNameValuePair(String s) {
        int index = s.indexOf("=");
        if(index == -1) {
            return new String[]{s, null};
        } else {
            return new String[]{s.substring(0, index), s.substring(index + 1)};
        }
    }

    private static void putNameValuePair(String[] str, Map<String, String[]> paramsMap, Function<String, String> decoder) {
        for (int i = 0; i < str.length; i++) {
            str[i] = decoder.apply(str[i]);
        }
        if (paramsMap.containsKey(str[0])) {
            paramsMap.put(str[0], appendValue(paramsMap.get(str[0]), str[1]));
        } else {
            paramsMap.put(str[0], new String[] { str[1] });
        }
    }

    public static String[] appendValue(String[] values, String value) {
        if(values == null || values.length == 0) {
            return new String[] { value };
        }
        String[] newValues = new String[values.length + 1];
        System.arraycopy(values, 0, newValues, 0, values.length);
        newValues[newValues.length - 1] = value;
        return newValues;
    }
}
