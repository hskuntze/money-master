package br.com.kuntzedev.moneymaster.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.kuntzedev.moneymaster.dtos.FixedExpenseDTO;
import br.com.kuntzedev.moneymaster.dtos.TotalExpenseByMonthBasicDTO;
import br.com.kuntzedev.moneymaster.dtos.TotalExpenseByMonthDTO;
import br.com.kuntzedev.moneymaster.dtos.VariableExpenseDTO;
import br.com.kuntzedev.moneymaster.dtos.VariableExpenseSumByDateDTO;
import br.com.kuntzedev.moneymaster.dtos.VariableExpenseSumByTitleDTO;
import br.com.kuntzedev.moneymaster.entities.ExpenseTrack;
import br.com.kuntzedev.moneymaster.entities.FixedExpense;
import br.com.kuntzedev.moneymaster.entities.TotalExpenseByMonth;
import br.com.kuntzedev.moneymaster.entities.User;
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
	public TotalExpenseByMonthDTO findById(Long id, boolean sortExpenses, String sortExpenseAttribute) {
		TotalExpenseByMonth tebm = tebmRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(RNFE));

		if (sortExpenses) {
			switch (sortExpenseAttribute) {
				case "price":
					tebm.getVariableExpenses().sort(Comparator.comparing(VariableExpense::getPrice));
					break;
				case "title":
					tebm.getVariableExpenses().sort(Comparator.comparing(VariableExpense::getTitle));
					break;
				case "dateOfCharge":
					tebm.getVariableExpenses().sort(Comparator.comparing(VariableExpense::getDateOfCharge));
					break;
				default:
					break;
			}
		}

		return new TotalExpenseByMonthDTO(tebm);
	}

	@Transactional(readOnly = true)
	public Page<TotalExpenseByMonthDTO> findByAuthenticatedUser(Pageable pageable, boolean sortExpenses,
			String sortExpenseAttribute) {
		User user = authenticationService.authenticated();
		Page<TotalExpenseByMonth> page = tebmRepository.findByUser(pageable, user.getId());

		if (sortExpenses) {
			switch (sortExpenseAttribute) {
				case "price":
					page.forEach(p -> p.getVariableExpenses().sort(Comparator.comparing(VariableExpense::getPrice)));
					break;
				case "title":
					page.forEach(p -> p.getVariableExpenses().sort(Comparator.comparing(VariableExpense::getTitle)));
					break;
				case "dateOfCharge":
					page.forEach(p -> p.getVariableExpenses().sort(Comparator.comparing(VariableExpense::getDateOfCharge)));
					break;
				default:
					break;
			}
		}

		return page.map(TotalExpenseByMonthDTO::new);
	}

	@Transactional
	public Page<FixedExpenseDTO> findAllFixedExpenses(Pageable pageable) {
		User user = authenticationService.authenticated();
		List<TotalExpenseByMonth> tebms = user.getExpenseTrack().getTotalExpenseByMonths();
		Long lastTebm = tebms.get(tebms.size() - 1).getId();
		Page<FixedExpense> page = fixedExpensesRepository.findAllByUserId(user.getId(), lastTebm, pageable);
		return page.map(FixedExpenseDTO::new);
	}
	
	@Transactional
	public Page<FixedExpenseDTO> findAllFixedExpensesWithValidDate(Pageable pageable) {
		User user = authenticationService.authenticated();
		Page<FixedExpense> page = fixedExpensesRepository.findAllByUserIdWithValidDate(user.getId(), pageable);
		return page.map(FixedExpenseDTO::new);
	}

	/**
	 * Verifica se um TotalExpenseByMonth existe no mês atual
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean verifyIfTotalExpenseByMonthExistsThisMonth() {
		int month = LocalDate.now().getMonthValue();
		Long id = authenticationService.authenticated().getId();

		int rows = tebmRepository.verifyIfTotalExpenseByMonthExists(id, month);

		if (rows <= 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Verifica se um TotalExpenseByMonth existe para determinado mês
	 * 
	 * @param month
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean verifyIfTotalExpenseByMonthExistsInSpecificMonth(int month) {
		Long id = authenticationService.authenticated().getId();
		int rows = tebmRepository.verifyIfTotalExpenseByMonthExists(id, month);

		if (rows <= 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Esta função diz respeito a criação de um novo "TotalExpenseByMonth"
	 * completamente zerado. Se aplica ao momento da criação de um "ExpenseTrack".
	 * 
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
	 * Insere um gasto variável no "TotalExpenseByMonth"
	 * 
	 * @param month
	 * @param dto
	 */
	@Transactional
	public void insertVariableExpense(int year, int month, VariableExpenseDTO dto) {
		Long id = authenticationService.authenticated().getId();
		TotalExpenseByMonth tebm = tebmRepository.findByMonth(id, month, year)
				.orElseThrow(() -> new ResourceNotFoundException(RNFE));

		VariableExpense ve = new VariableExpense();
		variableExpensesDtoToEntity(ve, dto);
		ve.setTotalExpenseByMonth(tebm);
		ve = variableExpensesRepository.save(ve);

		tebm.getVariableExpenses().add(ve);
		tebmRepository.save(tebm);
	}

	/**
	 * Insere vários gastos variáveis no "TotalExpenseByMonth"
	 * 
	 * @param month
	 * @param dto
	 */
	@Transactional
	public void insertVariableExpense(int year, int month, VariableExpenseDTO... dtos) {
		Long id = authenticationService.authenticated().getId();
		TotalExpenseByMonth tebm = tebmRepository.findByMonth(id, month, year)
				.orElseThrow(() -> new ResourceNotFoundException(RNFE));

		for (VariableExpenseDTO v : dtos) {
			VariableExpense ve = new VariableExpense();
			variableExpensesDtoToEntity(ve, v);
			ve.setTotalExpenseByMonth(tebm);
			ve = variableExpensesRepository.save(ve);

			tebm.getVariableExpenses().add(ve);
		}

		tebmRepository.save(tebm);
	}

	/**
	 * Cria um gasto fixo e o relaciona com o "TotalExpenseByMonth" atual.
	 * 
	 * @param dtos
	 */
	@Transactional
	public void insertFixedExpense(FixedExpenseDTO dto) {
		int month = LocalDate.now().getMonthValue();
		int year = LocalDate.now().getYear();
		Long id = authenticationService.authenticated().getId();

		TotalExpenseByMonth tebm = tebmRepository.findByMonth(id, month, year)
				.orElseThrow(() -> new ResourceNotFoundException(RNFE));
		Optional<List<FixedExpense>> opt = fixedExpensesRepository.findManyByTitle(dto.getTitle());

		if (!opt.get().isEmpty()) {
			for (FixedExpense fe : opt.get()) {
				boolean contains = tebm.getFixedExpenses().contains(fe);
				if (!contains) {
					tebm.getFixedExpenses().add(fe);
				}
			}
		} else {
			FixedExpense fe = new FixedExpense();
			fixedExpensesDtoToEntity(fe, dto);
			fe = fixedExpensesRepository.save(fe);

			tebm.getFixedExpenses().add(fe);
		}

		tebmRepository.save(tebm);
	}

	/**
	 * Cria uma série de gastos fixos e os relaciona com o "TotalExpenseByMonth"
	 * atual.
	 * 
	 * @param dtos
	 */
	@Transactional
	public void insertFixedExpense(FixedExpenseDTO... dtos) {
		int month = LocalDate.now().getMonthValue();
		int year = LocalDate.now().getYear();
		Long id = authenticationService.authenticated().getId();

		TotalExpenseByMonth tebm = tebmRepository.findByMonth(id, month, year)
				.orElseThrow(() -> new ResourceNotFoundException(RNFE));
		for (FixedExpenseDTO dto : dtos) {
			Optional<List<FixedExpense>> opt = fixedExpensesRepository.findManyByTitle(dto.getTitle());

			if (!opt.get().isEmpty()) {
				for (FixedExpense fe : opt.get()) {
					boolean contains = tebm.getFixedExpenses().contains(fe);
					if (!contains) {
						tebm.getFixedExpenses().add(fe);
					}
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
	 * Operação responsável por atualizar um objeto "VariableExpense"
	 * 
	 * @param id
	 * @param dto
	 */
	@Transactional
	public void updateVariableExpense(Long id, VariableExpenseDTO dto) {
		VariableExpense ve = variableExpensesRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(RNFE));
		variableExpensesDtoToEntity(ve, dto);
		ve = variableExpensesRepository.save(ve);
	}

	/**
	 * Operação responsável por atualizar um objeto "FixedExpense"
	 * 
	 * @param id
	 * @param dto
	 */
	@Transactional
	public void updateFixedExpense(Long id, FixedExpenseDTO dto) {
		FixedExpense fe = fixedExpensesRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(RNFE));
		fixedExpensesDtoToEntity(fe, dto);
		fe = fixedExpensesRepository.save(fe);
	}

	/**
	 * Cria um "TotalExpenseByMonth" para o mês atual. O método avalia a data final
	 * dos "FixedExpense's" do mês anterior. Se determinado "FixedExpense" tem uma
	 * data final maior que a data atual então ele ainda é válido e é incluído no
	 * "TotalExpenseByMonth" criado.
	 * 
	 * @param et
	 * @return
	 */
	@Transactional
	public TotalExpenseByMonth newExpenseForActualMonth(ExpenseTrack et) {
		boolean exists = verifyIfTotalExpenseByMonthExistsThisMonth();

		if (!exists) {
			int thisMonth = LocalDate.now().getMonthValue();
			LocalDate today = LocalDate.now();
			int year = LocalDate.now().getYear();

			Long userId = authenticationService.authenticated().getId();
			
			/**
			 * Se thisMonth == 1:	
			 * 	- Significa que é janeiro, 13 - 1 = 12 -> dezembro
			 */
			if(thisMonth == 1) {
				thisMonth = 13;
			}
			
			TotalExpenseByMonth lastMonth = tebmRepository.findByMonth(userId, thisMonth - 1, year)
					.orElseThrow(() -> new ResourceNotFoundException(RNFE));

			BigDecimal totalExpendedByFixedExpenses = BigDecimal.ZERO;

			TotalExpenseByMonth forThisMonth = new TotalExpenseByMonth();

			forThisMonth.setDate(today);
			forThisMonth.setRemainingAmount(BigDecimal.ZERO);
			forThisMonth.setExpenseTrack(et);

			for (FixedExpense fe : lastMonth.getFixedExpenses()) {
				if (fe.getBeginOfExpense().isBefore(today) && fe.getEndOfExpense().isAfter(today)) {
					forThisMonth.getFixedExpenses().add(fe);
					totalExpendedByFixedExpenses = totalExpendedByFixedExpenses.add(fe.getPrice());
				}
			}

			forThisMonth.setTotalExpended(totalExpendedByFixedExpenses);

			tebmRepository.save(forThisMonth);

			return forThisMonth;
		} else {
			throw new ResourceAlreadyExistsException(
					"You can't create a monthly expense control for this specific month because one already exists!");
		}
	}
	
	/**
	 * Função responsável por realizar a soma de todos os gastos variáveis pelo nome
	 * 
	 * @param monthId
	 * @return
	 */
	public List<VariableExpenseSumByTitleDTO> sumByTitle(Long monthId) {
		return variableExpensesRepository.sumByTitle(monthId);
	}
	
	/**
	 * Função responsável por realizar a soma de todos os gastos variáveis pela data de cobrança
	 * 
	 * @param monthId
	 * @return
	 */
	public List<VariableExpenseSumByDateDTO> sumByDateOfCharge(Long monthId) {
		return variableExpensesRepository.sumByDateOfCharge(monthId);
	}
	
	public List<TotalExpenseByMonthBasicDTO> getBasicData() {
		return tebmRepository.getBasicData();
	}

	/**
	 * Cria um "TotalExpenseByMonth" para uma data específica.
	 * 
	 * @param et
	 * @param date
	 * @return
	 * 
	 *         TODO: alguma solução que busque os gastos fixos e também insira-os
	 *         neste TEBM
	 */
	@Transactional
	public TotalExpenseByMonth newExpenseBySpecificMonth(ExpenseTrack et, LocalDate date) {
		boolean exists = verifyIfTotalExpenseByMonthExistsInSpecificMonth(date.getMonthValue());

		if (!exists) {
			TotalExpenseByMonth tebm = new TotalExpenseByMonth();

			tebm.setDate(date);
			tebm.setRemainingAmount(BigDecimal.ZERO);
			tebm.setTotalExpended(BigDecimal.ZERO);
			tebm.setExpenseTrack(et);

			tebmRepository.save(tebm);

			return tebm;
		} else {
			throw new ResourceAlreadyExistsException(
					"You can't create a monthly expense control for this specific month because one already exists!");
		}
	}

	/**
	 * Delete um FixedExpense baseado no identificador
	 * 
	 * @param id
	 */
	@Transactional
	public void deleteFixedExpense(Long id) {
		Optional<FixedExpense> opt = fixedExpensesRepository.findById(id);

		if (opt.isPresent()) {
			fixedExpensesRepository.deleteFixedExpenseInAuxTable(id);
			fixedExpensesRepository.deleteById(id);
		} else {
			throw new ResourceNotFoundException(RNFE);
		}
	}

	/**
	 * Delete um VariableExpense baseado no identificador
	 * 
	 * @param id
	 */
	public void deleteVariableExpense(Long id) {
		Optional<VariableExpense> opt = variableExpensesRepository.findById(id);

		if (opt.isPresent()) {
			variableExpensesRepository.deleteById(id);
		} else {
			throw new ResourceNotFoundException(RNFE);
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