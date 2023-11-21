package br.com.kuntzedev.moneymaster.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import br.com.kuntzedev.moneymaster.services.ExpenseTrackService;

@Configuration
@EnableScheduling
public class ExpenseTrackTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExpenseTrackTask.class);

	@Autowired
	private ExpenseTrackService expenseTrackService;
	
	//private static final String TIME_ZONE = "America/Sao_Paulo";
	
	//@Scheduled(cron = "0 0 0 1 * ?")
	//@Scheduled(cron = "0/15 * * * * ?", zone = TIME_ZONE)
	public void createNewTotalExpenseByMonthTask() {
		LOGGER.info("[ETT] - Starting scheduled task.");
		expenseTrackService.createNewTotalExpenseByMonthForThisMonth();
		LOGGER.info("[ETT] - Scheduled task finalized.");
	}
}