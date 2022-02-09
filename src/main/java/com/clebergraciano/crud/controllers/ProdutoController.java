package com.clebergraciano.crud.controllers;

import com.clebergraciano.crud.dtos.ProdutoDto;
import com.clebergraciano.crud.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequestMapping("/products")
@RestController
public class ProdutoController {

    private final ProdutoService produtoService;
    private final PagedResourcesAssembler<ProdutoDto> assembler; //Para hetoas, para retornar o link de um determinado produto

    @Autowired
    public ProdutoController(ProdutoService produtoService, PagedResourcesAssembler<ProdutoDto> assembler) {
        this.produtoService = produtoService;
        this.assembler = assembler;
    }

    @GetMapping(value = "/{id}", produces = {"application/json", "application/xml", "application/x-yaml"})
    public ProdutoDto findById(@PathVariable(name = "id") Long id){
        ProdutoDto produtoDto = produtoService.findById(id);
        produtoDto.add(linkTo(methodOn(ProdutoController.class).findById(id)).withSelfRel());
        return produtoDto;
    }

    @GetMapping(produces = {"application/json", "application/xml", "application/x-yaml"})
    public ResponseEntity<?> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "limit", defaultValue = "12") int limit,
                                     @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "nome"));

        Page<ProdutoDto> produtos = produtoService.findAll(pageable);
        produtos.stream()
                .forEach(p -> p.add(linkTo(methodOn(ProdutoController.class).findById(p.getId())).withSelfRel()));

        PagedModel<EntityModel<ProdutoDto>> pagedModel = assembler.toModel(produtos);
        return new ResponseEntity<>(pagedModel, HttpStatus.OK);
    }

    @PostMapping(produces = {"application/json", "application/xml", "application/x-yaml"},
            consumes = {"application/json", "application/xml", "application/x-yaml"})
    public ProdutoDto create(@RequestBody ProdutoDto produtoDto){
        ProdutoDto proDto = produtoService.create(produtoDto);
        proDto.add(linkTo(methodOn(ProdutoController.class).findById(produtoDto.getId())).withSelfRel());
        return proDto;
    }

    @PutMapping(produces = {"application/json", "application/xml", "application/x-yaml"},
            consumes = {"application/json", "application/xml", "application/x-yaml"})
    public ProdutoDto update(@RequestBody ProdutoDto produtoDto){
        ProdutoDto proDto = produtoService.update(produtoDto);
        proDto.add(linkTo(methodOn(ProdutoController.class).findById(produtoDto.getId())).withSelfRel());
        return proDto;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id){
        produtoService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
