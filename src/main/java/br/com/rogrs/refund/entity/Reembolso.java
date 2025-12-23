package br.com.rogrs.refund.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class Reembolso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Paciente paciente;
    @NotBlank
    private String numeroSr;
    @NotBlank
    private String notaFiscal;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoTerapia tipoTerapia;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal valorNotaFiscal;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal valorReembolso;

    private Boolean srCancelado;

    private String comprovante;
    private String protocolo;

    private String arquivoNotaFiscal;
    private String arquivoComprovante;

    @Column(nullable = false)
    private LocalDate dataPedido;


    @PrePersist
    protected void prePersist() {
        if (this.dataPedido == null) {
            this.dataPedido = LocalDate.now();
        }

        if (this.srCancelado == null) {
            this.srCancelado = Boolean.FALSE;
        }
    }

}
