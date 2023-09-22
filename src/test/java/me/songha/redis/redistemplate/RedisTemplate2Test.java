package me.songha.redis.redistemplate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import java.util.Map;
import java.util.Set;

@SpringBootTest
public class RedisTemplate2Test {

    @Autowired
    private StringRedisTemplate redisTemplate;
    private ValueOperations<String, String> valueOperations;
    private ListOperations<String, String> listOperations;
    private HashOperations<String, String, String> hashOperations;
    private SetOperations<String, String> setOperations;
    private ZSetOperations<String, String> zSetOperations;

    @BeforeEach
    public void init() {
        valueOperations = redisTemplate.opsForValue();
        listOperations = redisTemplate.opsForList();
        hashOperations = redisTemplate.opsForHash();
        setOperations = redisTemplate.opsForSet();
        zSetOperations = redisTemplate.opsForZSet();

        //list put
        listOperations.rightPush("test:task", "introduce");
        listOperations.rightPush("test:task", "hobby");
        listOperations.rightPush("test:task", "wish");
        //hash put
        hashOperations.put("test:user:songha", "name", "홍길동");
        hashOperations.put("test:user:songha", "age", "32");
        //set put
        setOperations.add("test:user:songha:hobby", "develop");
        setOperations.add("test:user:songha:hobby", "musical");
        setOperations.add("test:user:songha:hobby", "shopping");
        //zset
        zSetOperations.add("test:user:songha:wish", "blablabla1", 1);
        zSetOperations.add("test:user:songha:wish", "blablabla2", 2);
        zSetOperations.add("test:user:songha:wish", "blablabla3", 3);
        zSetOperations.add("test:user:songha:wish", "blablabla4", 4);
    }

    @AfterEach
    void afterAll() {
        redisTemplate.keys("test:user:songha*").forEach(k -> {
            redisTemplate.delete(k);
        });
    }

    @Test
    public void test() {
        String task = listOperations.leftPop("test:task");
        StringBuilder stringBuilder = new StringBuilder();
        while (task != null) {
            switch (task) {
                case "introduce" -> {
                    Map<String, String> intro = hashOperations.entries("test:user:songha");
                    stringBuilder.append("\n===== Introcude =====");
                    stringBuilder.append("\nMy name is ");
                    stringBuilder.append(intro.get("name"));
                    stringBuilder.append("\nMy age is ");
                    stringBuilder.append(intro.get("age"));
                }
                case "hobby" -> {
                    Set<String> hobbys = setOperations.members("test:user:songha:hobby");
                    stringBuilder.append("\n===== hobby =====");
                    stringBuilder.append("\nHobby is ");
                    for (String hobby : hobbys) {
                        stringBuilder.append(hobby);
                        stringBuilder.append(", ");
                    }
                }
                case "wish" -> {
                    Set<String> wishes = zSetOperations.range("test:user:songha:wish", 0, 2);
                    stringBuilder.append("\n===== wish =====");
                    int rank = 1;
                    for (String wish : wishes) {
                        stringBuilder.append("\n");
                        stringBuilder.append("rank ");
                        stringBuilder.append(rank++);
                        stringBuilder.append(" : ");
                        stringBuilder.append(wish);
                    }
                }
                default -> stringBuilder.append("none");
            }
            task = listOperations.leftPop("test:task");
        }
        System.out.println(stringBuilder);
    }
}

