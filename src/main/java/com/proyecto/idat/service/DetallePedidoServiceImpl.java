//package com.proyecto.idat.service;

//import java.util.List;
//import java.util.regex.Pattern;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import com.proyecto.idat.entity.DetallePedidoEntity;
//import com.proyecto.idat.exception.ResponseException;
//import com.proyecto.idat.repository.DetallePedidoRepository;
//import com.proyecto.idat.repository.ClienteRepository;
//import com.proyecto.idat.repository.EmpleadoRepository;
//import com.proyecto.idat.repository.MesaRepository;

// @Service
// public class DetallePedidoServiceImpl implements DetallePedidoService {

// 	Pattern uuidValidate = Pattern.compile("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$");

// 	@Autowired
// 	private DetallePedidoRepository DetallePedidoRepository;

// 	@Autowired
// 	private ClienteRepository clienteRepository;

// 	@Autowired
// 	private EmpleadoRepository empleadoRepository;

// 	@Autowired
// 	private MesaRepository mesaRepository;

// 	@Override
// 	@Transactional(readOnly = true)
// 	public List<DetallePedidoEntity> findAll(int pageNumber, int pageSize) {
// 		Pageable pageable = PageRequest.of(pageNumber, pageSize);
// 		return DetallePedidoRepository.findByAllEstado( pageable).getContent();
// 	}

// 	@Override
// 	@Transactional(readOnly = true)
// 	public DetallePedidoEntity findOne(String search) throws ResponseException {
// 		DetallePedidoEntity DetallePedido = DetallePedidoRepository.findById(
// 				search).orElseThrow(
// 						() -> new ResponseException("DetallePedido not found", HttpStatus.NOT_FOUND));
// 		return DetallePedido;
// 	}

// 	@Override
// 	@Transactional
// 	public DetallePedidoEntity create(DetallePedidoEntity DetallePedidoEntity) throws ResponseException {

// 		String idCliente = DetallePedidoEntity.getCliente().getId(),
// 				idEmpleado = DetallePedidoEntity.getEmpleado().getId(),
// 				idMesa = DetallePedidoEntity.getMesa().getId();
// 		clienteRepository.findById(idCliente).orElseThrow(() -> new ResponseException(
// 				String.format("Cliente with id '%s'  not found", idCliente), HttpStatus.NOT_FOUND));

// 		mesaRepository.findById(idMesa).orElseThrow(() -> new ResponseException(
// 				String.format("Mesa with id '%s'  not found", idCliente), HttpStatus.NOT_FOUND));

// 		empleadoRepository.findById(idEmpleado).orElseThrow(() -> new ResponseException(
// 				String.format("Empleado with id '%s'  not found", idCliente), HttpStatus.NOT_FOUND));
// 		try {
// 			return DetallePedidoRepository.save(DetallePedidoEntity);
// 		} catch (Exception ex) {
// 			ex.printStackTrace();
// 			throw new ResponseException("Unexpected error, check server logs", HttpStatus.INTERNAL_SERVER_ERROR);
// 		}
// 	}

// 	@Override
// 	@Transactional
// 	public DetallePedidoEntity update(DetallePedidoEntity DetallePedidoEntity, String id) throws ResponseException {
// 		if (!uuidValidate.matcher(id).matches()) {
// 			throw new IllegalArgumentException(String.format("id '%s' must be a uuid", id));
// 		}
// 		DetallePedidoEntity DetallePedido = DetallePedidoRepository.findById(id)
// 				.orElseThrow(() -> new ResponseException("DetallePedido not found", HttpStatus.NOT_FOUND));
// 		DetallePedidoEntity.setId(id);
// 		DetallePedido.updateDetallePedido(DetallePedidoEntity);
// 		return DetallePedidoRepository.save(DetallePedido);
// 	}

// 	@Override
// 	@Transactional
// 	public void changeState(String id, String estado) throws ResponseException, IllegalArgumentException, Exception {
// 		if (!uuidValidate.matcher(id).matches()) {
// 			throw new IllegalArgumentException(String.format("id '%s' must be a uuid", id));
// 		}
// 		if (!estado.equals("finalizado") && !estado.equals("activo") && !estado.equals("inactivo")) {
// 			throw new ResponseException("Invalid value for estado. Allowed estados are: activo|inactivo|finalizado",
// 					HttpStatus.BAD_REQUEST);
// 		}
// 		DetallePedidoEntity DetallePedidoEntity = DetallePedidoRepository.findById(id)
// 				.orElseThrow(() -> new ResponseException("DetallePedido not found", HttpStatus.NOT_FOUND));
// 		DetallePedidoEntity.setEstado(estado);
// 		DetallePedidoRepository.save(DetallePedidoEntity);
// 	}

// }
