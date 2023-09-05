package com.proyecto.idat.util;

import java.util.ArrayList;
import java.util.Collection;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import com.proyecto.idat.entity.ArqueoEntity;
import com.proyecto.idat.entity.FondoInicialEntity;
import com.proyecto.idat.mapper.ArqueoMapper;
import com.proyecto.idat.mapper.FondoMapper;

public class Mapper {
	public static Collection<FondoMapper> toFondoInicials(Collection<FondoInicialEntity> FondoInicials) {
		Collection<FondoMapper> collection = new ArrayList<>();
		for (FondoInicialEntity FondoInicial : FondoInicials) {
			FondoMapper mapper = new FondoMapper(FondoInicial);
			collection.add(mapper);
		}
		return collection;
	}

	public static Collection<ArqueoMapper> toArqueo(Collection<ArqueoEntity> FondoInicials) {
		Collection<ArqueoMapper> collection = new ArrayList<>();
		for (ArqueoEntity FondoInicial : FondoInicials) {
			ArqueoMapper mapper = new ArqueoMapper(FondoInicial);
			collection.add(mapper);
		}
		return collection;
	}

	public static ModelMapper notNullMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
		return modelMapper;
	}

}
