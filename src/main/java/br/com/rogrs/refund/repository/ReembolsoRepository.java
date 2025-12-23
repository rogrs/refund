package br.com.rogrs.refund.repository;

import br.com.rogrs.refund.entity.Reembolso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReembolsoRepository extends JpaRepository<Reembolso, Long> {
    List<Reembolso> findByDataPedidoBetween(LocalDate inicio, LocalDate fim);

    List<Reembolso> findByNumeroSrContainingIgnoreCase(String numeroSr);
}
