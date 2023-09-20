package me.songha.redis.sample;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class StringRedisController {

    private final StringRedisService stringRedisService;

    @GetMapping
    public String ok() {
        return "ok";
    }

    @GetMapping("/key/{key}")
    public String getKey(@PathVariable String key) {
        return stringRedisService.getValues(key);
    }

    @GetMapping("/keys/{pattern}")
    public Set<String> getKeys(@PathVariable String pattern) {
        return stringRedisService.getKeys(pattern);
    }

    @PostMapping
    public void setValue(@RequestBody Map<String, String> body) {
        stringRedisService.setValues(body.get("key"), body.get("value"));
    }
}