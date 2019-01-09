package com.darthside.movienights.database;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document
public class Movie {

        @Id
        private String id;
        @JsonAlias("Title")
        private String title;
        @JsonAlias("Genre")
        private String genre;
        @JsonAlias("Year")
        private int year;
        @JsonAlias("Poster")
        private String poster;

        private String imdbRating;

        public Movie() {}

        public Movie(String title, String genre, int year, String poster, String imdbRating) {
            this.title = title;
            this.genre = genre;
            this.year = year;
            this.poster = poster;
            this.imdbRating = imdbRating;
        }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }
}
