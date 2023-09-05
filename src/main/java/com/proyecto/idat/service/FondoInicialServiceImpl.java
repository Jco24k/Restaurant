package com.proyecto.idat.service;

import java.util.Collection;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import com.proyecto.idat.dtos.CreateFondoDto;
import com.proyecto.idat.dtos.UpdateFondoDto;
import com.proyecto.idat.entity.EmpleadoEntity;
import com.proyecto.idat.entity.FondoInicialEntity;
import com.proyecto.idat.exception.ResponseException;
import com.proyecto.idat.mapper.FondoMapper;
import com.proyecto.idat.repository.EmpleadoRepository;
import com.proyecto.idat.repository.FondoInicialRepository;
import com.proyecto.idat.util.Mapper;

@Service
public class FondoInicialServiceImpl implements FondoInicialService {

	Pattern uuidValidate = Pattern.compile("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$");

	@Autowired
	private FondoInicialRepository fondoInicialRepository;

	@Autowired
	private EmpleadoRepository empleadoRepository;

	@Override
	@Transactional(readOnly = true)
	public Collection<FondoMapper> findAll(String estado) {
		if (!estado.equals("all") && !estado.equals("active") && !estado.equals("inactive")) {
			throw new ResponseException("Invalid value for estado. Allowed values are: all, active, inactive",
					HttpStatus.BAD_REQUEST);
		}
		Collection<FondoInicialEntity> collection = fondoInicialRepository.findByAllEstado(estado);
		if (collection.isEmpty()) {
			throw new ResponseException("FondoInicialEntity not content",
					HttpStatus.NO_CONTENT);
		}
		return Mapper.toFondoInicials(collection);
	}

	@Override
	@Transactional(readOnly = true)
	public FondoInicialEntity findOne(String search) throws ResponseException {
		FondoInicialEntity FondoInicial = fondoInicialRepository.findSearchFondoInicial(search);
		if (FondoInicial != null) {
			return FondoInicial;
		}
		throw new ResponseException(String.format("FondoInicial with id  '%s' not found", search),
				HttpStatus.NOT_FOUND);
	}

	@Override
	@Transactional
	public FondoInicialEntity create(CreateFondoDto createFondoDto) throws ResponseException {

		verificarFondoInicial(true, "Existe un fondo inicial activo");
		FondoInicialEntity fondoInicialEntity = new FondoInicialEntity();
		chargeFks(fondoInicialEntity, createFondoDto);
		ModelMapper modelMapper = sinEmpleado();

		modelMapper.map(createFondoDto, fondoInicialEntity);
		try {
			return fondoInicialRepository.save(fondoInicialEntity);
		} catch (Exception ex) {
			throw new ResponseException("Unexpected error, check server logs", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@Transactional
	public FondoInicialEntity update(UpdateFondoDto updateFondoInicialDto, String id) throws ResponseException {
		if (!uuidValidate.matcher(id).matches()) {
			throw new IllegalArgumentException(String.format("id '%s' must be a uuid", id));
		}
		FondoInicialEntity fondoInicial = fondoInicialRepository.findById(id)
				.orElseThrow(() -> new ResponseException("FondoInicial not found", HttpStatus.NOT_FOUND));
		ModelMapper modelMapper = sinEmpleado();
		modelMapper.map(updateFondoInicialDto, fondoInicial);
		chargeFks(fondoInicial, updateFondoInicialDto);
		return fondoInicialRepository.save(fondoInicial);
	}

	@Override
	@Transactional
	public void delete(String id, Boolean option) throws ResponseException, IllegalArgumentException, Exception {
		if (!uuidValidate.matcher(id).matches()) {
			throw new IllegalArgumentException(String.format("id '%s' must be a uuid", id));
		}
		FondoInicialEntity FondoInicialEntity = fondoInicialRepository.findById(id)
				.orElseThrow(() -> new ResponseException("FondoInicial not found", HttpStatus.NOT_FOUND));
		FondoInicialEntity.setEstado(option);
		fondoInicialRepository.save(FondoInicialEntity);
	}

	private ModelMapper sinEmpleado() {
		ModelMapper modelMapper = Mapper.notNullMapper();
		PropertyMap<CreateFondoDto, FondoInicialEntity> propertyMap = new PropertyMap<CreateFondoDto, FondoInicialEntity>() {
			protected void configure() {
				skip().setEmpleado(null);
			}
		};
		modelMapper.addMappings(propertyMap);
		return modelMapper;
	}

	private void chargeFks(FondoInicialEntity fondoInicialEntity, CreateFondoDto fondoDto) {
		if (fondoDto.getEmpleado() != null) {
			String idEmpleado = fondoDto.getEmpleado();
			EmpleadoEntity emp = empleadoRepository.findById(idEmpleado)
					.orElseThrow(() -> new ResponseException(String.format("Empleado with id '%s'  not found", idEmpleado),
							HttpStatus.NOT_FOUND));
			fondoInicialEntity.setEmpleado(emp);
		}

	}

	@Override
	@Transactional(readOnly = true)
	public FondoInicialEntity verificarFondoInicial(Boolean option, String message) throws ResponseStatusException {
		FondoInicialEntity fondoUltimo = fondoInicialRepository.findUltimoFondo();
		if (fondoUltimo != null && fondoUltimo.getIsStarted() == option)
			throw new ResponseException(message, HttpStatus.BAD_REQUEST);
		return fondoUltimo;
	}

	@Override
	@Transactional
	public void isStartedChange(FondoInicialEntity fondoInicialEntity, Boolean option)
			throws ResponseException, IllegalArgumentException, Exception {

		fondoInicialEntity.setIsStarted(option);
		fondoInicialRepository.save(fondoInicialEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public FondoInicialEntity verificarInicioProcesos() throws ResponseException {
		FondoInicialEntity fondoUltimo = fondoInicialRepository.findUltimoFondo();
		if (fondoUltimo == null) {
			throw new ResponseException("FondoInicial no encontrado, NECESITA APERTURAR LA CAJA", HttpStatus.NOT_FOUND);
		}
		if (fondoUltimo.getIsStarted() == false) {
			throw new ResponseException(" NECESITA APERTURAR LA CAJA", HttpStatus.NOT_FOUND);
		}
		return fondoUltimo;
	}
}
