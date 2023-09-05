package com.proyecto.idat.service;

import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.BeanUtils;
import com.proyecto.idat.entity.MesaEntity;
import com.proyecto.idat.exception.ResponseException;
import com.proyecto.idat.repository.MesaRepository;

@Service
public class MesaServiceImpl implements MesaService {

	Pattern uuidValidate = Pattern.compile("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$");

	@Autowired
	private MesaRepository mesaRepository;


	/*@Override
	@Transactional(readOnly = true)
	public  List<MesaEntity> findAll(String estado,int pageNumber,int pageSize) {
		if (!estado.equals("activo") && !estado.equals("inactivo") && !estado.equals("mantenimiento")) {
	        throw new ResponseException("Invalid estado for estado. Allowed estados are: activo, inactivo, mantenimiento", HttpStatus.BAD_REQUEST);
	    }
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return mesaRepository.findByAllEstado(estado,pageable).getContent();
	}*/
	@Override
	@Transactional(readOnly = true)
	public List<MesaEntity> findAll(int pageNumber, int pageSize) {
	    Pageable pageable = PageRequest.of(pageNumber, pageSize);
	    return mesaRepository.findAll(pageable).getContent();
	}

	@Override
	@Transactional(readOnly = true)
	public MesaEntity findOne(String search) throws ResponseException {
		MesaEntity Mesa = mesaRepository.findSearchMesa(search);
        if(Mesa != null){
            return Mesa;
        }
        throw new  ResponseException(String.format("Mesa with id or name '%s' not found", search),
        HttpStatus.NOT_FOUND);
	}

	@Override
	@Transactional
    public MesaEntity create(MesaEntity MesaEntity) throws ResponseException {
        try {
            return mesaRepository.save(MesaEntity);
        } catch (Exception ex) {
            throw new ResponseException("Unexpected error, check server logs", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
	@Override
	@Transactional
	public MesaEntity update(MesaEntity mesaEntity, String id) throws ResponseException {
		if (!uuidValidate.matcher(id).matches()) {
			throw new IllegalArgumentException(String.format("id '%s' must be a uuid", id));
		}
		MesaEntity mesa = mesaRepository.findById(id).orElseThrow(()-> new ResponseException("Mesa not found", HttpStatus.NOT_FOUND));
		mesaEntity.setId(id);
		mesaEntity.setEstado(mesa.getEstado());
		BeanUtils.copyProperties(mesaEntity, mesa);
		return mesaRepository.save(mesa);
	}

	@Override
	@Transactional
    public void changeState(String id,String estado) throws ResponseException, IllegalArgumentException, Exception {
        if (!uuidValidate.matcher(id).matches()) {
            throw new IllegalArgumentException(String.format("id '%s' must be a uuid", id));
        }
		if (!estado.equals("libre") && !estado.equals("ocupado") && !estado.equals("reserva") && !estado.equals("mantenimiento")) {
	        throw new ResponseException("Invalid value for estado. Allowed estados are: libre|ocupado|reserva|mantenimiento", HttpStatus.BAD_REQUEST);
	    }
        MesaEntity mesaEntity = mesaRepository.findById(id).orElseThrow(()-> new ResponseException("Mesa not found", HttpStatus.NOT_FOUND));
        mesaEntity.setEstado(estado);
        mesaRepository.save(mesaEntity);
    }

	//listado de mesas libres
	@Override
	@Transactional(readOnly = true)
	public Collection<MesaEntity> findTableByStatusFree() {
		return mesaRepository.findTableByStatusFree();
		
	}

}
