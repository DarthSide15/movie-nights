package com.darthside.movienights;

import com.darthside.movienights.database.Movie;
import com.darthside.movienights.database.MovieTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class OMDBController {

    private final String apiKey = "&apikey=6c247c78";
    @Autowired
    MovieTable movieTable;
    private RestTemplate restTemplate = new RestTemplate();

    @RequestMapping(value = "/movie", method = RequestMethod.GET)
    public Movie getMovie(@RequestParam(value = "title", defaultValue= " ") String title) {
        // TODO: If movie is already in DB, do not make request.
        if (movieTable.findDistinctFirstByTitleIgnoreCase(title) == null) {
            Movie movie = restTemplate.getForObject("https://www.omdbapi.com/?t=" + title + apiKey, Movie.class);
            if (movie.getTitle() != null)
                movieTable.save(movie);
            System.out.println("Movie saved to DB");
            return movie;
        }
        else {
            System.out.println("Movie was already in DB");
            return movieTable.findDistinctFirstByTitleIgnoreCase(title);
        }
    }
}
