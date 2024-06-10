package com.ntt.transaction.repository;


import java.time.LocalDateTime;
import java.util.List;
import com.ntt.transaction.entity.Cuenta;
import com.ntt.transaction.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMovimientosRepository   extends JpaRepository<Movimiento, Long> {

    List<Movimiento> findByCuentaAndFechaBetween(Cuenta cuenta, LocalDateTime startDate, LocalDateTime endDate);

}
