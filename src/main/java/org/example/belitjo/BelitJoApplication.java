package org.example.belitjo;

import org.example.belitjo.model.Customer;
import org.example.belitjo.model.Movie;
import org.example.belitjo.model.Ticket;
import org.example.belitjo.service.CustomerService;
import org.example.belitjo.service.MovieService;
import org.example.belitjo.service.TicketService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@SpringBootApplication
public class BelitJoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BelitJoApplication.class, args);
    }

    @Bean
    CommandLineRunner run(CustomerService customerService,
                          MovieService movieService,
                          TicketService ticketService) {
        return args -> {
            Scanner scanner = new Scanner(System.in);

            if (movieService.getAllMovies().isEmpty()) {
                movieService.addMovie(new Movie("Titanic", 15, 50000L));
                movieService.addMovie(new Movie("The Dark Knight", 10, 60000L));
                movieService.addMovie(new Movie("Avatar", 20, 55000L));
                movieService.addMovie(new Movie("Interstellar", 12, 65000L));
                movieService.addMovie(new Movie("Spider-Man", 18, 52000L));
                movieService.addMovie(new Movie("Harry Potter", 25, 48000L));
                movieService.addMovie(new Movie("The Lord of the Rings", 14, 70000L));
                movieService.addMovie(new Movie("Gladiator", 9, 53000L));
                movieService.addMovie(new Movie("Forrest Gump", 11, 47000L));
                movieService.addMovie(new Movie("Arrival", 13, 49000L));
                System.out.println("Initial movie data loaded.");
            }

            boolean adminEnded = false;

            while (!adminEnded) {
                Customer currentCustomer = null;

                while (currentCustomer == null && !adminEnded) {
                    System.out.println("\nPlease choose:");
                    System.out.println("1. Register");
                    System.out.println("2. Login");
                    System.out.println("3. End (admin only)");
                    System.out.print("Your choice: ");
                    String choice = scanner.nextLine();

                    if ("1".equals(choice)) {
                        System.out.print("Enter your name: ");
                        String name = scanner.nextLine().trim();

                        System.out.print("Enter your email: ");
                        String email = scanner.nextLine().trim();

                        Optional<Customer> existing = customerService.getAllCustomers()
                                .stream()
                                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                                .findFirst();

                        if (existing.isPresent()) {
                            System.out.println("Email already registered. Please login.");
                        } else {
                            Customer newCustomer = new Customer(name, email);
                            currentCustomer = customerService.createCustomer(newCustomer);
                            System.out.println("Registration successful. Welcome " + name + "!");
                        }
                    } else if ("2".equals(choice)) {
                        System.out.print("Enter your email: ");
                        String email = scanner.nextLine().trim();

                        Optional<Customer> customerOpt = customerService.getAllCustomers()
                                .stream()
                                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                                .findFirst();

                        if (customerOpt.isPresent()) {
                            currentCustomer = customerOpt.get();
                            System.out.println("Login successful. Welcome back " + currentCustomer.getName() + "!");
                        } else {
                            System.out.println("No user found with this email. Please register.");
                        }
                    } else if ("3".equals(choice)) {
                        adminEnded = true;
                        System.out.println("Admin ended the program.");
                    } else {
                        System.out.println("Invalid choice.");
                    }
                }

                if (adminEnded) {
                    break;
                }

                boolean continueShopping = true;

                while (continueShopping) {
                    List<Movie> availableMovies = movieService.getAllMovies().stream()
                            .filter(m -> m.getAvailableTickets() > 0)
                            .toList();

                    if (availableMovies.isEmpty()) {
                        System.out.println("No movies with available tickets found.");
                        break;
                    }

                    System.out.println("\nAvailable movies:");
                    availableMovies.forEach(m ->
                            System.out.printf("%d. %s - Tickets available: %d - Price: %d Toman%n",
                                    m.getId(), m.getTitle(), m.getAvailableTickets(), m.getPrice()));

                    System.out.print("Enter the ID of the movie you want to buy tickets for: ");
                    String movieIdStr = scanner.nextLine();

                    Long movieId;
                    try {
                        movieId = Long.parseLong(movieIdStr);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid movie ID.");
                        continue;
                    }

                    Optional<Movie> selectedMovieOpt = availableMovies.stream()
                            .filter(m -> m.getId().equals(movieId))
                            .findFirst();

                    if (selectedMovieOpt.isEmpty()) {
                        System.out.println("Movie with this ID not found or no tickets available.");
                        continue;
                    }

                    Movie movie = selectedMovieOpt.get();

                    System.out.print("Enter the number of tickets you want to buy: ");
                    String quantityStr = scanner.nextLine();

                    int quantity;
                    try {
                        quantity = Integer.parseInt(quantityStr);
                        if (quantity <= 0) {
                            System.out.println("The number of tickets must be a positive integer.");
                            continue;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid quantity.");
                        continue;
                    }

                    try {
                        Ticket ticket = ticketService.purchaseTicket(currentCustomer.getId(), movie.getId(), quantity);
                        Long totalPrice = ticketService.calculateTotalPrice(movie.getId(), quantity);
                        System.out.printf("Purchase successful! Total price: %d Toman%n", totalPrice);
                    } catch (Exception e) {
                        System.out.println("Error during purchase: " + e.getMessage());
                    }

                    System.out.print("Do you want to buy tickets for another movie? (yes/no): ");
                    String answer = scanner.nextLine();
                    if (!answer.equalsIgnoreCase("yes")) {
                        continueShopping = false;
                    }
                }

                System.out.println("Thank you for your purchase, " + currentCustomer.getName() + "!");

            }

            System.out.println("\nAll purchased tickets:");
            List<Ticket> allTickets = ticketService.findAllTicket();
            if (allTickets.isEmpty()) {
                System.out.println("No tickets have been purchased.");
            } else {
                for (Ticket ticket : allTickets) {
                    System.out.printf("Ticket ID: %d, Customer: %s, Movie: %s, Quantity: %d%n",
                            ticket.getId(),
                            ticket.getCustomer().getName(),
                            ticket.getMovie().getTitle(),
                            ticket.getQuantity());
                }
            }

            scanner.close();
        };
    }
}
