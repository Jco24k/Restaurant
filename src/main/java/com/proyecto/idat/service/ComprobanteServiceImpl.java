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

import com.proyecto.idat.dtos.CreateComprobanteDto;
import com.proyecto.idat.dtos.UpdateComprobanteDto;
import com.proyecto.idat.entity.CabeceraPedidoEntity;
import com.proyecto.idat.entity.ComprobanteEntity;
import com.proyecto.idat.exception.ResponseException;
import com.proyecto.idat.repository.CabeceraPedidoRepository;
import com.proyecto.idat.repository.ComprobanteRepository;
import com.proyecto.idat.util.Mapper;

@Service
public class ComprobanteServiceImpl implements ComprobanteService {

	Pattern uuidValidate = Pattern.compile("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$");

	@Autowired
	private ComprobanteRepository comprobanteRepository;

	@Autowired
	private CabeceraPedidoRepository cabeceraPedidoRepository;

	@Autowired
	private FondoInicialService fondoInicialService;

	@Override
	@Transactional(readOnly = true)
	public List<ComprobanteEntity> findAll(String estado, int pageNumber, int pageSize) {
		if (!estado.equals("all") && !estado.equals("activo") && !estado.equals("inactivo")) {
			throw new ResponseException("Invalid value for estado. Allowed values are: all, activo, inactivo",
					HttpStatus.BAD_REQUEST);
		}
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return comprobanteRepository.findByAllEstado(estado, pageable).getContent();
	}

	@Override
	@Transactional(readOnly = true)
	public ComprobanteEntity findOne(String search) throws ResponseException {
		ComprobanteEntity Comprobante = comprobanteRepository.findSearchComprobante(search);
		if (Comprobante != null) {
			return Comprobante;
		}
		throw new ResponseException(String.format("Comprobante with id or nro_comprobante '%s' not found", search),
				HttpStatus.NOT_FOUND);
	}

	@Override
	@Transactional
	public ComprobanteEntity create(CreateComprobanteDto comprobanteDto) throws ResponseException {
		fondoInicialService.verificarInicioProcesos();
		ComprobanteEntity comp = new ComprobanteEntity();
		chargeFks(comp, comprobanteDto);
		ModelMapper modelMapper = sinNroComprobante();
		modelMapper.map(comprobanteDto, comp);
		try {
			return comprobanteRepository.save(comp);
		} catch (Exception ex) {
			throw new ResponseException("Unexpected error, check server logs", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@Transactional
	public ComprobanteEntity update(UpdateComprobanteDto comprobanteDto, Long id) throws ResponseException {

		ComprobanteEntity comprobante = comprobanteRepository.findById(id)
				.orElseThrow(() -> new ResponseException("Comprobante not found", HttpStatus.NOT_FOUND));
		ModelMapper modelMapper = sinNroComprobante();
		modelMapper.map(comprobanteDto, comprobante);
		return comprobanteRepository.save(comprobante);
	}

	@Override
	@Transactional
	public void changeState(Long id, Boolean value) throws ResponseException, IllegalArgumentException, Exception {
		ComprobanteEntity comprobanteEntity = comprobanteRepository.findById(id)
				.orElseThrow(() -> new ResponseException("Comprobante not found", HttpStatus.NOT_FOUND));
		comprobanteEntity.setEstado(value);
		comprobanteRepository.save(comprobanteEntity);
	}

	private void chargeFks(ComprobanteEntity comprobanteEntity, CreateComprobanteDto comprobanteDto) {

		if (comprobanteDto.getCab_pedido() != null) {
			String idCab = comprobanteDto.getCab_pedido();
			CabeceraPedidoEntity cabPedido = cabeceraPedidoRepository.findById(idCab)
					.orElseThrow(() -> new ResponseException(String.format("CabeceraPedido with id '%s'  not found", idCab),
							HttpStatus.NOT_FOUND));
			comprobanteEntity.setCab_pedido(cabPedido);
		}
	}

	private ModelMapper sinNroComprobante() {
		ModelMapper modelMapper = Mapper.notNullMapper();
		PropertyMap<CreateComprobanteDto, ComprobanteEntity> propertyMap = new PropertyMap<CreateComprobanteDto, ComprobanteEntity>() {
			protected void configure() {
				skip().setNro_comprobante(null);
			}
		};
		modelMapper.addMappings(propertyMap);
		return modelMapper;
	}

}
