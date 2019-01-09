package com.darthside.movienights.database;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MovieTable extends MongoRepository<Movie, String> {

    public Movie findDistinctFirstByTitleIgnoreCase(String title);

    public List<Movie> findByGenre(String genre);

}
