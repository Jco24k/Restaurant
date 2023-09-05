package com.proyecto.idat.dtos;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateComprobanteDto extends CreateComprobanteDto {
	@Nullable
	private Boolean estado;
}