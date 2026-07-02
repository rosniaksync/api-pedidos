package com.devgabriel.pedidos.dto;
import com.devgabriel.pedidos.enums.StatusPedido;
import org.w3c.dom.stylesheets.LinkStyle;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PedidoResponseDto(
        Long id,
        ClienteResumoDto cliente,
        List<ItemPedidoResponseDto> itens,
        LocalDate dataPedido,
        StatusPedido status,
        BigDecimal valorTotal
) {}