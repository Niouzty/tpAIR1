package fr.uit.univparis8.tpair.tpair1.repository;

import fr.uit.univparis8.tpair.tpair1.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByLabel(String label);
}
