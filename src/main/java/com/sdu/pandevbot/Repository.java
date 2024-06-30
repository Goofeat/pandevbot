package com.sdu.pandevbot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface Repository extends JpaRepository<Category, Long> {

	Optional<Category> findByName(String name);

	@Query("SELECT c FROM Category c LEFT JOIN FETCH c.children")
	List<Category> findAllWithChildren();
}
