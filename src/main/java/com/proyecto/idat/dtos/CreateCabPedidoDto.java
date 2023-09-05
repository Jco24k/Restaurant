package com.proyecto.idat.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.GroupSequence;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
@GroupSequence({ CreateCabPedidoDto.class, CreateCabPedidoDto.UpdateValidationCabPedido.class })
public class CreateCabPedidoDto {

	private String id;
	@Valid()
	@NotNull(message = "det_pedido must not be null")
	@NotEmpty(message = "det_pedido must not be empty")
	private List<CreateDetPedidoDto> det_pedido;

	@Pattern(message = "empleado '%s' must be a uuid", regexp = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}", groups = {
			UpdateValidationCabPedido.class, })
	@NotNull(message = "empleado must not be null")
	private String empleado;

	@Pattern(message = "cliente '%s' must be a uuid", regexp = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}", groups = {
			UpdateValidationCabPedido.class, })
	private String cliente;

	@Pattern(message = "mesa '%s' must be a uuid", regexp = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}", groups = {
			UpdateValidationCabPedido.class, })
	@NotNull(message = "mesa must not be null")
	private String mesa;

	@Nullable()
	@Pattern(regexp = "efectivo|tarjeta", message = "forma_pago is invalid ( efectivo | tarjeta )", groups = {
			UpdateValidationCabPedido.class })
	private String forma_pago;

	public interface UpdateValidationCabPedido {
	}

}
