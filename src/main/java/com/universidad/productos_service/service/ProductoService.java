package com.universidad.productos_service.service;

import com.universidad.productos_service.domain.Producto;
import com.universidad.productos_service.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductoService {

    // Fix 1: inyección por constructor en lugar de @Autowired en campo
    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // Fix 2: método principal reducido, extrae validación
    public Producto procesarProducto(String nombre, Double precio, Integer stock,
                                     String cat, boolean activo, String proveedor) {
        validarDatos(nombre, precio, stock);
        Producto producto = new Producto();
        producto.setNombre(nombre.strip());
        producto.setPrecio(precio);
        producto.setStock(stock);
        return productoRepository.save(producto);
    }

    // Fix 3: método privado extraído para reducir complejidad ciclomática
    private void validarDatos(String nombre, Double precio, Integer stock) {
        // Fix 4: isBlank() en lugar de equals("")
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        if (precio == null || precio <= 0)
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        if (precio > 999999)
            throw new IllegalArgumentException("El precio excede el máximo permitido");
        if (stock == null || stock < 0)
            throw new IllegalArgumentException("El stock no puede ser negativo");
    }

    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    // Fix 5: lanza excepción en lugar de retornar null
    public Producto buscar(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("Producto no encontrado: " + id));
    }
}