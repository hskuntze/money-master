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
import br.com.kuntzedev.moneymaster.services.VaultService;

@RestController
@RequestMapping(value = "/vaults")
public class VaultController {

	@Autowired
	private VaultService vaultService;
	
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
	@PatchMapping(value = "/increaseSavings/{id}")
	public ResponseEntity<VaultDTO> increaseSavings(@PathVariable Long id, @RequestParam("value") BigDecimal value) {
		return ResponseEntity.ok().body(vaultService.increaseSavingsValue(id, value));
	}
	
	/**
	 * Função que reduz o valor do atributo "Savings"
	 * @param id
	 * @param value
	 * @return
	 */
	@PatchMapping(value = "/reduceSavings/{id}")
	public ResponseEntity<VaultDTO> reduceSavings(@PathVariable Long id, @RequestParam("value") BigDecimal value) {
		return ResponseEntity.ok().body(vaultService.reduceSavingsValue(id, value));
	}
	
	/**
	 * Função que incrementa o valor do atributo "onWallet"
	 * @param id
	 * @param value
	 * @return
	 */
	@PatchMapping(value = "/increaseWallet/{id}")
	public ResponseEntity<VaultDTO> increaseWallet(@PathVariable Long id, @RequestParam("value") BigDecimal value) {
		return ResponseEntity.ok().body(vaultService.increaseWalletValue(id, value));
	}
	
	/**
	 * Função que reduz o valor do atributo "onWallet"
	 * @param id
	 * @param value
	 * @return
	 */
	@PatchMapping(value = "/reduceWallet/{id}")
	public ResponseEntity<VaultDTO> reduceWallet(@PathVariable Long id, @RequestParam("value") BigDecimal value) {
		return ResponseEntity.ok().body(vaultService.reduceWalletValue(id, value));
	}
	
	/**
	 * Função que incrementa o valor do atributo "allowedToSpend"
	 * @param id
	 * @param value
	 * @return
	 */
	@PatchMapping(value = "/increaseAllowedToSpend/{id}")
	public ResponseEntity<VaultDTO> increaseAllowedToSpend(@PathVariable Long id, @RequestParam("value") BigDecimal value) {
		return ResponseEntity.ok().body(vaultService.increaseAllowedToSpendValue(id, value));
	}
	
	/**
	 * Função que reduz o valor do atributo "allowedToSpend"
	 * @param id
	 * @param value
	 * @return
	 */
	@PatchMapping(value = "/reduceAllowedToSpend/{id}")
	public ResponseEntity<VaultDTO> reduceAllowedToSpend(@PathVariable Long id, @RequestParam("value") BigDecimal value) {
		return ResponseEntity.ok().body(vaultService.reduceAllowedToSpendValue(id, value));
	}
}