package com.clebergraciano.crud.services;

import com.clebergraciano.crud.dtos.ProdutoDto;
import com.clebergraciano.crud.entities.Produto;
import com.clebergraciano.crud.exceptions.ResourceNotFundException;
import com.clebergraciano.crud.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    @Autowired
    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public ProdutoDto create(ProdutoDto produtoDto){
        ProdutoDto produtoDtoRetorno = ProdutoDto.create(produtoRepository.save(Produto.create(produtoDto)));
        return produtoDtoRetorno;
    }

    public Page<ProdutoDto> findAll(Pageable pageable){
        var page = produtoRepository.findAll(pageable);
        return page.map(this::converterToProdutoDto);
    }

    private ProdutoDto converterToProdutoDto(Produto produto){
        return ProdutoDto.create(produto);
    }


    public ProdutoDto findById(Long id){
        var entity = produtoRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFundException("No records found for this ID"));
        return ProdutoDto.create(entity);
    }

    public ProdutoDto update(ProdutoDto produtoDto){
        final Optional<Produto> optionalProduto = produtoRepository.findById(produtoDto.getId());
        if (!optionalProduto.isPresent()){
            new ResourceNotFundException("No records found for this ID");
        }
        return ProdutoDto.create(produtoRepository.save(Produto.create(produtoDto)));
    }

    public void delete (Long id){
        var entity = produtoRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFundException("No records found for this ID"));
        produtoRepository.delete(entity);
    }



}
