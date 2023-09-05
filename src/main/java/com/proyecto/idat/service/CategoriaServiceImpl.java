package com.proyecto.idat.service;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.BeanUtils;
import com.proyecto.idat.entity.CategoriaEntity;
import com.proyecto.idat.exception.ResponseException;
import com.proyecto.idat.repository.CategoriaRepository;

@Service
public class CategoriaServiceImpl implements CategoriaService {

	Pattern uuidValidate = Pattern.compile("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$");

	@Autowired
	private CategoriaRepository categoriaRepository;


	@Override
	@Transactional(readOnly = true)
	public List<CategoriaEntity> findAll(String estado) {
		if (!estado.equals("all") && !estado.equals("active") && !estado.equals("inactive")) {
	        throw new ResponseException("Invalid value for estado. Allowed values are: all, active, inactive", HttpStatus.BAD_REQUEST);
	    }
		return categoriaRepository.findByAllEstado(estado);
	}

	@Override
	@Transactional(readOnly = true)
	public CategoriaEntity findOne(String search) throws ResponseException {
		CategoriaEntity categoria = categoriaRepository.findSearchCategoria(search);
        if(categoria != null){
            return categoria;
        }
        throw new  ResponseException(String.format("Categoria with id or name '%s' not found", search),
        HttpStatus.NOT_FOUND);
	}

	@Override
	@Transactional
    public CategoriaEntity create(CategoriaEntity categoriaEntity) throws ResponseException {
        try {
            return categoriaRepository.save(categoriaEntity);
        } catch (Exception ex) {
            throw new ResponseException("Unexpected error, check server logs", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
	@Override
	@Transactional
	public CategoriaEntity update(CategoriaEntity categoriaEntity, String id) throws ResponseException {
		if (!uuidValidate.matcher(id).matches()) {
			throw new IllegalArgumentException(String.format("id '%s' must be a uuid", id));
		}
		CategoriaEntity Categoria = categoriaRepository.findById(id).orElseThrow(()-> new ResponseException("Categoria not found", HttpStatus.NOT_FOUND));
		BeanUtils.copyProperties(categoriaEntity, Categoria);
		Categoria.setId(id);
		return categoriaRepository.save(Categoria);
	}

	@Override
	@Transactional
    public void delete(String id) throws ResponseException, IllegalArgumentException, Exception {
        if (!uuidValidate.matcher(id).matches()) {
            throw new IllegalArgumentException(String.format("id '%s' must be a uuid", id));
        }
        CategoriaEntity categoriaEntity = categoriaRepository.findById(id).orElseThrow(()-> new ResponseException("Categoria not found", HttpStatus.NOT_FOUND));
        categoriaEntity.setEstado(false);
        categoriaRepository.save(categoriaEntity);
    }

}
