package br.com.kuntzedev.moneymaster.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.kuntzedev.moneymaster.entities.Wishlist;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long>{
	@Query("SELECT w FROM Wishlist w "
			+ "LEFT JOIN FETCH w.items "
			+ "WHERE w.id = :wishlistId")
	Optional<Wishlist> findWishlistWithItems(Long wishlistId);

	@Query(value = "SELECT w FROM Wishlist w "
			+ "LEFT JOIN FETCH w.items "
			+ "WHERE w.user.id = :userId",
			countQuery = "SELECT count(w) FROM Wishlist w "
					+ "LEFT JOIN w.items "
					+ "WHERE w.user.id = :userId")
	Optional<Page<Wishlist>> findByUserId(Pageable pageable, Long userId);
	
	@Query(value = "SELECT DISTINCT w FROM Wishlist w "
			+ "WHERE (LOWER(w.title) LIKE LOWER(CONCAT('%',:title,'%'))) "
			+ "AND w.user.id = :userId")	
	Page<Wishlist> findByTitleAndAuthenticatedUser(Pageable pageable, String title, Long userId);
}