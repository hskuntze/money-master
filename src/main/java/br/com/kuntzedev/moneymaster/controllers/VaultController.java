package br.com.kuntzedev.moneymaster.controllers;

import java.math.BigDecimal;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.kuntzedev.moneymaster.dtos.VaultDTO;
import br.com.kuntzedev.moneymaster.dtos.VaultSavingsResponseDTO;
import br.com.kuntzedev.moneymaster.services.VaultService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@RestController
@RequestMapping(value = "/vaults")
public class VaultController {

	@Autowired
	private VaultService vaultService;
	
	private MeterRegistry meterRegistry;
	
	public VaultController(MeterRegistry meterRegistry) {
		this.meterRegistry = meterRegistry;
	}
	
	/**
	 * -------------- GETS --------------
	 */
	
	/**
	 * Resgata um Vault baseado no ID
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<VaultDTO> findById(@PathVariable Long id) {
		return ResponseEntity.ok().body(vaultService.findVaultById(id));
	}
	
	@GetMapping(value = "/authenticated")
	public ResponseEntity<VaultDTO> findByAuthenticatedUser() {
		return ResponseEntity.ok().body(vaultService.findByAuthenticatedUser());
	}
	
	/**
	 * -------------- POSTS --------------
	 */
	
	/**
	 * Registra um Vault no banco de dados (não será vinculado a um usuário)
	 * @param dto
	 * @return
	 */
	@PostMapping(value = "/register")
	public ResponseEntity<VaultDTO> insert(@RequestBody VaultDTO dto) {
		dto = vaultService.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	/**
	 * -------------- PUTS --------------
	 */
	
	/**
	 * Atualiza um Vault baseado no ID
	 * @param id
	 * @param dto
	 * @return
	 */
	@PutMapping(value = "/update/{id}")
	public ResponseEntity<VaultDTO> update(@PathVariable Long id, @RequestBody VaultDTO dto) {
		Counter counter = Counter.builder("savings_counter").tag("savings_counter", "savings")
				.register(meterRegistry);
		
		counter.increment(1.0);
		
		return ResponseEntity.ok().body(vaultService.update(id, dto));
	}
	
	/**
	 * -------------- DELETES --------------
	 */
	
	/**
	 * Deleta um Vault baseado no ID
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		vaultService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
	/**
	 * Deleta um Vault baseado no ID do usuário vinculado
	 * @param userId
	 * @return
	 */
	@DeleteMapping(value = "/delete/byUser/{userId}")
	public ResponseEntity<Void> deleteByUserId(@PathVariable Long userId) {
		vaultService.deleteByUserId(userId);
		return ResponseEntity.noContent().build();
	}
	
	/**
	 * -------------- PATCHES --------------
	 */
	
	/**
	 * Função que incrementa o valor do atributo "Savings"
	 * @param id
	 * @param value
	 * @return
	 */
	@PatchMapping(value = "/increaseSavings")
	public ResponseEntity<VaultSavingsResponseDTO> increaseSavings(@RequestParam("value") BigDecimal value) {
		return ResponseEntity.ok().body(vaultService.increaseSavingsValue(value));
	}
	
	/**
	 * Função que reduz o valor do atributo "Savings"
	 * @param id
	 * @param value
	 * @return
	 */
	@PatchMapping(value = "/reduceSavings")
	public ResponseEntity<VaultDTO> reduceSavings(@RequestParam("value") BigDecimal value) {
		return ResponseEntity.ok().body(vaultService.reduceSavingsValue(value));
	}
	
	/**
	 * Função que incrementa o valor do atributo "onWallet"
	 * @param id
	 * @param value
	 * @return
	 */
	@PatchMapping(value = "/increaseWallet")
	public ResponseEntity<VaultDTO> increaseWallet(@RequestParam("value") BigDecimal value) {
		return ResponseEntity.ok().body(vaultService.increaseWalletValue(value));
	}
	
	/**
	 * Função que reduz o valor do atributo "onWallet"
	 * @param id
	 * @param value
	 * @return
	 */
	@PatchMapping(value = "/reduceWallet")
	public ResponseEntity<VaultDTO> reduceWallet(@RequestParam("value") BigDecimal value) {
		return ResponseEntity.ok().body(vaultService.reduceWalletValue(value));
	}
	
	/**
	 * Função que incrementa o valor do atributo "allowedToSpend"
	 * @param id
	 * @param value
	 * @return
	 */
	@PatchMapping(value = "/increaseAllowedToSpend")
	public ResponseEntity<VaultDTO> increaseAllowedToSpend(@RequestParam("value") BigDecimal value) {
		return ResponseEntity.ok().body(vaultService.increaseAllowedToSpendValue(value));
	}
	
	/**
	 * Função que reduz o valor do atributo "allowedToSpend"
	 * @param id
	 * @param value
	 * @return
	 */
	@PatchMapping(value = "/reduceAllowedToSpend")
	public ResponseEntity<VaultDTO> reduceAllowedToSpend(@RequestParam("value") BigDecimal value) {
		return ResponseEntity.ok().body(vaultService.reduceAllowedToSpendValue(value));
	}
}