package com.ordercontrol.infrastructure.validator;

import static com.ordercontrol.utils.NumberUtils.isInvalidId;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ordercontrol.application.dto.customerorder.OrderItemInsertDto;
import com.ordercontrol.domain.model.Product;
import com.ordercontrol.infrastructure.exception.ResourceNotFoundException;
import com.ordercontrol.infrastructure.exception.ValidationException;
import com.ordercontrol.infrastructure.repository.IProductRepository;
import com.ordercontrol.utils.NumberUtils;

@Component
public class ItemInsertValidator {

	@Autowired
	private IProductRepository productRepository;

	public void validate(OrderItemInsertDto itemDto) {
		validateProductId(itemDto.getProductId());
		validateUnitPrice(itemDto.getUnitPrice());
		validateQuantity(itemDto.getQuantity(), itemDto.getProductId());
	}

	private void validateProductId(Long productId) {
		if (isInvalidId(productId)) {
			throw new ValidationException("The productId is mandatory information.",
					"O identificar do produto é uma informação obrigatória.");
		}
		validateIdProductExists(productId);
	}

	private void validateIdProductExists(Long productId) {
		if (!productRepository.findById(productId).isPresent()) {
			throw new ResourceNotFoundException("Product not found with Id: ".concat(productId.toString()),
					"Produto não encontrado. Favor verificar o id fornecido.");
		}
	}

	private void validateUnitPrice(BigDecimal unitPrice) {
		if (!NumberUtils.isPositiveNumber(unitPrice)) {
			throw new ValidationException(
					"The unitPrice cannot be a negative number or zero: ".concat(unitPrice.toString()),
					"O preço unitário não poder ser um valor negativo ou zero. Favor informar um valor positivo válido.");
		}
	}

	private void validateQuantity(Integer quantity, Long productId) {
		if (!NumberUtils.isPositiveNumber(quantity)) {
			throw new ValidationException(
					"The quantity cannot be a negative number  or zero: ".concat(quantity.toString()),
					"A quantidade não poder ser um valor negativo ou zero. Favor informar um valor positivo válido.");
		}
		validateQuantityAvaliable(quantity, productId);
	}

	private void validateQuantityAvaliable(Integer quantity, Long productId) {
		Optional<Product> product = productRepository.findById(productId);
		Integer quantityAvailable = product.get().getUnits();
		if (quantityAvailable - quantity < 0) {
			throw new ValidationException(
					"The quantity requested exceeds the quantity available in stock. The quantity available for product id "
							.concat(productId.toString()).concat(" is: ").concat(quantityAvailable.toString()),
					"A quantidade solicitada de ".concat(product.get().getName())
							.concat(" ultrapassa a quantidade disponível em estoque. A quantidade disponível é de ")
							.concat(quantityAvailable.toString()).concat(" unidades."));
		}
	}
}
