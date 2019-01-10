package com.darthside.movienights.database;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TokenTable extends MongoRepository<Token, String> {


}
