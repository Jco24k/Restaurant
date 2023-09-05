package com.proyecto.idat.service;

import java.util.List;
import java.util.regex.Pattern;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.idat.dtos.CreateReservacionDto;
import com.proyecto.idat.dtos.UpdateReservacionDto;
import com.proyecto.idat.entity.ClienteEntity;
import com.proyecto.idat.entity.EmpleadoEntity;
import com.proyecto.idat.entity.MesaEntity;
import com.proyecto.idat.entity.ReservacionEntity;
import com.proyecto.idat.exception.ResponseException;
import com.proyecto.idat.repository.ClienteRepository;
import com.proyecto.idat.repository.EmpleadoRepository;
// import com.proyecto.idat.repository.EmpleadoRepository;
import com.proyecto.idat.repository.MesaRepository;
import com.proyecto.idat.repository.ReservacionRepository;
import com.proyecto.idat.util.Mapper;

@Service
public class ReservacionServiceImpl implements ReservacionService {

	Pattern uuidValidate = Pattern.compile("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$");

	@Autowired
	private ReservacionRepository reservacionRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private EmpleadoRepository empleadoRepository;

	@Autowired
	private MesaRepository mesaRepository;

	@Autowired
	private FondoInicialService fondoInicialService;

	@Override
	@Transactional(readOnly = true)
	public List<ReservacionEntity> findAll(String estado, int pageNumber, int pageSize) {
		if (!estado.equals("all") && !estado.equals("activo") && !estado.equals("inactivo")) {
			throw new ResponseException("Invalid value for estado. Allowed values are: all, activo, inactivo",
					HttpStatus.BAD_REQUEST);
		}
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return reservacionRepository.findByAllEstado(estado, pageable).getContent();
	}

	@Override
	@Transactional(readOnly = true)
	public ReservacionEntity findOne(String search) throws ResponseException {
		ReservacionEntity Reservacion = reservacionRepository.findSearchReservacion(search);
		if (Reservacion != null) {
			return Reservacion;
		}
		throw new ResponseException(String.format("Reservacion with id or name '%s' not found", search),
				HttpStatus.NOT_FOUND);
	}

	@Override
	@Transactional
	public ReservacionEntity create(CreateReservacionDto reservacionDto) throws ResponseException {
		fondoInicialService.verificarInicioProcesos();
		ReservacionEntity reservacionEntity = new ReservacionEntity();
		chargeFKS(reservacionEntity, reservacionDto);
		ModelMapper modelMapper = sinFKS();
		modelMapper.map(reservacionDto, reservacionEntity);
		try {
			return reservacionRepository.save(reservacionEntity);
		} catch (Exception ex) {
			throw new ResponseException("Unexpected error, check server logs", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@Transactional
	public ReservacionEntity update(UpdateReservacionDto reservacionDto, String id) throws ResponseException {
		if (!uuidValidate.matcher(id).matches()) {
			throw new IllegalArgumentException(String.format("id '%s' must be a uuid", id));
		}
		ReservacionEntity reservacionEntity = reservacionRepository.findById(id)
				.orElseThrow(() -> new ResponseException("Reservacion not found", HttpStatus.NOT_FOUND));

		chargeFKS(reservacionEntity, reservacionDto);
		ModelMapper modelMapper = sinFKS();
		modelMapper.map(reservacionDto, reservacionEntity);
		return reservacionRepository.save(reservacionEntity);
	}

	@Override
	@Transactional
	public void changeState(String id, Boolean value) throws ResponseException, IllegalArgumentException, Exception {
		if (!uuidValidate.matcher(id).matches()) {
			throw new IllegalArgumentException(String.format("id '%s' must be a uuid", id));
		}
		ReservacionEntity ReservacionEntity = reservacionRepository.findById(id)
				.orElseThrow(() -> new ResponseException("Reservacion not found", HttpStatus.NOT_FOUND));
		ReservacionEntity.setEstado(value);
		reservacionRepository.save(ReservacionEntity);
	}

	private ModelMapper sinFKS() {
		ModelMapper modelMapper = Mapper.notNullMapper();
		PropertyMap<CreateReservacionDto, ReservacionEntity> propertyMap = new PropertyMap<CreateReservacionDto, ReservacionEntity>() {
			protected void configure() {
				skip().setEmpleado(null);
				skip().setMesa(null);
				skip().setCliente(null);
			}
		};
		modelMapper.addMappings(propertyMap);
		return modelMapper;
	}

	private void chargeFKS(ReservacionEntity reservacionEntity, CreateReservacionDto createReservacionDto) {
		if (createReservacionDto.getCliente() != null) {
			String idCliente = createReservacionDto.getCliente();
			ClienteEntity cliente = clienteRepository.findById(idCliente)
					.orElseThrow(() -> new ResponseException(String.format("Cliente with id '%s'  not found", idCliente),
							HttpStatus.NOT_FOUND));
			reservacionEntity.setCliente(cliente);
		}
		if (createReservacionDto.getEmpleado() != null) {
			String idEmpleado = createReservacionDto.getEmpleado();
			EmpleadoEntity empleado = empleadoRepository.findById(idEmpleado)
					.orElseThrow(() -> new ResponseException(String.format("Empleado with id '%s'  not found", idEmpleado),
							HttpStatus.NOT_FOUND));
			reservacionEntity.setEmpleado(empleado);
		}
		if (createReservacionDto.getMesa() != null) {
			String idMesa = createReservacionDto.getMesa();
			MesaEntity mesa = mesaRepository.findById(idMesa)
					.orElseThrow(() -> new ResponseException(String.format("Mesa with id '%s'  not found", idMesa),
							HttpStatus.NOT_FOUND));

			reservacionEntity.setMesa(mesa);

		}
	}

}