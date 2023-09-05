package com.proyecto.idat.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	public static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(ResponseException.class)
	public ResponseEntity<ErrorDetails> handleException(ResponseException ex, WebRequest webRequest) {

		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), webRequest.getDescription(false));
		return new ResponseEntity<>(errorDetails, ex.getStatusCode());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetails> resourceNotFoundException(Exception exception, WebRequest webRequest) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
				webRequest.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String nameField = ((FieldError) error).getField();
			String messageString = error.getDefaultMessage();
			errors.put(nameField, messageString);

		});
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException exception,
			WebRequest webRequest) {

		String[] listPath = webRequest.getDescription(false).toString().split("/");
		ErrorDetails errorDetails = new ErrorDetails(new Date(),
				String.format("%s already exists in the database", listPath[2].toUpperCase()),
				webRequest.getDescription(false));
		logger.info("MONTO_TOTAL: " + exception.getMessage());
		exception.getStackTrace();
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
}
