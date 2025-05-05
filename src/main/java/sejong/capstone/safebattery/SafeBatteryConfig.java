package sejong.capstone.safebattery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sejong.capstone.safebattery.repository.ClientRepository;
import sejong.capstone.safebattery.repository.PemfcRepository;
import sejong.capstone.safebattery.repository.PredictionRepository;
import sejong.capstone.safebattery.repository.RecordRepository;
import sejong.capstone.safebattery.service.RecordService;

@Configuration
public class SafeBatteryConfig {

    @Value("${ai.server.url}")
    private String aiServerUrl;

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
    public DataInit dataInit(
            RecordRepository recordRepository,
            PemfcRepository pemfcRepository,
            ClientRepository clientRepository,
            PredictionRepository predictionRepository,
            RecordService recordService
    ) {
        return new DataInit(recordRepository, pemfcRepository, clientRepository, predictionRepository, recordService);
    }
}
