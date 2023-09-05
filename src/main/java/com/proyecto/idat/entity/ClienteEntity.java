package com.proyecto.idat.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "clientes")
@Data
@EqualsAndHashCode(callSuper=false)
public class ClienteEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "email", nullable = false, length = 120, unique = true)
	@NotNull(message = "email must not be null")
	@Email(message = "email is invalid")
	private String email;

	@Column(name = "pass", nullable = false, length = 100)
	@NotEmpty(message = "password must not be empty")
	@Size(min = 6, max = 100)
	private String password;

	@Column(name = "estado", nullable = false, columnDefinition = "bit(1) default 1")
	private Boolean estado;

	@Column(name = "nombre", nullable = false, length = 60)
	@NotBlank(message = "nombre must not be empty")

	@Size(min = 3, max = 60, message = "nombre must be between 3 to 20 characters")
	private String nombre;

	@Column(name = "dni", nullable = false, length = 8, unique = true)
	@NotNull(message = "dni must not be null")
	@Size(min = 8, max = 8, message = "dni must be to 8 digits")
	@Pattern(regexp = "[0-9]*", message = "dni must be a number")
	private String dni;

	@Column(name = "apellido", nullable = false, length = 60)
	@NotNull(message = "apellido must not be null")
	@Size(min = 3, max = 60, message = "apellido must be between 3 to 20 characters")
	private String apellido;

	@PrePersist
	public void prePersist() {
		id = UUID.randomUUID().toString();
		if (estado == null) {
			estado = true;
		}
	}

	public void updateClient(ClienteEntity client) {
		if (client.getNombre() != null)
			this.setNombre(client.getNombre());
		if (client.getApellido() != null)
			this.setApellido(client.getApellido());
		if (client.getDni() != null)
			this.setDni(client.getDni());
		if (client.getEmail() != null)
			this.setEmail(client.getEmail());
		if (client.getEstado() != null)
			this.setEstado(client.getEstado());
		if (client.getPassword() != null)
			this.setPassword(client.getPassword());
	}

}