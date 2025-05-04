package org.example.belitjo.service;

import jakarta.transaction.Transactional;
import org.example.belitjo.model.Customer;
import org.example.belitjo.model.Movie;
import org.example.belitjo.model.Ticket;
import org.example.belitjo.repository.CustomerRepository;
import org.example.belitjo.repository.MovieRepository;
import org.example.belitjo.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {
    private final CustomerRepository customerRepository;
    private final MovieRepository movieRepository;
    private final TicketRepository ticketRepository;

    public TicketService(CustomerRepository customerRepository,
                         MovieRepository movieRepository,
                         TicketRepository ticketRepository) {
        this.customerRepository = customerRepository;
        this.movieRepository = movieRepository;
        this.ticketRepository = ticketRepository;
    }
    public List<Ticket> findAllTicket(){
        return ticketRepository.findAll();
    }


    @Transactional
    public Ticket purchaseTicket(Long customerId, Long movieId, int quantity) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(()
                -> new RuntimeException("customer not found"));
        Movie movie = movieRepository.findById(movieId).orElseThrow(()
                -> new RuntimeException("movie not found"));

        if (quantity <= 0) {
            throw new IllegalArgumentException("The number of tickets must be positive");
        }

        if (movie.getAvailableTickets() < quantity) {
            throw new RuntimeException("The requested number of tickets exceeds the available stock");
        }

        movie.setAvailableTickets(movie.getAvailableTickets() - quantity);
        movieRepository.save(movie);

        Ticket ticket = new Ticket(movie, customer, quantity);
        return ticketRepository.save(ticket);
    }

    public Long calculateTotalPrice(Long movieId, int quantity) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(()
                -> new RuntimeException("movie not found"));

        if (quantity <= 0) {
            throw new IllegalArgumentException("The number of tickets must be positive");
        }
        return (movie.getPrice() * quantity);
    }
}
