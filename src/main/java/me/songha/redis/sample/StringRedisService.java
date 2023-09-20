package me.songha.redis.sample;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StringRedisService {

    private final StringRedisTemplate stringRedisTemplate;

    public void setValues(String key, String value) {
        ValueOperations<String, String> values = stringRedisTemplate.opsForValue();
        values.set(key, value, Duration.ofMinutes(100)); // 100분 뒤 메모리에서 삭제된다.
    }

    public String getValues(String name) {
        ValueOperations<String, String> values = stringRedisTemplate.opsForValue();
        return values.get(name);
    }

    public Set<String> getKeys(String pattern) {
        return stringRedisTemplate.keys(pattern);
    }

}