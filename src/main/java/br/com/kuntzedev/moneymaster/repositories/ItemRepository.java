package br.com.kuntzedev.moneymaster.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.kuntzedev.moneymaster.entities.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
	@Query("SELECT i FROM Item i "
			+ "LEFT JOIN FETCH i.itemHistory ih "
			+ "LEFT JOIN FETCH ih.itemPrices ips "
			+ "WHERE i.id = :id")
	Optional<Item> findItemWithHistory(Long id);
	
	@Query(value = "SELECT DISTINCT i FROM Item i "
			+ "LEFT JOIN FETCH i.itemHistory ih "
			+ "LEFT JOIN FETCH ih.itemPrices",
			countQuery = "SELECT DISTINCT i FROM Item i "
					+ "LEFT JOIN i.itemHistory ih "
					+ "LEFT JOIN ih.itemPrices")
	Page<Item> findAllItemsWithHistory(Pageable pageable);
}