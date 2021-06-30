package com.bmdb.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.bmdb.business.MovieCollection;
import com.bmdb.business.User;
import com.bmdb.db.MovieCollectionRepo;
import com.bmdb.db.UserRepo;

@CrossOrigin
@RestController
@RequestMapping("/api/movie-collections")
public class MovieCollectionController {

	@Autowired
	private MovieCollectionRepo movieCollectionRepo;
	@Autowired
	private UserRepo userRepo;

	@GetMapping("/")
	public Iterable<MovieCollection> getAll() {
		return movieCollectionRepo.findAll();
	}

	@GetMapping("/{id}")
	public Optional<MovieCollection> get(@PathVariable Integer id) {
		return movieCollectionRepo.findById(id);
	}

	@PostMapping("/")
	public MovieCollection add(@RequestBody MovieCollection movieCollection) {
		MovieCollection mc = movieCollectionRepo.save(movieCollection);
		if (recalculateCollectionValue(movieCollection.getUser())) {
			// Successful Recalculate
		} else {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Exception caught during movieCollection post.");
		}
		return mc;
	}

	@PutMapping("/")
	public MovieCollection update(@RequestBody MovieCollection movieCollection) {
		MovieCollection mc = movieCollectionRepo.save(movieCollection);
		if (recalculateCollectionValue(movieCollection.getUser())) {
			// Successful Recalculate
		} else {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Exception caught during movieCollection post.");
		}
		return mc;
	}

	@DeleteMapping("/{id}")
	public Optional<MovieCollection> delete(@PathVariable int id) {
		Optional<MovieCollection> movieCollection = movieCollectionRepo.findById(id);
		if (movieCollection.isPresent()) {
			try {
				movieCollectionRepo.deleteById(id);
				if (!recalculateCollectionValue(movieCollection.get().getUser())) {
					throw new Exception("Issue recalculating collection value on delete.");
				}
			} catch (DataIntegrityViolationException dive) {
				// Catch Dive when MovieCollection exist as fk on another table
				System.err.println(dive.getRootCause().getMessage());
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
						"Foreign Key Constraint Issue - movieCollection id: " + id + " is " + "referred to elsewhere");
			} catch (Exception e) {
				e.printStackTrace();
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
						"Exception caught during movieCollection delete.");
			}
		} else {
			System.err.println("MovieCollection delete error");
		}
		return movieCollection;
	}

	// Custom Queries

	// On Movie Collection Post, Put, Delete, getAll MovieCollections
	// for the user and loop through to calculate new collection value total

	private boolean recalculateCollectionValue(User user) {
		boolean success = false;
		try {
			// Get all movieCollection for user
			List<MovieCollection> mcs = movieCollectionRepo.findAllByUserId(user.getId());

			double cv = 0.0;
			for (MovieCollection mc : mcs) {
				cv += mc.getPurchasePrice();
			}
			// Set new collectionValue in the user
			user.setCollectionValue(cv);
			userRepo.save(user);
			success = true;
		} catch (Exception e) {
			System.err.println("Error Saving new collection value.");
			e.printStackTrace();
		}

		return success;

	}

}
