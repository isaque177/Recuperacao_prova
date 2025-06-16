package com.example.recuperacao_prova.controller;

import com.example.recuperacao_prova.entity.Bibliotecario;
import com.example.recuperacao_prova.repository.BibliotecarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bibliotecarios")
public class BibliotecarioController {
    
    @Autowired
    private BibliotecarioRepository bibliotecarioRepository;
    
    // Listar todos os bibliotecários
    @GetMapping
    public List<Bibliotecario> listarTodos() {
        return bibliotecarioRepository.findAll();
    }
    
    // Buscar bibliotecário por ID
    @GetMapping("/{id}")
    public ResponseEntity<Bibliotecario> buscarPorId(@PathVariable Long id) {
        Optional<Bibliotecario> bibliotecario = bibliotecarioRepository.findById(id);
        return bibliotecario.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
    }
    
    // Cadastrar novo bibliotecário
    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Bibliotecario bibliotecario) {
        // Validação básica
        if (bibliotecario.getNome() == null || bibliotecario.getNome().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Nome é obrigatório");
        }
        if (bibliotecario.getEmail() == null || bibliotecario.getEmail().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email é obrigatório");
        }
        
        // Verificar se email já existe
        if (bibliotecarioRepository.existsByEmail(bibliotecario.getEmail())) {
            return ResponseEntity.badRequest().body("Email já cadastrado");
        }
        
        Bibliotecario salvo = bibliotecarioRepository.save(bibliotecario);
        return ResponseEntity.ok(salvo);
    }
    
    // Atualizar bibliotecário
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Bibliotecario bibliotecario) {
        Optional<Bibliotecario> existente = bibliotecarioRepository.findById(id);
        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        bibliotecario.setId(id);
        Bibliotecario atualizado = bibliotecarioRepository.save(bibliotecario);
        return ResponseEntity.ok(atualizado);
    }
    
    // Deletar bibliotecário
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (bibliotecarioRepository.existsById(id)) {
            bibliotecarioRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}