package br.com.kuntzedev.moneymaster.controllers;

import java.math.BigDecimal;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.kuntzedev.moneymaster.dtos.ExpenseTrackDTO;
import br.com.kuntzedev.moneymaster.services.ExpenseTrackService;

@RestController
@RequestMapping(value = "/expenseTracks")
public class ExpenseTrackController {

	@Autowired
	private ExpenseTrackService expenseTrackService;
	
	/**
	 * -------------- GETS --------------
	 */
	
	@GetMapping
	public ResponseEntity<ExpenseTrackDTO> findByUser() {
		return ResponseEntity.ok().body(expenseTrackService.findByUser());
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<ExpenseTrackDTO> findById(@PathVariable Long id) {
		return ResponseEntity.ok().body(expenseTrackService.findById(id));
	}
	
	@GetMapping(value = "/verifyExistence")
	public ResponseEntity<Boolean> verifyExistence() {
		return ResponseEntity.ok().body(expenseTrackService.verifyExistence());
	}
	
	/**
	 * -------------- POSTS --------------
	 */
	
	@PostMapping(value = "/register")
	public ResponseEntity<ExpenseTrackDTO> createExpenseTrack(@RequestBody ExpenseTrackDTO dto, @RequestParam("businessDayOfPayment") int businessDayOfPayment) {
		dto = expenseTrackService.createFirstExpenseTrack(dto, businessDayOfPayment);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	@PostMapping(value = "/register/totalExpenseByMonth")
	public ResponseEntity<ExpenseTrackDTO> createTotalExpenseByMonth(@RequestParam("date") String date) {
		ExpenseTrackDTO dto = expenseTrackService.createNewTotalExpenseByMonth(date);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	@PostMapping(value = "/register/totalExpenseForThisMonth")
	public ResponseEntity<ExpenseTrackDTO> createTotalExpenseByMonthForThisMonth() {
		ExpenseTrackDTO dto = expenseTrackService.createNewTotalExpenseByMonthForThisMonth();
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	/**
	 * -------------- PUTS --------------
	 */
	
	@PutMapping(value = "/update")
	public ResponseEntity<ExpenseTrackDTO> updateExpenseTrack(@RequestBody ExpenseTrackDTO dto) {
		return ResponseEntity.ok().body(expenseTrackService.updateExpenseTrack(dto));
	}
	
	@PutMapping(value = "/update/salaryIncome")
	public ResponseEntity<ExpenseTrackDTO> updateSalaryIncome(@RequestParam("value") BigDecimal value) {
		return ResponseEntity.ok().body(expenseTrackService.updateSalaryIncome(value));
	}
}