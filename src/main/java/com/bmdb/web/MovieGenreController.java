package com.bmdb.web;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.bmdb.business.Genre;
import com.bmdb.business.MovieGenre;
import com.bmdb.db.GenreRepo;
import com.bmdb.db.MovieGenreRepo;

@CrossOrigin
@RestController
@RequestMapping("/api/movie-genre")
public class MovieGenreController {

	@Autowired
	private MovieGenreRepo movieGenreRepo;
	@Autowired
	private GenreRepo genreRepo;

	@GetMapping("/")
	public Iterable<MovieGenre> getAll() {
		return movieGenreRepo.findAll();
	}

	@GetMapping("/{id}")
	public Optional<MovieGenre> get(@PathVariable Integer id) {
		return movieGenreRepo.findById(id);
	}

	@PostMapping("/")
	public MovieGenre add(@RequestBody MovieGenre movieGenre) {
		return movieGenreRepo.save(movieGenre);
	}

	@PutMapping("/")
	public MovieGenre update(@RequestBody MovieGenre movieGenre) {
		return movieGenreRepo.save(movieGenre);
	}

	@DeleteMapping("/{id}")
	public Optional<MovieGenre> delete(@PathVariable int id) {
		Optional<MovieGenre> movieGenre = movieGenreRepo.findById(id);
		if (movieGenre.isPresent()) {
			try {
				movieGenreRepo.deleteById(id);
			} catch (DataIntegrityViolationException dive) {
				// Catch Dive when MovieGenre exist as fk on another table
				System.err.println(dive.getRootCause().getMessage());
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
						"Foreign Key Constraint Issue - movieGenre id: " + id + " is " + "referred to elsewhere");
			} catch (Exception e) {
				e.printStackTrace();
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
						"Exception caught during movieGenre delete.");
			}
			movieGenreRepo.deleteById(id);
		} else {
			System.err.println("MovieGenre delete error");
		}
		return movieGenre;
	}

	// Custom Queries
	
	@GetMapping("/genre")
	public Iterable<MovieGenre> getAllByGenre(@RequestParam String genreStr) {
		Genre genre = genreRepo.findByName(genreStr);
		return movieGenreRepo.findAllByGenreId(genre.getId());
	}

}
