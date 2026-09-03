package com.putra.trade.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 敏感词 DFA（确定有穷自动机）过滤器
 * <p>
 * 词库构建成字典树，扫描文本 O(n)，与文本长度线性相关、与词库大小几乎无关。
 */
public class SensitiveWordFilter {

    private final Map<Character, Object> root = new HashMap<>();
    private static final String END_FLAG = "isEnd";

    /**
     * 用新词库重建字典树
     */
    public synchronized void reload(List<String> words) {
        root.clear();
        for (String word : words) {
            if (word == null || word.isEmpty()) {
                continue;
            }
            Map<Character, Object> node = root;
            for (char c : word.toCharArray()) {
                node = (Map<Character, Object>) node.computeIfAbsent(c, k -> new HashMap<Character, Object>());
            }
            ((Map) node).put(END_FLAG, true);
        }
    }

    /**
     * 扫描文本，返回命中的敏感词列表（无命中返回空列表）
     */
    public List<String> scan(String text) {
        List<String> hits = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            return hits;
        }
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            Map node = root;
            StringBuilder matched = new StringBuilder();
            for (int j = i; j < chars.length; j++) {
                node = (Map) node.get(chars[j]);
                if (node == null) {
                    break;
                }
                matched.append(chars[j]);
                if (Boolean.TRUE.equals(node.get(END_FLAG))) {
                    hits.add(matched.toString());
                    break;
                }
            }
        }
        return hits;
    }
}
