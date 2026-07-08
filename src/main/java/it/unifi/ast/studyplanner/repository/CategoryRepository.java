package it.unifi.ast.studyplanner.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.unifi.ast.studyplanner.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	Optional<Category> findByName(String name);
}