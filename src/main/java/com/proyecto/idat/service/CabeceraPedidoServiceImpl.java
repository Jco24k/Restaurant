package com.proyecto.idat.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.proyecto.idat.dtos.CabeceraPedidoDto;
import com.proyecto.idat.dtos.CreateCabPedidoDto;
import com.proyecto.idat.dtos.CreateDetPedidoDto;
import com.proyecto.idat.dtos.ReporteMesDto;
import com.proyecto.idat.entity.CabeceraPedidoEntity;
import com.proyecto.idat.entity.ClienteEntity;
import com.proyecto.idat.entity.DetallePedidoEntity;
import com.proyecto.idat.entity.EmpleadoEntity;
import com.proyecto.idat.entity.FondoInicialEntity;
import com.proyecto.idat.entity.MesaEntity;
import com.proyecto.idat.entity.ProductoEntity;
import com.proyecto.idat.exception.ResponseException;
import com.proyecto.idat.repository.CabeceraPedidoRepository;
import com.proyecto.idat.repository.ClienteRepository;
import com.proyecto.idat.repository.DetallePedidoRepository;
import com.proyecto.idat.repository.EmpleadoRepository;
import com.proyecto.idat.repository.MesaRepository;
import com.proyecto.idat.repository.ProductoRepository;

@Service
public class CabeceraPedidoServiceImpl implements CabeceraPedidoService {
	private static final Logger logger = LogManager.getLogger(CabeceraPedidoServiceImpl.class);
	Pattern uuidValidate = Pattern.compile("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$");

	@Autowired
	private CabeceraPedidoRepository cabeceraPedidoRepository;

	@Autowired
	private DetallePedidoRepository detallePedidoRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private EmpleadoRepository empleadoRepository;

	@Autowired
	private MesaRepository mesaRepository;

	@Autowired
	private ProductoRepository productoRepository;

	@Autowired
	private FondoInicialService fondoInicialService;

	@Override
	@Transactional(readOnly = true)
	public List<CabeceraPedidoEntity> findAll(String estado, int pageNumber, int pageSize) {
		if (!estado.equals("all") && !estado.equals("activo") && !estado.equals("inactivo")
				&& !estado.equals("finalizado")) {
			throw new ResponseException(
					"Invalid value for estado. Allowed values are: all, activo, inactivo, finalizado",
					HttpStatus.BAD_REQUEST);
		}
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return cabeceraPedidoRepository.findByAllEstado(estado, pageable).getContent();
	}

	@Override
	@Transactional(readOnly = true)
	public CabeceraPedidoEntity findOne(String search) throws ResponseException {
		CabeceraPedidoEntity cabeceraPedido = cabeceraPedidoRepository.findById(
				search).orElseThrow(
						() -> new ResponseException("CabeceraPedido not found", HttpStatus.NOT_FOUND));
		return cabeceraPedido;
	}

	@Override
	@Transactional
	public CabeceraPedidoEntity create(CabeceraPedidoDto cabeceraPedidoDto) throws ResponseException {
		CabeceraPedidoEntity cabeceraPedidoEntity = new CabeceraPedidoEntity();
		chargeFondo(cabeceraPedidoEntity);
		List<DetallePedidoEntity> listDetails = cabeceraPedidoDto.getDet_pedido();
		chargeFks(cabeceraPedidoEntity, cabeceraPedidoDto);
		BigDecimal total = calcularTotalUpdate(listDetails, cabeceraPedidoEntity, null);
		cabeceraPedidoEntity.setTotal(total);
		cabeceraPedidoRepository.save(cabeceraPedidoEntity);
		List<DetallePedidoEntity> details = detallePedidoRepository.saveAll(listDetails);
		try {
			cabeceraPedidoEntity.setDet_pedido(details);
			// return cabeceraPedidoDto;
			return cabeceraPedidoEntity;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ResponseException("Unexpected error, check server logs", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@Transactional
	public CabeceraPedidoEntity update(CabeceraPedidoDto cabeceraPedidoDto, String id) throws ResponseException {
		if (!uuidValidate.matcher(id).matches()) {
			throw new IllegalArgumentException(String.format("id '%s' must be a uuid", id));
		}
		CabeceraPedidoEntity cabeceraPedido = cabeceraPedidoRepository.findById(id)
				.orElseThrow(() -> new ResponseException("CabeceraPedido not found", HttpStatus.NOT_FOUND));

		CabeceraPedidoEntity cabeceraPedidoEntity = new CabeceraPedidoEntity();
		cabeceraPedidoEntity.setId(id);
		cabeceraPedidoEntity.setFondoInicial(cabeceraPedido.getFondoInicial());
		cabeceraPedidoEntity.setHora(cabeceraPedido.getHora());
		chargeFks(cabeceraPedidoEntity, cabeceraPedidoDto);

		// GENERAR PEDIDOS
		if (cabeceraPedidoDto.getDet_pedido() != null) {
			List<DetallePedidoEntity> listAnterior = cabeceraPedido.getDet_pedido();
			detallePedidoRepository.deleteAll(cabeceraPedido.getDet_pedido());
			List<DetallePedidoEntity> listDetails = cabeceraPedidoDto.getDet_pedido();

			BigDecimal total = calcularTotalUpdate(listDetails, cabeceraPedidoEntity, listAnterior);
			cabeceraPedidoEntity.setTotal(total);
			cabeceraPedidoEntity = cabeceraPedidoRepository.save(cabeceraPedidoEntity);
			List<DetallePedidoEntity> details = detallePedidoRepository.saveAll(listDetails);
			cabeceraPedidoEntity.setDet_pedido(details);
		} else {

			cabeceraPedidoEntity.setDet_pedido(cabeceraPedido.getDet_pedido());
		}

		return cabeceraPedidoEntity;
	}

	@Override
	@Transactional
	public void changeState(String id, String estado) throws ResponseException, IllegalArgumentException, Exception {
		if (!uuidValidate.matcher(id).matches()) {
			throw new IllegalArgumentException(String.format("id '%s' must be a uuid", id));
		}
		if (!estado.equals("finalizado") && !estado.equals("activo") && !estado.equals("inactivo")) {
			throw new ResponseException("Invalid value for estado. Allowed estados are: activo|inactivo|finalizado",
					HttpStatus.BAD_REQUEST);
		}
		CabeceraPedidoEntity cabeceraPedidoEntity = cabeceraPedidoRepository.findById(id)
				.orElseThrow(() -> new ResponseException("cabeceraPedido not found", HttpStatus.NOT_FOUND));
		cabeceraPedidoEntity.setEstado(estado);
		cabeceraPedidoRepository.save(cabeceraPedidoEntity);
	}

	private void chargeFks(CabeceraPedidoEntity cabeceraPedidoEntity, CabeceraPedidoDto cabeceraPedidoDto) {

		if (cabeceraPedidoDto.getCliente() != null) {
			String idCliente = cabeceraPedidoDto.getCliente().getId();
			ClienteEntity cliente = clienteRepository.findById(idCliente).orElseThrow(() -> new ResponseException(
					String.format("Cliente with id '%s'  not found", idCliente), HttpStatus.NOT_FOUND));
			cabeceraPedidoEntity.setCliente(cliente);
		}
		if (cabeceraPedidoDto.getEmpleado() != null) {
			String idEmpleado = cabeceraPedidoDto.getEmpleado().getId();
			EmpleadoEntity empleado = empleadoRepository.findById(idEmpleado).orElseThrow(() -> new ResponseException(
					String.format("Empleado with id '%s'  not found", idEmpleado), HttpStatus.NOT_FOUND));
			cabeceraPedidoEntity.setEmpleado(empleado);
		}
		if (cabeceraPedidoDto.getMesa() != null) {
			String idMesa = cabeceraPedidoDto.getMesa().getId();
			MesaEntity mesa = mesaRepository.findById(idMesa).orElseThrow(() -> new ResponseException(
					String.format("Mesa with id '%s'  not found", idMesa), HttpStatus.NOT_FOUND));
			cabeceraPedidoEntity.setMesa(mesa);
		}
		if (cabeceraPedidoDto.getEstado() != null) {
			cabeceraPedidoEntity.setEstado(cabeceraPedidoDto.getEstado());
		}
		if (cabeceraPedidoDto.getFecha() != null) {
			cabeceraPedidoEntity.setFecha(cabeceraPedidoDto.getFecha());
		}
		if (cabeceraPedidoDto.getForma_pago() != null) {
			cabeceraPedidoEntity.setForma_pago(cabeceraPedidoDto.getForma_pago());
		}

		// cabeceraPedidoEntity.setData(cliente, mesa, empleado, new BigDecimal(0));
	}

	private BigDecimal calcularTotalUpdate(List<DetallePedidoEntity> detallePedidoDto,
			CabeceraPedidoEntity cabeceraPedidoEntity, List<DetallePedidoEntity> ListdetallePedidoAnterior) {
		if (ListdetallePedidoAnterior != null) {
			for (DetallePedidoEntity detalleAnterior : ListdetallePedidoAnterior) {
				ProductoEntity pro = detalleAnterior.getProducto();
				pro.setStock(pro.getStock() + detalleAnterior.getCantidad());
				productoRepository.save(pro);
			}
		}

		BigDecimal total = new BigDecimal(0);
		for (DetallePedidoEntity det : detallePedidoDto) {
			String proId = det.getProducto().getId();
			ProductoEntity proFind = productoRepository.findById(proId)
					.orElseThrow(() -> new ResponseException("Product not found", HttpStatus.NOT_FOUND));

			if (proFind.getStock() < det.getCantidad())
				throw new ResponseException(
						String.format("Product with id '%s' out of stock (max. %s) ", proId, proFind.getStock()),
						HttpStatus.BAD_REQUEST);
			det.setPrecio_producto(proFind.getPrecio());
			BigDecimal subtotal = det.getPrecio_producto().multiply(new BigDecimal(det.getCantidad()));
			logger.info("SUBTOTAL: " + subtotal);
			proFind.setStock(proFind.getStock() - det.getCantidad());
			productoRepository.save(proFind);
			det.chargeIds(cabeceraPedidoEntity, proFind);
			total = total.add(subtotal);

		}
		logger.info("MONTO_TOTAL: " + total);
		return total;
	}

	@Override
	public CabeceraPedidoEntity createOne(CreateCabPedidoDto createCabPedidoDto) throws ResponseException {
		fondoInicialService.verificarInicioProcesos();
		List<CreateDetPedidoDto> listDetails = createCabPedidoDto.getDet_pedido();
		CabeceraPedidoEntity cabeceraPedidoEntity = new CabeceraPedidoEntity();

		// * CARGOS FKS
		chargeFksTwo(cabeceraPedidoEntity, createCabPedidoDto);
		List<DetallePedidoEntity> detailsCreate = chargeListDetails(listDetails, cabeceraPedidoEntity, null);
		cabeceraPedidoRepository.save(cabeceraPedidoEntity);
		List<DetallePedidoEntity> details = detallePedidoRepository.saveAll(detailsCreate);
		try {
			cabeceraPedidoEntity.setDet_pedido(details);
			return cabeceraPedidoEntity;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ResponseException("Unexpected error, check server logs", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private void chargeFksTwo(CabeceraPedidoEntity cabeceraPedidoEntity, CreateCabPedidoDto createCabPedidoDto) {

		if (createCabPedidoDto.getCliente() != null) {
			String idCliente = createCabPedidoDto.getCliente();
			ClienteEntity cliente = clienteRepository.findById(idCliente)
					.orElseThrow(() -> new ResponseException(String.format("Cliente with id '%s'  not found", idCliente),
							HttpStatus.NOT_FOUND));
			cabeceraPedidoEntity.setCliente(cliente);
		}
		if (createCabPedidoDto.getEmpleado() != null) {
			String idEmpleado = createCabPedidoDto.getEmpleado();
			EmpleadoEntity empleado = empleadoRepository.findById(idEmpleado).orElseThrow(() -> new ResponseException(
					String.format("Empleado with id '%s'  not found", idEmpleado), HttpStatus.NOT_FOUND));
			cabeceraPedidoEntity.setEmpleado(empleado);
		}
		if (createCabPedidoDto.getMesa() != null) {
			String idMesa = createCabPedidoDto.getMesa();
			MesaEntity mesa = mesaRepository.findById(idMesa).orElseThrow(() -> new ResponseException(
					String.format("Mesa with id '%s'  not found", idMesa), HttpStatus.NOT_FOUND));
			cabeceraPedidoEntity.setMesa(mesa);
		}
	}

	private List<DetallePedidoEntity> chargeListDetails(List<CreateDetPedidoDto> detallePedidoDto,
			CabeceraPedidoEntity cabeceraPedidoEntity, List<DetallePedidoEntity> ListdetallePedidoAnterior) {

		// * DEVOLVER STOCK */
		if (ListdetallePedidoAnterior != null) {
			for (DetallePedidoEntity detalleAnterior : ListdetallePedidoAnterior) {
				ProductoEntity pro = detalleAnterior.getProducto();
				pro.setStock(pro.getStock() + detalleAnterior.getCantidad());
				productoRepository.save(pro);
			}
		}

		BigDecimal total = new BigDecimal(0);
		List<DetallePedidoEntity> detallePedidoEntities = new ArrayList<>();
		List<ProductoEntity> productoEntities = new ArrayList<>();

		for (CreateDetPedidoDto det : detallePedidoDto) {
			// * PRODUCT */
			String proId = det.getProducto();
			ProductoEntity proFind = productoRepository.findById(proId)
					.orElseThrow(() -> new ResponseException("Product not found", HttpStatus.NOT_FOUND));

			if (proFind.getStock() < det.getCantidad())
				throw new ResponseException(
						String.format("Product with id '%s' out of stock (max. %s) ", proId, proFind.getStock()),
						HttpStatus.BAD_REQUEST);

			// *ADD LIST_DETAILS */
			DetallePedidoEntity detallePedidoEntity = new DetallePedidoEntity();
			detallePedidoEntity.setCantidad(det.getCantidad());
			// if (Objects.isNull(det.getPrecio_producto()))
			// detallePedidoEntity.setPrecio_producto(proFind.getPrecio());
			// else {
			// detallePedidoEntity.setPrecio_producto(det.getPrecio_producto());

			// }
			detallePedidoEntity.setPrecio_producto(proFind.getPrecio()); // *FALTA VALIDAR, BIGDECIMAL NO SEA NULL */
			logger.info("Total: " + det.getPrecio_producto());
			BigDecimal subtotal = det.getPrecio_producto().multiply(new BigDecimal(det.getCantidad()));
			proFind.setStock(proFind.getStock() - det.getCantidad());
			productoEntities.add(proFind);
			detallePedidoEntity.chargeIds(cabeceraPedidoEntity, proFind);

			detallePedidoEntities.add(detallePedidoEntity);
			total = total.add(subtotal);

		}

		cabeceraPedidoEntity.setTotal(total);
		productoRepository.saveAll(productoEntities);
		return detallePedidoEntities;
	}

	private void chargeFondo(CabeceraPedidoEntity cabPedido) {
		FondoInicialEntity fondoUltimo = fondoInicialService.verificarInicioProcesos();
		cabPedido.setFondoInicial(fondoUltimo);
	}

	@Override
	public List<CabeceraPedidoEntity> filterSale(LocalDate fecha, int pageNumber, int pageSize) {
		PageRequest pageable = PageRequest.of(pageNumber, pageSize);
		if (fecha == null) {

			FondoInicialEntity fondo = fondoInicialService.verificarInicioProcesos();
			fecha = fondo.getFecha();
		}
		return cabeceraPedidoRepository.findAllByDate(fecha, pageable).getContent();
	}

	@Override
	public List<ReporteMesDto> findAllMountYear(Integer year, Integer month, int pageNumber, int pageSize) {
		PageRequest pageable = PageRequest.of(pageNumber, pageSize);
		if (year == null) {
			year = LocalDate.now().getYear(); // Obtener el aÃ±o actual
		}
		if (month == null) {
			month = LocalDate.now().getMonthValue();
		}

		return cabeceraPedidoRepository.findAllMountYear(year, month, pageable).getContent();
	}
}