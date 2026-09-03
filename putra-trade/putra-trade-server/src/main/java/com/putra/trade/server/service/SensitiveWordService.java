package com.putra.trade.server.service;

import com.putra.trade.common.context.BaseContext;
import com.putra.trade.common.utils.SensitiveWordFilter;
import com.putra.trade.pojo.entity.SensitiveWord;
import com.putra.trade.server.mapper.SensitiveWordMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 敏感词服务：启动时把词库加载进内存 DFA；后台增删词后即时刷新
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SensitiveWordService {

    private final SensitiveWordMapper sensitiveWordMapper;
    private final SensitiveWordFilter filter = new SensitiveWordFilter();

    @PostConstruct
    public void init() {
        reload();
    }

    public void reload() {
        List<String> words = sensitiveWordMapper.listWords();
        filter.reload(words);
        log.info("敏感词库加载完成, 共 {} 个词", words.size());
    }

    /** 返回命中的敏感词（空列表 = 干净） */
    public List<String> scan(String text) {
        return filter.scan(text);
    }

    public List<SensitiveWord> list() {
        return sensitiveWordMapper.list();
    }

    public void add(String word, Integer type) {
        SensitiveWord entity = SensitiveWord.builder()
                .word(word).type(type).createUser(BaseContext.getCurrentId()).build();
        sensitiveWordMapper.insert(entity);
        reload();
    }

    public void delete(Long id) {
        sensitiveWordMapper.deleteById(id);
        reload();
    }
}
