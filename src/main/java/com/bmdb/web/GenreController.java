package com.bmdb.web;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.bmdb.business.Genre;
import com.bmdb.db.GenreRepo;

@CrossOrigin
@RestController
@RequestMapping("/api/genres")
public class GenreController {

	@Autowired
	private GenreRepo genreRepo;

	@GetMapping("/")
	public Iterable<Genre> getAll() {
		return genreRepo.findAll();
	}

	@GetMapping("/{id}")
	public Optional<Genre> get(@PathVariable Integer id) {
		return genreRepo.findById(id);
	}

	@PostMapping("/")
	public Genre add(@RequestBody Genre genre) {
		return genreRepo.save(genre);
	}

	@PutMapping("/")
	public Genre update(@RequestBody Genre genre) {
		return genreRepo.save(genre);
	}

	@DeleteMapping("/{id}")
	public Optional<Genre> delete(@PathVariable int id) {
		Optional<Genre> genre = genreRepo.findById(id);
		if (genre.isPresent()) {
			try {
				genreRepo.deleteById(id);
			} catch (DataIntegrityViolationException dive) {
				// Catch Dive when Genre exist as fk on another table
				System.err.println(dive.getRootCause().getMessage());
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
						"Foreign Key Constraint Issue - genre id: " + id + " is " + "referred to elsewhere");
			} catch (Exception e) {
				e.printStackTrace();
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
						"Exception caught during genre delete.");
			}
			genreRepo.deleteById(id);
		} else {
			System.err.println("Genre delete error");
		}
		return genre;
	}

	// Custom Queries

}
