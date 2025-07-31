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
import java.util.List;

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

            User user1 = new User(null, "johndoe", "john@example.com",
                    passwordEncoder.encode("password123"), Role.USER, new ArrayList<>());
            User user2 = new User(null, "moderator", "mod@example.com",
                    passwordEncoder.encode("modpass12"), Role.USER, new ArrayList<>());
            User user3 = new User(null, "femcoder", "coder@example.com",
                    passwordEncoder.encode("password123"), Role.USER, new ArrayList<>());

            userRepository.saveAll(Arrays.asList(user1, user2, user3));
            System.out.println("Sample users seeded.");

            Destination paris = new Destination(
                    null,
                    "France",
                    "Paris",
                    "City of lights and love.",
                    "https://thiscouplewent.wordpress.com/wp-content/uploads/2016/02/img_0219.jpg",
                    user1
            );

            Destination tokyo = new Destination(
                    null,
                    "Japan",
                    "Tokyo",
                    "The ultra-modern capital of Japan.",
                    "https://jamiechancetravels.com/wp-content/uploads/2025/03/DSCF3071.jpg",
                    user1
            );

            Destination rome = new Destination(
                    null,
                    "Italy",
                    "Rome",
                    "The Eternal City.",
                    "https://www.earthtrekkers.com/wp-content/uploads/2023/01/Roman-Forum.jpg.optimal.jpg",
                    user2
            );

            Destination london = new Destination(
                    null,
                    "United Kingdom",
                    "London",
                    "A vibrant capital blending history and modernity.",
                    "https://upload.wikimedia.org/wikipedia/commons/c/cd/London_Montage_L.jpg",
                    user2
            );

            Destination sydney = new Destination(
                    null,
                    "Australia",
                    "Sydney",
                    "Iconic harbour city known for its Opera House and beaches.",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/6/65/Sydney_montage_2018.jpg/500px-Sydney_montage_2018.jpg",
                    user3
            );

            Destination newYork = new Destination(
                    null,
                    "USA",
                    "New York",
                    "The city that never sleeps, full of energy and culture.",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/0/05/View_of_Empire_State_Building_from_Rockefeller_Center_New_York_City_dllu.jpg/960px-View_of_Empire_State_Building_from_Rockefeller_Center_New_York_City_dllu.jpg",
                    user1
            );

            Destination cairo = new Destination(
                    null,
                    "Egypt",
                    "Cairo",
                    "Gateway to ancient pyramids and rich history.",
                    "https://upload.wikimedia.org/wikipedia/commons/e/e3/Kheops-Pyramid.jpg",
                    user3
            );

            Destination rioDeJaneiro = new Destination(
                    null,
                    "Brazil",
                    "Rio de Janeiro",
                    "Famous for Copacabana beach and the Christ the Redeemer statue.",
                   "https://upload.wikimedia.org/wikipedia/commons/thumb/7/79/Rio_Collage.png/960px-Rio_Collage.png",
                    user2
            );

            Destination barcelona = new Destination(
                    null,
                    "Spain",
                    "Barcelona",
                    "A city known for Gaudí's architecture and vibrant culture.",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/3/38/Barcelona_1_2013.jpg/250px-Barcelona_1_2013.jpg",
                    user1
            );

            Destination vancouver = new Destination(
                    null,
                    "Canada",
                    "Vancouver",
                    "Scenic west coast city nestled between ocean and mountains.",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/1/11/Vancouver_Montage_2020.jpg/500px-Vancouver_Montage_2020.jpg",
                    user3
            );

            Destination bangkok = new Destination(
                    null,
                    "Thailand",
                    "Bangkok",
                    "A bustling metropolis with vibrant street life and temples.",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/a/af/Bangkok_Montage_2021.jpg/500px-Bangkok_Montage_2021.jpg",
                    user2
            );

            List<Destination> destinations = Arrays.asList(
                    paris, tokyo, rome,
                    london, sydney, newYork,
                    cairo, rioDeJaneiro, barcelona,
                    vancouver, bangkok
            );

            destinationRepository.saveAll(destinations);
            System.out.println("Sample destinations seeded.");
        };
    }
}
