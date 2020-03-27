package me.kazechin.janword;

import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import me.kazechin.janword.extra.weblio.Weblio;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@SpringBootApplication
public class JanWordApplication implements WebMvcConfigurer {

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new AuthenticationPrincipalArgumentResolver());
	}

	@Value("${spring.redis.host}")
	private String hostName;

	@Value("${spring.redis.password}")
	private String password;

	@Bean
	public JedisConnectionFactory redisConnectionFactory() {
		// TODO 为什么没有加载配置？还需要自己给值
		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(hostName, 6379);
		config.setPassword(password);
		return new JedisConnectionFactory(config);
	}

	@Bean
	public Weblio weblio() {
		return new Weblio();
	}

	@Bean
	public RedisTemplate redisTemplate(JedisConnectionFactory connectionFactory) {
		RedisTemplate redisTemplate = new RedisTemplate();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
		return redisTemplate;
	}


	public static void main(String[] args) {
		SpringApplication.run(JanWordApplication.class, args);
	}

}
