package br.com.kuntzedev.moneymaster.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_expense_track")
public class ExpenseTrack implements Serializable {
	private static final long serialVersionUID = -6179938791289338803L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private BigDecimal monthlyIncome;
	private BigDecimal anualIncome;
	private BigDecimal extraIncome;
	private Integer dayOfSalaryPayment;
	private Float fluctuationByMonth;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "expenseTrack")
	private List<TotalExpenseByMonth> totalExpenseByMonths = new ArrayList<>();

	public ExpenseTrack() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getMonthlyIncome() {
		return monthlyIncome;
	}

	public void setMonthlyIncome(BigDecimal monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}

	public BigDecimal getAnualIncome() {
		return anualIncome;
	}

	public void setAnualIncome() {
		this.anualIncome = monthlyIncome.multiply(BigDecimal.valueOf(12)).add(extraIncome);
	}

	public BigDecimal getExtraIncome() {
		return extraIncome;
	}

	public void setExtraIncome(BigDecimal extraIncome) {
		this.extraIncome = extraIncome;
	}

	public Integer getDayOfSalaryPayment() {
		return dayOfSalaryPayment;
	}

	public void setDayOfSalaryPayment(Integer dayOfSalaryPayment) {
		this.dayOfSalaryPayment = dayOfSalaryPayment;
	}

	public Float getFluctuationByMonth() {
		return calculateFluctuation();
	}

	public void setFluctuationByMonth(Float fluctuationByMonth) {
		this.fluctuationByMonth = fluctuationByMonth;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<TotalExpenseByMonth> getTotalExpenseByMonths() {
		return totalExpenseByMonths;
	}
	
	public int verifyClosestPaymentDay(int businessDayOfPayment) {
		LocalDate now = LocalDate.now();
		LocalDate date = now.withDayOfMonth(1);
		int fifthBusinessDay = 0;

		while (fifthBusinessDay < businessDayOfPayment - 1) {
			if (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY) {
				fifthBusinessDay++;
			}

			date = date.plusDays(1);
		}

		return date.getDayOfMonth();
	}
	
	public void addToTotalExpenseByMonth(TotalExpenseByMonth tebm) {
		if(!this.totalExpenseByMonths.contains(tebm)) {
			totalExpenseByMonths.add(tebm);
		}
	}

	private float calculateFluctuation() {
		float percentage = 0.0f;
		int size = totalExpenseByMonths.size();
		
		if(!(totalExpenseByMonths.size() <= 1)) {
			BigDecimal previous = totalExpenseByMonths.get(size - 2).getRemainingAmount();
			BigDecimal current = totalExpenseByMonths.get(size - 1).getRemainingAmount();
				
			BigDecimal difference = current.subtract(previous);
			percentage = difference.divide(previous, 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).floatValue();
		}

	return percentage;

	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExpenseTrack other = (ExpenseTrack) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "ExpenseTrack [id=" + id + ", monthlyIncome=" + monthlyIncome + ", anualIncome=" + anualIncome
				+ ", extraIncome=" + extraIncome + ", dayOfSalaryPayment=" + dayOfSalaryPayment
				+ ", fluctuationByMonth=" + fluctuationByMonth + ", user=" + user + "]";
	}
}