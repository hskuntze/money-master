package br.com.kuntzedev.moneymaster.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.kuntzedev.moneymaster.dtos.FixedExpenseDTO;
import br.com.kuntzedev.moneymaster.dtos.TotalExpenseByMonthDTO;
import br.com.kuntzedev.moneymaster.dtos.VariableExpenseDTO;
import br.com.kuntzedev.moneymaster.entities.ExpenseTrack;
import br.com.kuntzedev.moneymaster.entities.FixedExpense;
import br.com.kuntzedev.moneymaster.entities.TotalExpenseByMonth;
import br.com.kuntzedev.moneymaster.entities.VariableExpense;
import br.com.kuntzedev.moneymaster.repositories.FixedExpensesRepository;
import br.com.kuntzedev.moneymaster.repositories.TotalExpenseByMonthRepository;
import br.com.kuntzedev.moneymaster.repositories.VariableExpenseRepository;
import br.com.kuntzedev.moneymaster.services.exceptions.ResourceAlreadyExistsException;
import br.com.kuntzedev.moneymaster.services.exceptions.ResourceNotFoundException;

@Service
public class TotalExpenseByMonthService {
	
	@Autowired
	private TotalExpenseByMonthRepository tebmRepository;
	
	@Autowired
	private FixedExpensesRepository fixedExpensesRepository;
	
	@Autowired
	private VariableExpenseRepository variableExpensesRepository;

	@Autowired
	private AuthenticationService authenticationService;
	
	private static final String RNFE = "Resource not found in the database.";
	
	@Transactional(readOnly = true)
	public TotalExpenseByMonthDTO findById(Long id) {
		TotalExpenseByMonth tebm = tebmRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(RNFE));
		return new TotalExpenseByMonthDTO(tebm);
	}
	
	@Transactional
	public Page<FixedExpenseDTO> findAllFixedExpenses(Pageable pageable) {
		Long userId = authenticationService.authenticated().getId();
		Page<FixedExpense> page = fixedExpensesRepository.findAllByUserId(userId, pageable);
		return page.map(FixedExpenseDTO::new);
	}
	
	@Transactional(readOnly = true)
	public boolean verifyIfTotalExpenseByMonthExistsThisMonth() {
		int month = LocalDate.now().getMonthValue();
		Long id = authenticationService.authenticated().getId();
		
		int rows = tebmRepository.verifyIfTotalExpenseByMonthExists(id, month);
		
		if(rows <= 0) {
			return false;
		} else {
			return true;
		}
	}
	
	@Transactional(readOnly = true)
	public boolean verifyIfTotalExpenseByMonthExistsInSpecificMonth(int month) {
		Long id = authenticationService.authenticated().getId();
		int rows = tebmRepository.verifyIfTotalExpenseByMonthExists(id, month);
		
		if(rows <= 0) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Insere um gasto variável no "TotalExpenseByMonth"
	 * @param month
	 * @param dto
	 */
	@Transactional
	public void insertVariableExpense(int month, VariableExpenseDTO dto) {
		Long id = authenticationService.authenticated().getId();
		TotalExpenseByMonth tebm = tebmRepository.findByMonth(id, month).orElseThrow(() -> new ResourceNotFoundException(RNFE));
		
		VariableExpense ve = new VariableExpense();
		variableExpensesDtoToEntity(ve, dto);
		ve.setTotalExpenseByMonth(tebm);
		ve = variableExpensesRepository.save(ve);
		
		tebm.getVariableExpenses().add(ve);
		tebmRepository.save(tebm);
	}
	
	/**
	 * Cria um gasto fixo e relaciona ele com o "TotalExpenseByMonth" atual.
	 * @param dtos
	 */
	@Transactional
	public void insertFixedExpense(FixedExpenseDTO... dtos) {
		int month = LocalDate.now().getMonthValue();
		Long id = authenticationService.authenticated().getId();
		
		TotalExpenseByMonth tebm = tebmRepository.findByMonth(id, month).orElseThrow(() -> new ResourceNotFoundException(RNFE));
		for(FixedExpenseDTO dto : dtos) {
			Optional<FixedExpense> opt = fixedExpensesRepository.findByTitle(dto.getTitle());
			
			if(opt.isPresent()) {
				boolean contains = tebm.getFixedExpenses().contains(opt.get());
				if(!contains) {
					tebm.getFixedExpenses().add(opt.get());
				}
			} else {
				FixedExpense fe = new FixedExpense();
				fixedExpensesDtoToEntity(fe, dto);
				fe = fixedExpensesRepository.save(fe);
				
				tebm.getFixedExpenses().add(fe);
			}
		}
		
		tebmRepository.save(tebm);
	}
	
	/**
	 * Esta função diz respeito a criação de um novo "TotalExpenseByMonth" completamente zerado.
	 * Se aplica ao momento da criação de um "ExpenseTrack".
	 * @param et
	 * @return
	 */
	@Transactional
	public TotalExpenseByMonth newExpenseByMonth(ExpenseTrack et) {
		TotalExpenseByMonth fresh = new TotalExpenseByMonth();
		
		fresh.setDate(LocalDate.now());
		fresh.setRemainingAmount(BigDecimal.ZERO);
		fresh.setTotalExpended(BigDecimal.ZERO);
		fresh.setExpenseTrack(et);
		
		tebmRepository.save(fresh);
		
		return fresh;
	}
	
	/**
	 * Cria um "TotalExpenseByMonth" para o mês atual. O método avalia a data final dos "FixedExpense's"
	 * do mês anterior. Se determinado "FixedExpense" tem uma data final maior que a data atual então ele
	 * ainda é válido e é incluído no "TotalExpenseByMonth" criado.
	 * @param et
	 * @return
	 */
	@Transactional
	public TotalExpenseByMonth newExpenseForActualMonth(ExpenseTrack et) {
		boolean exists = verifyIfTotalExpenseByMonthExistsThisMonth();
		
		if(!exists) {
			int thisMonth = LocalDate.now().getMonthValue();
			LocalDate today = LocalDate.now();
			
			Long userId = authenticationService.authenticated().getId();
			TotalExpenseByMonth lastMonth = tebmRepository.findByMonth(userId, thisMonth - 1).orElseThrow(() -> new ResourceNotFoundException(RNFE));
			
			BigDecimal totalExpendedByFixedExpenses = BigDecimal.ZERO;
			
			TotalExpenseByMonth forThisMonth = new TotalExpenseByMonth();
			
			forThisMonth.setDate(today);
			forThisMonth.setRemainingAmount(BigDecimal.ZERO);
			forThisMonth.setExpenseTrack(et);
			
			for(FixedExpense fe : lastMonth.getFixedExpenses()) {
				if(fe.getBeginOfExpense().isBefore(today) && fe.getEndOfExpense().isAfter(today)) {
					forThisMonth.getFixedExpenses().add(fe);
					totalExpendedByFixedExpenses = totalExpendedByFixedExpenses.add(fe.getPrice());
				}
			}
			
			forThisMonth.setTotalExpended(totalExpendedByFixedExpenses);
			
			tebmRepository.save(forThisMonth);
			
			return forThisMonth;
		} else {
			throw new ResourceAlreadyExistsException("You can't create a monthly expense control for this specific month because one already exists!"); 
		}
	}
	
	/**
	 * Cria um "TotalExpenseByMonth" para uma data específica.
	 * @param et
	 * @param date
	 * @return
	 * 
	 * TODO: alguma solução que busque os gastos fixos e também insira-os neste TEBM
	 */
	@Transactional
	public TotalExpenseByMonth newExpenseBySpecificMonth(ExpenseTrack et, LocalDate date) {
		boolean exists = verifyIfTotalExpenseByMonthExistsInSpecificMonth(date.getMonthValue());
		
		if(!exists) {
			TotalExpenseByMonth tebm = new TotalExpenseByMonth();
			
			tebm.setDate(date);
			tebm.setRemainingAmount(BigDecimal.ZERO);
			tebm.setTotalExpended(BigDecimal.ZERO);
			tebm.setExpenseTrack(et);
			
			tebmRepository.save(tebm);
			
			return tebm;
		} else {
			throw new ResourceAlreadyExistsException("You can't create a monthly expense control for this specific month because one already exists!");
		}
	}
	
	private void variableExpensesDtoToEntity(VariableExpense entity, VariableExpenseDTO dto) {
		entity.setPrice(dto.getPrice());
		entity.setDateOfCharge(dto.getDateOfCharge());
		entity.setTitle(dto.getTitle());
	}
	
	private void fixedExpensesDtoToEntity(FixedExpense entity, FixedExpenseDTO dto) {
		entity.setPrice(dto.getPrice());
		entity.setDayOfCharge(dto.getDayOfCharge());
		entity.setTitle(dto.getTitle());
		entity.setBeginOfExpense(dto.getBeginOfExpense());
		entity.setEndOfExpense(dto.getEndOfExpense());
	}
}