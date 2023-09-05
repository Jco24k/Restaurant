package com.proyecto.idat.dtos;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateFondoDto extends CreateFondoDto {
	@Nullable
	private Boolean estado;
}