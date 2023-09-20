package me.songha.redis.sample;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StringRedisServiceTest {

    @Autowired
    private StringRedisService stringRedisService;

    @Test
    public void printf() {
        System.out.println("print!!");
    }

    @Test
    public void set() {
        stringRedisService.setValues("test", "1111");
    }

    @Test
    public void get() {
        System.out.println(stringRedisService.getValues("hello"));
    }
}