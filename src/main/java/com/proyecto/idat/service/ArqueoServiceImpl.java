package com.proyecto.idat.service;

import java.util.Collection;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;

import com.proyecto.idat.dtos.CreateArqueoDto;
import com.proyecto.idat.dtos.UpdateArqueoDto;
import com.proyecto.idat.entity.FondoInicialEntity;
import com.proyecto.idat.entity.ArqueoEntity;
import com.proyecto.idat.exception.ResponseException;
import com.proyecto.idat.mapper.ArqueoMapper;
import com.proyecto.idat.repository.ArqueoRepository;
import com.proyecto.idat.util.Mapper;

@Service
public class ArqueoServiceImpl implements ArqueoService {

	Pattern uuidValidate = Pattern.compile("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$");

	@Autowired
	private ArqueoRepository arqueoRepository;

	@Autowired
	private FondoInicialService fondoInicialService;

	@Override
	@Transactional(readOnly = true)
	public Collection<ArqueoMapper> findAll(String estado) {
		if (!estado.equals("all") && !estado.equals("active") && !estado.equals("inactive")) {
			throw new ResponseException("Invalid value for estado. Allowed values are: all, active, inactive",
					HttpStatus.BAD_REQUEST);
		}
		Collection<ArqueoEntity> collection = arqueoRepository.findByAllEstado(estado);
		if (collection.isEmpty() || collection == null) {
			throw new ResponseException("ArqueoEntity not content",
					HttpStatus.NO_CONTENT);
		}
		return Mapper.toArqueo(collection);
	}

	@Override
	@Transactional(readOnly = true)
	public ArqueoEntity findOne(String search) throws ResponseException {
		ArqueoEntity Arqueo = arqueoRepository.findSearchArqueo(search);
		if (Arqueo != null) {
			return Arqueo;
		}
		throw new ResponseException(String.format("Arqueo with id '%s' not found", search),
				HttpStatus.NOT_FOUND);
	}

	@Override
	@Transactional
	public ArqueoEntity create(CreateArqueoDto createFondoDto) throws ResponseException {
		ArqueoEntity arqueoEntity = new ArqueoEntity();
		chargeFondo(arqueoEntity);
		ModelMapper modelMapper = sinEmpleado();

		modelMapper.map(createFondoDto, arqueoEntity);
		arqueoEntity = arqueoRepository.save(arqueoEntity);
		try {
			fondoInicialService.isStartedChange(arqueoEntity.getFondoInicial(), false);
			return arqueoEntity;
		} catch (Exception ex) {
			throw new ResponseException("Unexpected error, check server logs", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@Transactional
	public ArqueoEntity update(UpdateArqueoDto updateArqueoDto, String id) throws ResponseException {
		if (!uuidValidate.matcher(id).matches()) {
			throw new IllegalArgumentException(String.format("id '%s' must be a uuid", id));
		}
		ArqueoEntity Arqueo = arqueoRepository.findById(id)
				.orElseThrow(() -> new ResponseException("Arqueo not found", HttpStatus.NOT_FOUND));
		ModelMapper modelMapper = sinEmpleado();
		modelMapper.map(updateArqueoDto, Arqueo);
		return arqueoRepository.save(Arqueo);
	}

	@Override
	@Transactional
	public void delete(String id) throws ResponseException, IllegalArgumentException, Exception {
		if (!uuidValidate.matcher(id).matches()) {
			throw new IllegalArgumentException(String.format("id '%s' must be a uuid", id));
		}
		ArqueoEntity ArqueoEntity = arqueoRepository.findById(id)
				.orElseThrow(() -> new ResponseException("Arqueo not found", HttpStatus.NOT_FOUND));
		ArqueoEntity.setEstado(false);
		arqueoRepository.save(ArqueoEntity);
	}

	private ModelMapper sinEmpleado() {
		ModelMapper modelMapper = Mapper.notNullMapper();
		// PropertyMap<CreateFondoDto, ArqueoEntity> propertyMap = new
		// PropertyMap<CreateFondoDto, ArqueoEntity>() {
		// protected void configure() {
		// skip().setEmpleado(null);
		// }
		// };
		// modelMapper.addMappings(propertyMap);
		return modelMapper;
	}

	private void chargeFondo(ArqueoEntity arqueoEntity) {
		FondoInicialEntity fondoUltimo = fondoInicialService.verificarInicioProcesos();
		arqueoEntity.setFondoInicial(fondoUltimo);
	}

}
