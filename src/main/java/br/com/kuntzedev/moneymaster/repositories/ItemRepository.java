package br.com.kuntzedev.moneymaster.repositories;

import java.util.List;
import java.util.Optional;

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
			+ "LEFT JOIN FETCH ih.itemPrices")
	List<Item> findAllItemsWithHistory();
}