package com.universidad.productos_service;

import com.universidad.productos_service.domain.Producto;
import com.universidad.productos_service.repository.ProductoRepository;
import com.universidad.productos_service.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ProductoServiceTest {

	@Mock
	private ProductoRepository repo;

	@InjectMocks
	private ProductoService productoService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void procesarProducto_nombreVacio_lanzaExcepcion() {
		assertThrows(IllegalArgumentException.class, () ->
				productoService.procesarProducto("", 10.0, 5, null, true, null)
		);
	}

	@Test
	void procesarProducto_nombreBlanco_lanzaExcepcion() {
		assertThrows(IllegalArgumentException.class, () ->
				productoService.procesarProducto("   ", 10.0, 5, null, true, null)
		);
	}

	@Test
	void procesarProducto_precioNegativo_lanzaExcepcion() {
		assertThrows(IllegalArgumentException.class, () ->
				productoService.procesarProducto("Laptop", -1.0, 5, null, true, null)
		);
	}

	@Test
	void procesarProducto_precioExcesivo_lanzaExcepcion() {
		assertThrows(IllegalArgumentException.class, () ->
				productoService.procesarProducto("Laptop", 9999999.0, 5, null, true, null)
		);
	}

	@Test
	void procesarProducto_stockNegativo_lanzaExcepcion() {
		assertThrows(IllegalArgumentException.class, () ->
				productoService.procesarProducto("Laptop", 100.0, -1, null, true, null)
		);
	}

	@Test
	void procesarProducto_datosValidos_guardaProducto() {
		Producto mockProducto = new Producto();
		mockProducto.setId(1L);
		mockProducto.setNombre("Laptop");
		mockProducto.setPrecio(1500.0);
		mockProducto.setStock(10);

		when(repo.save(any(Producto.class))).thenReturn(mockProducto);

		Producto resultado = productoService.procesarProducto(
				"Laptop", 1500.0, 10, null, true, null
		);

		assertNotNull(resultado);
		assertEquals("Laptop", resultado.getNombre());
	}

	@Test
	void buscar_idInexistente_lanzaExcepcion() {
		when(repo.findById(99L)).thenReturn(Optional.empty());
		assertThrows(NoSuchElementException.class, () ->
				productoService.buscar(99L)
		);
	}

	@Test
	void getEstado_stockNull_retornaDesconocido() {
		Producto p = new Producto();
		p.setStock(null);
		assertEquals("DESCONOCIDO", p.getEstado());
	}

	@Test
	void getEstado_stockCero_retornaAgotado() {
		Producto p = new Producto();
		p.setStock(0);
		assertEquals("AGOTADO", p.getEstado());
	}

	@Test
	void getEstado_stockBajo_retornaBajo() {
		Producto p = new Producto();
		p.setStock(3);
		assertEquals("BAJO", p.getEstado());
	}
}