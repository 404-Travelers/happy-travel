package com._travelers.happy_travel.config;

import com._travelers.happy_travel.users.Role;
import com._travelers.happy_travel.users.User;
import com._travelers.happy_travel.users.UserRepository;
import com._travelers.happy_travel.destinations.Destination;
import com._travelers.happy_travel.destinations.DestinationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner seedSampleData(
            UserRepository userRepository,
            DestinationRepository destinationRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            if (userRepository.count() > 1) {
                System.out.println("Sample users already seeded.");
                return;
            }

            User user1 = new User(null,"johndoe", "john@example.com", passwordEncoder.encode("password123"), Role.ROLE_USER, new ArrayList<>());
            User user2 = new User(null, "moderator", "mod@example.com", passwordEncoder.encode("modpass12"), Role.ROLE_USER, new ArrayList<>());
            userRepository.saveAll(Arrays.asList(user1, user2));
            System.out.println("Sample users seeded.");

            Destination paris = new Destination(null,  "France", "Paris", "City of lights and love.", "https://thiscouplewent.wordpress.com/wp-content/uploads/2016/02/img_0219.jpg", user1);
            Destination tokyo = new Destination(null, "Japan", "Tokyo", "The ultra-modern capital of Japan.","https://jamiechancetravels.com/wp-content/uploads/2025/03/DSCF3071.jpg" , user1);
            Destination rome = new Destination(null, "Italy", "Rome", "The Eternal City.","https://www.earthtrekkers.com/wp-content/uploads/2023/01/Roman-Forum.jpg.optimal.jpg" , user2);

            destinationRepository.saveAll(Arrays.asList(paris, tokyo, rome));
            System.out.println("Sample destinations seeded.");
        };
    }
}