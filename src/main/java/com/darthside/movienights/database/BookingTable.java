package com.darthside.movienights.database;

import com.darthside.movienights.database.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookingTable extends MongoRepository<Booking, String> {

}