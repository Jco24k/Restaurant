package com.proyecto.idat.service;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.proyecto.idat.entity.EmpleadoEntity;
import com.proyecto.idat.exception.ResponseException;
import com.proyecto.idat.repository.EmpleadoRepository;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {

	Pattern uuidValidate = Pattern.compile("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$");

	@Autowired
	private EmpleadoRepository empleadoRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	

	@Override
	@Transactional(readOnly = true)
	public List<EmpleadoEntity> findAll(String estado) {
		if (!estado.equals("all") && !estado.equals("active") && !estado.equals("inactive")) {
	        throw new ResponseException("Invalid value for estado. Allowed values are: all, active, inactive", HttpStatus.BAD_REQUEST);
	    }
		return empleadoRepository.findByAllEstado(estado);
	}

	@Override
	@Transactional(readOnly = true)
	public EmpleadoEntity findOne(String search) throws ResponseException {
		EmpleadoEntity Empleado;
		Empleado = empleadoRepository.findSearchEmpleado(search);
		if (Empleado != null) {
			return Empleado;
		}
		throw new  ResponseException(String.format("Producto with id or name '%s' not found", search),
		        HttpStatus.NOT_FOUND);
	}

	@Override
	@Transactional
    public EmpleadoEntity create(EmpleadoEntity empleadoEntity) throws ResponseException {
		
	    String contrasenaEncriptada = passwordEncoder.encode(empleadoEntity.getPassword());
	    empleadoEntity.setPassword(contrasenaEncriptada);
	    
		try {
			return empleadoRepository.save(empleadoEntity);
		} catch (Exception ex) {
            throw new ResponseException("Unexpected error, check server logs", HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

	@Override
	@Transactional
	public EmpleadoEntity update(EmpleadoEntity empleadoEntity, String id) throws ResponseException {
		if (!uuidValidate.matcher(id).matches()) {
			throw new IllegalArgumentException(String.format("id '%s' must be a uuid", id));
		}
		EmpleadoEntity Empleado = empleadoRepository.findById(id).orElseThrow(() -> new ResponseException("Empleado not found", HttpStatus.NOT_FOUND));
        BeanUtils.copyProperties(empleadoEntity, Empleado);
        String contrasenaEncriptada = passwordEncoder.encode(empleadoEntity.getPassword());
        Empleado.setPassword(contrasenaEncriptada);
        Empleado.setId(id);
        return empleadoRepository.save(Empleado);
	}

	@Override
	@Transactional
	public void changeState(String id, Boolean value) throws ResponseException, IllegalArgumentException, Exception {
		if (!uuidValidate.matcher(id).matches()) {
			throw new IllegalArgumentException(String.format("id '%s' must be a uuid", id));
		}
		EmpleadoEntity EmpleadoEntity = empleadoRepository.findById(id)
				.orElseThrow(() -> new ResponseException("Empleado not found", HttpStatus.NOT_FOUND));
		EmpleadoEntity.setEstado(value);
		empleadoRepository.save(EmpleadoEntity);
	}

}