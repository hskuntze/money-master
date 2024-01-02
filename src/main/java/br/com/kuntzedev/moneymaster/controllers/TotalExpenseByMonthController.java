package br.com.kuntzedev.moneymaster.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.kuntzedev.moneymaster.dtos.FixedExpenseDTO;
import br.com.kuntzedev.moneymaster.dtos.TotalExpenseByMonthBasicDTO;
import br.com.kuntzedev.moneymaster.dtos.TotalExpenseByMonthDTO;
import br.com.kuntzedev.moneymaster.dtos.VariableExpenseDTO;
import br.com.kuntzedev.moneymaster.dtos.VariableExpenseSumByDateDTO;
import br.com.kuntzedev.moneymaster.dtos.VariableExpenseSumByTitleDTO;
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
	public ResponseEntity<TotalExpenseByMonthDTO> findById(@PathVariable Long id,
			@RequestParam(name = "sortExpenses", defaultValue = "false") boolean sortExpenses,
			@RequestParam(name = "sortExpenseAttribute", defaultValue = "") String sortExpenseAttribute) {
		return ResponseEntity.ok().body(tebmService.findById(id, sortExpenses, sortExpenseAttribute));
	}
	
	@GetMapping(value = "/authenticated")
	public ResponseEntity<Page<TotalExpenseByMonthDTO>> findByAuthenticatedUser(Pageable pageable, 
			@RequestParam(name = "sortExpenses", defaultValue = "false") boolean sortExpenses,
			@RequestParam(name = "sortExpenseAttribute", defaultValue = "") String sortExpenseAttribute) {
		return ResponseEntity.ok().body(tebmService.findByAuthenticatedUser(pageable, sortExpenses, sortExpenseAttribute));
	}
	
	@GetMapping(value = "/verifyExistence")
	public ResponseEntity<Boolean> verifyExistence() {
		return ResponseEntity.ok().body(tebmService.verifyIfTotalExpenseByMonthExistsThisMonth());
	}
	
	@GetMapping(value = "/findAll/fixedExpenses")
	public ResponseEntity<Page<FixedExpenseDTO>> findAllFixedExpenses(Pageable pageable) {
		return ResponseEntity.ok().body(tebmService.findAllFixedExpenses(pageable));
	}
	
	@GetMapping(value = "/findAll/fixedExpenses/validdate")
	public ResponseEntity<Page<FixedExpenseDTO>> findAllFixedExpensesWithValidDate(Pageable pageable) {
		return ResponseEntity.ok().body(tebmService.findAllFixedExpensesWithValidDate(pageable));
	}
	
	@GetMapping(value = "/sumByTitle/{monthId}")
	public ResponseEntity<List<VariableExpenseSumByTitleDTO>> sumByTitle(@PathVariable Long monthId) {
		return ResponseEntity.ok().body(tebmService.sumByTitle(monthId));
	}
	
	@GetMapping(value = "/sumByDate/{monthId}")
	public ResponseEntity<List<VariableExpenseSumByDateDTO>> sumByDate(@PathVariable Long monthId) {
		return ResponseEntity.ok().body(tebmService.sumByDateOfCharge(monthId));
	}
	
	@GetMapping(value = "/basicDataFromAll")
	public ResponseEntity<List<TotalExpenseByMonthBasicDTO>> getBasicData() {
		return ResponseEntity.ok().body(tebmService.getBasicData());
	}
	
	/**
	 * -------------- POSTS --------------
	 */
	
	@PostMapping(value = "/register/variableExpense")
	public ResponseEntity<Void> insertVariableExpense(@RequestParam("year") int year, @RequestParam("month") int month, @RequestBody VariableExpenseDTO dto) {
		tebmService.insertVariableExpense(year, month, dto);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping(value = "/register/variableExpenses")
	public ResponseEntity<Void> insertVariableExpenses(@RequestParam("year") int year, @RequestParam("month") int month, @RequestBody VariableExpenseDTO... dto) {
		tebmService.insertVariableExpense(year, month, dto);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping(value = "/register/fixedExpense")
	public ResponseEntity<Void> insertFixedExpense(@RequestBody FixedExpenseDTO dto) {
		tebmService.insertFixedExpense(dto);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping(value = "/register/fixedExpenses")
	public ResponseEntity<Void> insertFixedExpense(@RequestBody FixedExpenseDTO... dtos) {
		tebmService.insertFixedExpense(dtos);
		return ResponseEntity.ok().build();
	}
	
	/**
	 * -------------- PUTS --------------
	 */
	
	@PutMapping(value = "/update/variableExpense/{id}")
	public ResponseEntity<Void> updateVariableExpense(@PathVariable Long id, @RequestBody VariableExpenseDTO dto) {
		tebmService.updateVariableExpense(id, dto);
		return ResponseEntity.ok().build();
	}
	
	@PutMapping(value = "/update/fixedExpense/{id}")
	public ResponseEntity<Void> updateFixedExpense(@PathVariable Long id, @RequestBody FixedExpenseDTO dto) {
		tebmService.updateFixedExpense(id, dto);
		return ResponseEntity.ok().build();
	}
	
	/**
	 * -------------- DELETES --------------
	 */
	
	@DeleteMapping(value = "/delete/fixedExpense/{id}")
	public ResponseEntity<Void> deleteFixedExpense(@PathVariable Long id) {
		tebmService.deleteFixedExpense(id);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping(value = "/delete/variableExpense/{id}")
	public ResponseEntity<Void> deleteVariableExpense(@PathVariable Long id) {
		tebmService.deleteVariableExpense(id);
		return ResponseEntity.ok().build();
	}
}