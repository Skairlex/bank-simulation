package com.ntt.transaction.service;

import com.ntt.transaction.entity.Cuenta;
import com.ntt.transaction.vo.CuentaVO;
import com.ntt.transaction.utils.BaseResponseVo;

public interface ICuentaService {

    BaseResponseVo getAllCuentas();

    BaseResponseVo getCuentaById(Long id);

    BaseResponseVo getCuentaByAccountNumber(String accountNumber);

    BaseResponseVo createCuenta(CuentaVO cuenta);

    BaseResponseVo updateCuenta(CuentaVO cuentaDetails);

    BaseResponseVo deleteCuenta(Long id);

    CuentaVO entityToVo(Cuenta cuenta);
}
