package com.universidad.productos_service.service;

import com.universidad.productos_service.domain.Producto;
import com.universidad.productos_service.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    // Code Smell: inyección por campo en lugar de constructor
    @Autowired
    private ProductoRepository repo; // Code Smell: nombre genérico

    // Code Smell: método largo con múltiples responsabilidades (CC alta)
    public Producto procesarProducto(String n, Double p, Integer s,
                                     String cat, boolean activo, String proveedor) {
        Producto producto = new Producto();

        if (n == null || n.equals("")) { // Code Smell: debería usar isBlank()
            throw new IllegalArgumentException("nombre requerido");
        }
        if (p == null) {
            throw new IllegalArgumentException("precio requerido");
        } else if (p <= 0) {
            throw new IllegalArgumentException("precio invalido");
        } else if (p > 999999) {
            throw new IllegalArgumentException("precio excesivo");
        }
        if (s == null || s < 0) {
            throw new IllegalArgumentException("stock invalido");
        }

        producto.setNombre(n);
        producto.setPrecio(p);
        producto.setStock(s);

        // TODO: implementar lógica de categoría y proveedor
        return repo.save(producto);
    }

    public List<Producto> listar() {
        return repo.findAll();
    }

    // Bug: retorna null en lugar de lanzar excepción si no existe
    public Producto buscar(Long id) {
        return repo.findById(id).orElse(null);
    }
}