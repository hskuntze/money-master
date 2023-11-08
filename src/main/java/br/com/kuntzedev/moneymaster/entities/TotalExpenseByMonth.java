package br.com.kuntzedev.moneymaster.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tb_total_expense_by_month")
public class TotalExpenseByMonth implements Serializable {
	private static final long serialVersionUID = -3141747452651754505L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDate date;
	private BigDecimal totalExpended;
	private BigDecimal remainingAmount;
	
	@ManyToOne
	@JoinColumn(name = "expense_track_id")
	private ExpenseTrack expenseTrack;
	
	@OneToMany(mappedBy = "totalExpenseByMonth", fetch = FetchType.LAZY)
	private List<VariableExpense> variableExpenses = new ArrayList<>();
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "tb_fixed_expenses_by_month",
				joinColumns = @JoinColumn(name = "tbem_id"),
				inverseJoinColumns = @JoinColumn(name = "fixed_expense_id"))
	private List<FixedExpense> fixedExpenses  = new ArrayList<>();
	
	public TotalExpenseByMonth() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public BigDecimal getTotalExpended() {
		return calculateTotalExpended();
	}

	public void setTotalExpended(BigDecimal totalExpended) {
		this.totalExpended = totalExpended;
	}

	public BigDecimal getRemainingAmount() {
		return calculateRemainingAmount();
	}

	public void setRemainingAmount(BigDecimal remainingAmount) {
		this.remainingAmount = remainingAmount;
	}

	public List<VariableExpense> getVariableExpenses() {
		return variableExpenses;
	}

	public List<FixedExpense> getFixedExpenses() {
		return fixedExpenses;
	}
	
	private BigDecimal calculateTotalExpended() {
		totalExpended = BigDecimal.ZERO;
		
		if(this.variableExpenses.size() != 0) {
			this.variableExpenses.forEach(ve -> {
				totalExpended = totalExpended.add(ve.getPrice());
			});
		}
		
		if(this.fixedExpenses.size() != 0) {
			this.fixedExpenses.forEach(fe -> {
				totalExpended = totalExpended.add(fe.getPrice());
			});
		}
		
		int month = this.date.getMonthValue();
		
		if(this.expenseTrack.getUser().getWishlists().size() != 0) {
			List<Wishlist> list = this.expenseTrack.getUser().getWishlists().stream().filter(wish -> wish.getToBuyAt().getMonthValue() == month).collect(Collectors.toList());
			if(list.size() != 0) {
				list.forEach(el -> {
					totalExpended = totalExpended.add(el.getInstallmentsValue());
				});
			}
		}
		
		return totalExpended;
	}
	
	private BigDecimal calculateRemainingAmount() {
		if(expenseTrack != null) {
			return expenseTrack.getMonthlyIncome().subtract(getTotalExpended());
		} else {
			return BigDecimal.ZERO;
		}
	}
	
	@JsonIgnore
	public ExpenseTrack getExpenseTrack() {
		return expenseTrack;
	}

	public void setExpenseTrack(ExpenseTrack expenseTrack) {
		this.expenseTrack = expenseTrack;
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
		TotalExpenseByMonth other = (TotalExpenseByMonth) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "TotalExpenseByMonth [id=" + id + ", date=" + date + ", totalExpended=" + totalExpended
				+ ", remainingAmount=" + remainingAmount + ", variableExpenses=" + variableExpenses + ", fixedExpenses="
				+ fixedExpenses + "]";
	}
}