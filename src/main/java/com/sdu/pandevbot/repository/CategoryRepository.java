package com.sdu.pandevbot.repository;

import com.sdu.pandevbot.repository.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	Optional<Category> findByName(String name);
	Optional<Category> findByNameAndUserId(String name, Long userId);
	List<Category> findAllByUserId(Long userId);

}
