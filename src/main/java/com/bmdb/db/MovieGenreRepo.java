package com.bmdb.db;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bmdb.business.MovieGenre;

public interface MovieGenreRepo extends CrudRepository<MovieGenre, Integer> {

	List<MovieGenre> findAllByGenreId(int id);
}
