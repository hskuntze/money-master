package br.com.kuntzedev.moneymaster.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.kuntzedev.moneymaster.dtos.ExpenseTrackDTO;
import br.com.kuntzedev.moneymaster.entities.ExpenseTrack;
import br.com.kuntzedev.moneymaster.entities.TotalExpenseByMonth;
import br.com.kuntzedev.moneymaster.repositories.ExpenseTrackRepository;
import br.com.kuntzedev.moneymaster.services.exceptions.ResourceAlreadyExistsException;
import br.com.kuntzedev.moneymaster.services.exceptions.ResourceNotFoundException;

@Service
public class ExpenseTrackService {

	@Autowired
	private ExpenseTrackRepository expenseTrackRepository;

	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private TotalExpenseByMonthService tebmService;
	
	private static final String RNFE = "Resource not found in the database.";

	@Transactional(readOnly = true)
	public ExpenseTrackDTO findById(Long id) {
		ExpenseTrack et = expenseTrackRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(RNFE));
		return new ExpenseTrackDTO(et);
	}
	
	@Transactional
	public ExpenseTrackDTO findByUser() {
		Long id = authenticationService.authenticated().getId();
		return findById(id);
	}

	@Transactional(readOnly = true)
	public boolean verifyExistence() {
		Long id = authenticationService.authenticated().getId();

		int rows = expenseTrackRepository.verifyIfExpenseTrackExists(id);

		if (rows <= 0) {
			return false;
		} else {
			return true;
		}
	}

	@Transactional
	public ExpenseTrackDTO createFirstExpenseTrack(ExpenseTrackDTO dto, int businessDayOfPayment) {
		boolean exists = verifyExistence();

		if (!exists) {
			ExpenseTrack et = new ExpenseTrack();

			et.setDayOfSalaryPayment(et.verifyClosestPaymentDay(businessDayOfPayment));
			et.setUser(authenticationService.authenticated());
			et.setExtraIncome(dto.getExtraIncome());
			et.setMonthlyIncome(dto.getMonthlyIncome());
			et.setAnualIncome();
			et.setFluctuationByMonth(0.0f);

			et = expenseTrackRepository.save(et);
			
			et.getTotalExpenseByMonths().add(tebmService.newExpenseByMonth(et));
			return new ExpenseTrackDTO(et);
		} else {
			throw new ResourceAlreadyExistsException("You already have an expense track");
		}
	}
	
	@Transactional
	public ExpenseTrackDTO createNewTotalExpenseByMonth(String date) {
		Long id = authenticationService.authenticated().getId();
		ExpenseTrack et = expenseTrackRepository.findByUserId(id).orElseThrow(() -> new ResourceNotFoundException("Uh oh... something is wrong here! You need an expense track in order to create a new montlhy expense control."));
		
		for(TotalExpenseByMonth tebm : et.getTotalExpenseByMonths()) {
			System.out.println(tebm.getId());
		}
		
		et.getTotalExpenseByMonths().add(tebmService.newExpenseBySpecificMonth(et, LocalDate.parse(date)));
		et = expenseTrackRepository.save(et);
		
		return new ExpenseTrackDTO(et);
	}
	
	@Transactional
	public ExpenseTrackDTO updateExpenseTrack(ExpenseTrackDTO dto) {
		boolean exists = verifyExistence();
		
		if(exists) {
			Long id = authenticationService.authenticated().getExpenseTrack().getId();
			ExpenseTrack et = expenseTrackRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(RNFE));
			
			et.setDayOfSalaryPayment(dto.getDayOfSalaryPayment());
			et.setExtraIncome(dto.getExtraIncome());
			et.setMonthlyIncome(dto.getMonthlyIncome());
			et.setAnualIncome();
			et.setFluctuationByMonth(dto.getFluctuationByMonth());
			
			et = expenseTrackRepository.save(et);
			
			return new ExpenseTrackDTO(et);
		} else {
			throw new ResourceNotFoundException("Something is wrong here. You must already have an expense track in order to update it.");
		}
	}
}