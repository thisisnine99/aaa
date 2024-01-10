package com.korea.MOVIEBOOK.Movie.Daily;

import com.korea.MOVIEBOOK.Movie.Movie.Movie;
import com.korea.MOVIEBOOK.Movie.Movie.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class MovieDailyService {
    private final MovieDailyRepository movieDailyRepository;
    private final MovieService movieService;

    public MovieDaily add(String movieCD, Long rank, String date){
        Movie movie = this.movieService.findMovieByCD(movieCD);
        MovieDaily movieDaily = new MovieDaily();
        movieDaily.setMovie(movie);
        movieDaily.setRank(rank);
        movieDaily.setDate(date);
        return this.movieDailyRepository.save(movieDaily);
    }
    public List<MovieDaily> findMovies(String date){
        return this.movieDailyRepository.findBydate(date);
    }
}
