package org.example.belitjo.service;

import org.example.belitjo.model.Movie;
import org.example.belitjo.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("movie not found!"));
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie addMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public Movie updateMovie(Long id, Movie updatedMovie) {
        Movie movie = getMovieById(id);
        movie.setTitle(updatedMovie.getTitle());
        movie.setPrice(updatedMovie.getPrice());
        movie.setAvailableTickets(updatedMovie.getAvailableTickets());
        return movieRepository.save(movie);
    }
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    public List<Movie> getAvailableMovies() {
        return movieRepository.findByAvailableTicketsGreaterThan(0);
    }
}
