package com.ntt.transaction.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.ntt.transaction.common.TransactionsConstants;
import com.ntt.transaction.entity.Cuenta;
import com.ntt.transaction.entity.Movimiento;
import com.ntt.transaction.repository.ICuentaRepository;
import com.ntt.transaction.repository.IMovimientosRepository;
import com.ntt.transaction.request.MovimientoUpdateRequest;
import com.ntt.transaction.vo.CuentaVO;
import com.ntt.transaction.vo.MovimientoVO;
import com.ntt.transaction.utils.BaseResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Lazy
@Service
@Slf4j
public class MovimientoService implements IMovimientoService{


    @Autowired
    private IMovimientosRepository movimientosRepository;

    @Autowired
    private ICuentaRepository cuentaRepository;

    @Autowired ICuentaService cuentaService;

    @Override
    @Transactional(readOnly = true)
    public BaseResponseVo getAllMovimientos() {
        List<Movimiento> movimientoList= movimientosRepository.findAll();
        List<MovimientoVO> movimientoVOList= movimientoList.stream().map((mov->entityToVo(mov))).collect(
            Collectors.toList());
        return BaseResponseVo.builder().data(movimientoVOList).build();
    }

    @Override
    @Transactional(readOnly = true)
    public BaseResponseVo getMovimientoById(Long id) {
        Optional<Movimiento> movimientoOptional= movimientosRepository.findById(id);
        if(movimientoOptional.isPresent()){
            return BaseResponseVo.builder().data(entityToVo(movimientoOptional.get())).build();
        }else{
            return BaseResponseVo.builder().data(TransactionsConstants.TRANSACTION_NOT_FOUND_MESSAGE).build();
        }

    }

    @Override
    @Transactional
    public BaseResponseVo createMovimiento(MovimientoVO movimientoVO) {

        Optional<Cuenta> cuentaOpt = cuentaRepository.findByNumeroCuenta(movimientoVO.getCuenta().getNumeroCuenta());
        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();
            if(!movimientoVO.getCuenta().getTipoCuenta().equals(cuenta.getTipoCuenta())){
                return BaseResponseVo.builder().message(TransactionsConstants.DIFFERENT_TYPE_ACCOUNT).build();
            }
            BigDecimal saldoActual = cuenta.getSaldoInicial();
            BigDecimal valorMovimiento = movimientoVO.getValor();

            if (movimientoVO.getTipoMovimiento().equalsIgnoreCase(TransactionsConstants.OPTION_DEPOSIT)) {
                saldoActual = saldoActual.add(valorMovimiento);
            } else if (movimientoVO.getTipoMovimiento().equalsIgnoreCase(TransactionsConstants.OPTION_WITHDRAWAL) ) {//|| movimiento.getTipoMovimiento().equalsIgnoreCase("TRANSFERENCIA")) {
                if (saldoActual.compareTo(valorMovimiento) < 0) {
                    log.info("Saldo no disponible:"+cuenta.toString());
                    return BaseResponseVo.builder().message(TransactionsConstants.INSUFFICIENT_BALANCE_MESSAGE).build();
                }
                saldoActual = saldoActual.subtract(valorMovimiento);
            }else{
                return BaseResponseVo.builder().message(TransactionsConstants.TRANSACTION_TYPE_NOT_ALLOWED).build();
            }

            cuenta.setSaldoInicial(saldoActual);
            cuentaRepository.save(cuenta);
            Movimiento movimiento=voToEntity(movimientoVO);
            movimiento.setSaldo(saldoActual);
            movimiento.setFecha(LocalDateTime.now());
            movimiento.setCuenta(cuenta);
            movimientosRepository.save(movimiento);

            CuentaVO cuentaVOResponse= cuentaService.entityToVo(cuenta);
            MovimientoVO movimientoVOResponse= entityToVo(movimiento);
            movimientoVOResponse.setCuenta(cuentaVOResponse);
            return BaseResponseVo.builder().data(movimientoVOResponse).build();
        } else {
            log.info(TransactionsConstants.ACCOUNT_NOT_FOUND_MESSAGE+" : " +movimientoVO.toString());
            return BaseResponseVo.builder().message(TransactionsConstants.ACCOUNT_NOT_FOUND_MESSAGE).build();
        }
    }



    @Override
    @Transactional
    public BaseResponseVo updateMovimiento(MovimientoUpdateRequest movimientoDetails) {

        Optional<Movimiento> movimientoOptional = movimientosRepository.findById(movimientoDetails.getId());
        if(movimientoOptional.isEmpty()){
            return BaseResponseVo.builder().message(TransactionsConstants.TRANSACTION_NOT_FOUND_MESSAGE)
                .build();
        }

        Movimiento movimientoToUpdate=movimientoOptional.get();
        Cuenta cuentaActual=movimientoToUpdate.getCuenta();
        if(movimientoToUpdate.getSaldo().compareTo(cuentaActual.getSaldoInicial()) != 0){
            return BaseResponseVo.builder().message(TransactionsConstants.TRANSACTION_UPDATE_ERROR_MESSAGE).build();
        }
        //Regresa al anterior estado
        BigDecimal saldoActual = cuentaActual.getSaldoInicial();
        if (movimientoToUpdate.getTipoMovimiento().equalsIgnoreCase(TransactionsConstants.OPTION_WITHDRAWAL)) {
            saldoActual = saldoActual.add(movimientoToUpdate.getValor());
        } else if (movimientoToUpdate.getTipoMovimiento().equalsIgnoreCase(TransactionsConstants.OPTION_DEPOSIT) ) {
            if (saldoActual.compareTo(movimientoToUpdate.getValor()) < 0) {
                log.info("Saldo no disponible:"+cuentaActual.toString());
                return BaseResponseVo.builder().message(TransactionsConstants.INSUFFICIENT_BALANCE_MESSAGE).build();
            }
            saldoActual = saldoActual.subtract(movimientoToUpdate.getValor());
        }else{
            return BaseResponseVo.builder().message(TransactionsConstants.TRANSACTION_TYPE_NOT_ALLOWED).build();
        }
        //Actualiza con la nueva transacción
        // saldoActual = cuentaActual.getSaldoInicial();
        BigDecimal valorMovimiento = movimientoDetails.getValor();
        if (movimientoDetails.getTipoMovimiento().equalsIgnoreCase(TransactionsConstants.OPTION_DEPOSIT)) {
            saldoActual = saldoActual.add(valorMovimiento);
        } else if (movimientoDetails.getTipoMovimiento().equalsIgnoreCase(TransactionsConstants.OPTION_WITHDRAWAL) ) {
            if (saldoActual.compareTo(valorMovimiento) < 0) {
                log.info("Saldo no disponible:"+cuentaActual.toString());
                return BaseResponseVo.builder().message(TransactionsConstants.INSUFFICIENT_BALANCE_MESSAGE).build();
            }
            saldoActual = saldoActual.subtract(valorMovimiento);
        }else{
            return BaseResponseVo.builder().message(TransactionsConstants.TRANSACTION_TYPE_NOT_ALLOWED).build();
        }

        cuentaActual.setSaldoInicial(saldoActual);
        cuentaRepository.save(cuentaActual);
        movimientoToUpdate.setTipoMovimiento(movimientoDetails.getTipoMovimiento());
        movimientoToUpdate.setFecha(movimientoDetails.getFecha());
        movimientoToUpdate.setValor(movimientoDetails.getValor());
        movimientoToUpdate.setSaldo(saldoActual);

        movimientosRepository.save(movimientoToUpdate);
        return BaseResponseVo.builder().data(entityToVo(movimientoToUpdate)).build();
    }

    @Override
    @Transactional
    public BaseResponseVo deleteMovimiento(Long id) {
        Optional<Movimiento> movimientoOptional = movimientosRepository.findById(id);
        if(movimientoOptional.isEmpty()){
            return BaseResponseVo.builder().message(TransactionsConstants.TRANSACTION_NOT_FOUND_MESSAGE)
                .build();
        }

        Movimiento movimiento = movimientoOptional.get();
        Cuenta cuenta = movimiento.getCuenta();
        if(movimiento.getSaldo().compareTo(cuenta.getSaldoInicial()) != 0){
            return BaseResponseVo.builder().message(TransactionsConstants.TRANSACTION_DELETE_ERROR_MESSAGE).build();
        }
        if (null == cuenta) {
            return BaseResponseVo.builder()
                .message(TransactionsConstants.ACCOUNT_NOT_FOUND_MESSAGE)
                .build();
        }
        BigDecimal saldoActual = cuenta.getSaldoInicial();
        if (movimiento.getTipoMovimiento().equalsIgnoreCase(TransactionsConstants.OPTION_WITHDRAWAL)) {
            saldoActual = saldoActual.add(movimiento.getValor());
        } else if (movimiento.getTipoMovimiento().equalsIgnoreCase(TransactionsConstants.OPTION_DEPOSIT) ) {
            if (saldoActual.compareTo(movimiento.getValor()) < 0) {
                log.info("Saldo no disponible:"+cuenta.toString());
                return BaseResponseVo.builder().message(TransactionsConstants.INSUFFICIENT_BALANCE_MESSAGE).build();
            }
            saldoActual = saldoActual.subtract(movimiento.getValor());
        }else{
            return BaseResponseVo.builder().message(TransactionsConstants.TRANSACTION_TYPE_NOT_ALLOWED).build();
        }

        cuenta.setSaldoInicial(saldoActual);
        cuentaRepository.save(cuenta);

        movimientosRepository.deleteById(id);
        log.info(TransactionsConstants.TRANSACTION_DELETED_MESSAGE.format(id.toString()));
        return BaseResponseVo.builder().message(TransactionsConstants.SUCCESSFUL_TRANSACTION_DELETE)
            .build();
    }

    public MovimientoVO entityToVo(Movimiento movimiento){
        return MovimientoVO.builder()
            .id(movimiento.getId())
            .fecha(movimiento.getFecha())
            .tipoMovimiento(movimiento.getTipoMovimiento())
            .valor(movimiento.getValor())
            .saldo(movimiento.getSaldo())
            .build();
    }

    public Movimiento voToEntity(MovimientoVO movimiento){
        return Movimiento.builder()
            .id(movimiento.getId())
            .fecha(movimiento.getFecha())
            .tipoMovimiento(movimiento.getTipoMovimiento())
            .valor(movimiento.getValor())
            .saldo(movimiento.getSaldo())
            .build();
    }
}
