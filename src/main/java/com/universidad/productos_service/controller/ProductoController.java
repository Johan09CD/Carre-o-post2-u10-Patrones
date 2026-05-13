package com.universidad.productos_service.controller;

import com.universidad.productos_service.domain.Producto;
import com.universidad.productos_service.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService; // Code Smell: inyección por campo

    @GetMapping
    public List<Producto> listar() {
        return productoService.listar();
    }

    @GetMapping("/{id}")
    public Producto buscar(@PathVariable Long id) {
        return productoService.buscar(id); // Bug: puede retornar null → 200 vacío
    }

    @PostMapping
    public Producto crear(@RequestParam String nombre,
                          @RequestParam Double precio,
                          @RequestParam Integer stock) {
        return productoService.procesarProducto(nombre, precio, stock,
                null, true, null);
    }
}