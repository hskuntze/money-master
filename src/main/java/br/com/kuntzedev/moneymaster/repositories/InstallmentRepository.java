package br.com.kuntzedev.moneymaster.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.kuntzedev.moneymaster.entities.Installment;

@Repository
public interface InstallmentRepository extends JpaRepository<Installment, Long>{
}