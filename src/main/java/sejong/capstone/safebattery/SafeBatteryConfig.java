package sejong.capstone.safebattery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import sejong.capstone.safebattery.repository.ClientRepository;
import sejong.capstone.safebattery.repository.PemfcRepository;
import sejong.capstone.safebattery.repository.PredictionRepository;
import sejong.capstone.safebattery.repository.RecordRepository;

@Configuration
public class SafeBatteryConfig {

    @Value("${ai.server.url}")
    private String aiServerUrl;

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
            PredictionRepository predictionRepository
    ) {
        return new DataInit(recordRepository, pemfcRepository, clientRepository, predictionRepository);
    }
}
