package com.sdu.pandevbot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	Optional<Category> findByName(String name);
	Optional<Category> findByNameAndUserId(String name, Long userId);
	List<Category> findAllByUserId(Long userId);

	@Query("SELECT c FROM Category c LEFT JOIN FETCH c.children")
	List<Category> findAllWithChildren();
}
