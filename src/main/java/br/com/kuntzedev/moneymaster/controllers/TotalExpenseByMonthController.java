package br.com.kuntzedev.moneymaster.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.kuntzedev.moneymaster.dtos.FixedExpenseDTO;
import br.com.kuntzedev.moneymaster.dtos.TotalExpenseByMonthDTO;
import br.com.kuntzedev.moneymaster.dtos.VariableExpenseDTO;
import br.com.kuntzedev.moneymaster.services.TotalExpenseByMonthService;

@RestController
@RequestMapping(value = "/totalExpenseByMonths")
public class TotalExpenseByMonthController {

	@Autowired
	private TotalExpenseByMonthService tebmService;
	
	/**
	 * -------------- GETS --------------
	 */
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<TotalExpenseByMonthDTO> findById(@PathVariable Long id) {
		return ResponseEntity.ok().body(tebmService.findById(id));
	}
	
	@GetMapping(value = "/verifyExistence")
	public ResponseEntity<Boolean> verifyExistence() {
		return ResponseEntity.ok().body(tebmService.verifyIfTotalExpenseByMonthExistsThisMonth());
	}
	
	@GetMapping(value = "/findAll/fixedExpenses")
	public ResponseEntity<Page<FixedExpenseDTO>> findAllFixedExpenses(Pageable pageable) {
		return ResponseEntity.ok().body(tebmService.findAllFixedExpenses(pageable));
	}
	
	/**
	 * -------------- POSTS --------------
	 */
	
	@PostMapping(value = "/register/variableExpense")
	public ResponseEntity<Void> insertVariableExpense(@RequestParam("month") int month, @RequestBody VariableExpenseDTO dto) {
		tebmService.insertVariableExpense(month, dto);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping(value = "/register/fixedExpense")
	public ResponseEntity<Void> insertFixedExpense(@RequestBody FixedExpenseDTO... dtos) {
		tebmService.insertFixedExpense(dtos);
		return ResponseEntity.ok().build();
	}
}