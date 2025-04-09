package sejong.capstone.safebattery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class SafeBatteryConfig {
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://AI서버EC2인스턴스주소")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
