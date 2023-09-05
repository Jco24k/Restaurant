package com.proyecto.idat.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "empleados")
@Data
@EqualsAndHashCode(callSuper = false)
public class EmpleadoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "telefono", nullable = false, length = 9, unique = true)
	@NotNull(message = "telefono must not be null")
	@Size(min = 9, max = 9, message = "telefono must be to 9 digits")
	@Pattern(regexp = "^9\\d{8}$", message = "telefono must be a number valid")
	private String telefono;

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

	@Column(name = "direccion", nullable = false, length = 60)
	@NotNull(message = "direccion must not be null")
	private String direccion;

	@Column(name = "salario", nullable = false, columnDefinition = "Decimal(10,2)")
	@NotNull(message = "salario must not be null")
	@Positive()
	private BigDecimal salario;

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


	@ManyToMany(fetch = FetchType.EAGER, targetEntity = RolEntity.class)
	@JoinTable(name="empleados_roles", joinColumns = @JoinColumn(name="empleado_id"), inverseJoinColumns = @JoinColumn(name="role_id"))
	private Set<RolEntity> roles;

	@JsonIgnore
	@OneToMany(mappedBy = "empleado")
	private Collection<FondoInicialEntity> fondoInicialEntities;

	@PrePersist
	public void prePersist() {
		id = UUID.randomUUID().toString();
		if (estado == null) {
			estado = true;
		}
	}

	public void update(EmpleadoEntity emp) {
		if (emp.getNombre() != null)
			this.setNombre(emp.getNombre());
		if (emp.getApellido() != null)
			this.setApellido(emp.getApellido());
		if (emp.getDni() != null)
			this.setDni(emp.getDni());
		if (emp.getEmail() != null)
			this.setEmail(emp.getEmail());
		if (emp.getEstado() != null)
			this.setEstado(emp.getEstado());
		if (emp.getPassword() != null)
			this.setPassword(emp.getPassword());
		if (emp.getSalario() != null)
			this.setSalario(emp.getSalario());
	}

}
