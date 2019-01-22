package com.darthside.movienights.database;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MovieTable extends MongoRepository<Movie, String> {

    Movie findDistinctFirstByTitleIgnoreCase(String title);
}
