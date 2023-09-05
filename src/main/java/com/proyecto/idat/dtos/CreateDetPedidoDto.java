package com.proyecto.idat.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateDetPedidoDto {
  @Pattern(message = "empleado '%s' must be a uuid", regexp = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}", groups = {
      UpdateValidationDetPedido.class, })
  @NotNull(message = "producto must not be null")
  String producto;

  // @NotNull(message = "precio_producto must not be null")
  @Min(value = 1, groups = { UpdateValidationDetPedido.class })
  private BigDecimal precio_producto;

  @Positive
  @Min(value = 1, groups = { UpdateValidationDetPedido.class })
  @NotNull(message = "cantidad must not be null")
  private Integer cantidad;

  public interface UpdateValidationDetPedido {
  }
}
