package br.com.kuntzedev.moneymaster.controllers;

import java.util.Random;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@RestController
@RequestMapping(value = "/operations")
public class OperationsController {

	@Autowired
	private final MeterRegistry meterRegistry;
	private static final Logger LOGGER = LoggerFactory.getLogger(OperationsController.class);

	public OperationsController(MeterRegistry meterRegistry) {
		this.meterRegistry = meterRegistry;
	}

	@GetMapping(value = "/errors")
	public ResponseEntity<Void> getErrors() {
		IntStream.range(0, 100).forEach(value -> {
			Counter counter = Counter.builder("money_master_api_errors").tag("api_errors", "error")
					.description("The amount of API errors registered.").register(meterRegistry);
			int valor = new Random().nextInt(5000);
			if(valor >= 4000) {
				LOGGER.error("[OPC] - Invalid value");
				counter.increment();
			}
		});
		
		return ResponseEntity.ok().build();
	}
}