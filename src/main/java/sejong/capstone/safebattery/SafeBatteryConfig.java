package sejong.capstone.safebattery;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sejong.capstone.safebattery.domain.PowerPrediction;
import sejong.capstone.safebattery.domain.TemperaturePrediction;
import sejong.capstone.safebattery.domain.VoltagePrediction;
import sejong.capstone.safebattery.repository.*;
import sejong.capstone.safebattery.service.RecordService;

@Configuration
@RequiredArgsConstructor
public class SafeBatteryConfig {

    @Value("${ai.server.url}")
    private String aiServerUrl;
    private final JdbcTemplate jdbcTemplate;
    private final PemfcRepository pemfcRepository;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 모든 경로에 대해 CORS 허용
                    .allowedOrigins("http://localhost:3000")  // React 앱 주소
                    .allowedMethods("GET", "POST", "PUT", "DELETE")  // 허용할 HTTP 메서드
                    .allowedHeaders("*")  // 모든 헤더 허용
                    .allowCredentials(true);  // 인증이 필요한 요청 허용
            }
        };
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
            .baseUrl(aiServerUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

    @Bean
    public PredictionRankRepository<VoltagePrediction> voltagePredictionRankRepository() {
        return new PredictionRankRepository<>(jdbcTemplate, pemfcRepository,
            VoltagePrediction.class);
    }

    @Bean
    public PredictionRankRepository<PowerPrediction> powerPredictionRankRepository() {
        return new PredictionRankRepository<>(jdbcTemplate, pemfcRepository, PowerPrediction.class);
    }

    @Bean
    public PredictionRankRepository<TemperaturePrediction> temperaturePredictionRankRepository() {
        return new PredictionRankRepository<>(jdbcTemplate, pemfcRepository, TemperaturePrediction.class);
    }
}
