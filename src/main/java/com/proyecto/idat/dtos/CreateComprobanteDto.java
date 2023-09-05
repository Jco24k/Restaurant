package com.proyecto.idat.dtos;


import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@GroupSequence({ CreateComprobanteDto.class, CreateComprobanteDto.UpdateValidationComprobante.class })
public class CreateComprobanteDto {

	@Pattern(regexp = "boleta|factura", message = "type is invalid ( boleta | factura )", groups = UpdateValidationComprobante.class)
	private String tipo;

	@NotNull()
	@Pattern(message = "empleado '%s' must be a uuid", regexp = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}", groups = {
			UpdateValidationComprobante.class, })
	private String cab_pedido;

	public interface UpdateValidationComprobante {
	}

}