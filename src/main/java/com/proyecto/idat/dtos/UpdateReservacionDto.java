package com.proyecto.idat.dtos;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateReservacionDto extends CreateReservacionDto {
	@Nullable
	private Boolean estado;
}