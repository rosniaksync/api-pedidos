package com.devgabriel.pedidos.service;

import com.devgabriel.pedidos.domain.ClienteEntity;
import com.devgabriel.pedidos.dto.ClienteRequestDto;
import com.devgabriel.pedidos.dto.ClienteResponseDto;
import com.devgabriel.pedidos.dto.ClienteUpdateDto;
import com.devgabriel.pedidos.exception.BusinessException;
import com.devgabriel.pedidos.exception.ResourceNotFoundException;
import com.devgabriel.pedidos.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteResponseDto cadastrar(ClienteRequestDto dto) {
        if(repository.existsByEmail(dto.email())) {
            throw new BusinessException("E-mail já cadastrado");
        }
        ClienteEntity cliente = ClienteEntity.builder()
                .nome(dto.nome())
                .email(dto.email())
                .build();

        ClienteEntity clienteSalvo = repository.save(cliente);

        return new ClienteResponseDto(clienteSalvo);
    }

    public Page<ClienteResponseDto> listarTodos(Pageable pageable) {
        return repository.findAll(pageable)
                .map(ClienteResponseDto::new);
    }

    public ClienteResponseDto listarUm(Long id) {
        ClienteEntity cliente = buscarClientePorId(id);
        return new ClienteResponseDto(cliente);
    }

    @Transactional
    public ClienteResponseDto atualizar(Long id, ClienteUpdateDto dto) {
        ClienteEntity cliente = buscarClientePorId(id);
        if(dto.nome() != null) {
            cliente.setNome(dto.nome());
        }
        if(dto.email() != null) {
            cliente.setEmail(dto.email());
        }
        return new ClienteResponseDto(cliente);
    }

    public void deletar(Long id) {
        buscarClientePorId(id);
        repository.deleteById(id);
    }

    private ClienteEntity buscarClientePorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id: " + id));
    }
}