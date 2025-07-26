package com.example.thermos.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp initializeFirebase() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            try {
                // Thử đọc service account key từ classpath
                InputStream serviceAccount = new ClassPathResource("firebase-service-account-key.json").getInputStream();
                
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setDatabaseUrl("https://your-project-id-default-rtdb.firebaseio.com")
                        .build();

                return FirebaseApp.initializeApp(options);
            } catch (Exception e) {
                // Fallback: sử dụng default credentials hoặc tạo app với minimal config
                System.out.println("Warning: Could not load Firebase service account key. Using default configuration.");
                System.out.println("Please add firebase-service-account-key.json to src/main/resources/ for full Firebase functionality.");
                
                // Tạo FirebaseApp với cấu hình tối thiểu để ứng dụng vẫn chạy được
                FirebaseOptions options = FirebaseOptions.builder()
                        .setDatabaseUrl("https://your-project-id-default-rtdb.firebaseio.com")
                        .build();
                
                return FirebaseApp.initializeApp(options);
            }
        }
        return FirebaseApp.getInstance();
    }
}