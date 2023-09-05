package com.proyecto.idat.service;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.BeanUtils;
import com.proyecto.idat.entity.ProductoEntity;
import com.proyecto.idat.exception.ResponseException;
import com.proyecto.idat.repository.CategoriaRepository;
import com.proyecto.idat.repository.ProductoRepository;

@Service
public class ProductoServiceImpl implements ProductoService {

	Pattern uuidValidate = Pattern.compile("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$");

	@Autowired
	private ProductoRepository productoRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;


	@Override
	@Transactional(readOnly = true)
	public List<ProductoEntity> findAll(String estado) {
		if (!estado.equals("all") && !estado.equals("active") && !estado.equals("inactive")) {
	        throw new ResponseException("Invalid value for estado. Allowed values are: all, active, inactive", HttpStatus.BAD_REQUEST);
	    }
		return productoRepository.findByAllEstado(estado);
	}

	@Override
	@Transactional(readOnly = true)
	public ProductoEntity findOne(String search) throws ResponseException {
		ProductoEntity Producto = productoRepository.findSearchProducto(search);
        if(Producto != null){
            return Producto;
        }
        throw new  ResponseException(String.format("Producto with id or name '%s' not found", search),
        HttpStatus.NOT_FOUND);
	}

	@Override
	@Transactional
    public ProductoEntity create(ProductoEntity productoEntity) throws ResponseException {
		String idCategoria = productoEntity.getCategoria().getId();
		categoriaRepository.findById(idCategoria).orElseThrow(()->
			new ResponseException(String.format("Categoria with id '%s'  not found", idCategoria ), HttpStatus.NOT_FOUND));
        try {
            return productoRepository.save(productoEntity);
        } catch (Exception ex) {
            throw new ResponseException("Unexpected error, check server logs", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
	@Override
	@Transactional
	public ProductoEntity update(ProductoEntity productoEntity, String id) throws ResponseException {
		if (!uuidValidate.matcher(id).matches()) {
			throw new IllegalArgumentException(String.format("id '%s' must be a uuid", id));
		}
		ProductoEntity Producto = productoRepository.findById(id).orElseThrow(()-> new ResponseException("Producto not found", HttpStatus.NOT_FOUND));
		String idCategoria = productoEntity.getCategoria().getId();
		categoriaRepository.findById(idCategoria).orElseThrow(()->
			new ResponseException(String.format("Categoria with id '%s'  not found", idCategoria ), HttpStatus.NOT_FOUND));

		BeanUtils.copyProperties(productoEntity, Producto);
		Producto.setId(id);
		return productoRepository.save(Producto);
	}

	@Override
	@Transactional
    public void delete(String id) throws ResponseException, IllegalArgumentException, Exception {
        if (!uuidValidate.matcher(id).matches()) {
            throw new IllegalArgumentException(String.format("id '%s' must be a uuid", id));
        }
        ProductoEntity ProductoEntity = productoRepository.findById(id).orElseThrow(()-> new ResponseException("Producto not found", HttpStatus.NOT_FOUND));
        ProductoEntity.setEstado(false);
        productoRepository.save(ProductoEntity);
    }

}
