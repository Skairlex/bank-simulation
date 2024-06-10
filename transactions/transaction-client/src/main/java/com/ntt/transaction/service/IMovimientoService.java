package com.ntt.transaction.service;


import com.ntt.transaction.entity.Movimiento;
import com.ntt.transaction.request.MovimientoUpdateRequest;
import com.ntt.transaction.vo.MovimientoVO;
import com.ntt.transaction.utils.BaseResponseVo;

public interface IMovimientoService {

    BaseResponseVo getAllMovimientos();

    BaseResponseVo getMovimientoById(Long id);

    BaseResponseVo createMovimiento(MovimientoVO movimientos);

    BaseResponseVo updateMovimiento(MovimientoUpdateRequest movimientoDetails);

    BaseResponseVo deleteMovimiento(Long id);

    MovimientoVO entityToVo(Movimiento movimiento);
}
