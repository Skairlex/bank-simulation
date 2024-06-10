package com.ntt.transaction.service;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntt.transaction.common.TransactionsConstants;
import com.ntt.transaction.entity.Cuenta;
import com.ntt.transaction.exception.TransactionException;
import com.ntt.transaction.repository.ICuentaRepository;
import com.ntt.transaction.vo.ClienteVO;
import com.ntt.transaction.vo.CuentaVO;
import com.ntt.transaction.utils.BaseResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Lazy
@Service
@Slf4j
public class CuentaService implements ICuentaService {

    @Autowired
    private ICuentaRepository cuentaRepository;


    private ObjectMapper objectMapper;

    @Override
    @Transactional(readOnly = true)
    public BaseResponseVo getAllCuentas() {
        List<Cuenta> accountList=cuentaRepository.findAll();
        List<CuentaVO> cuentaVOList=accountList.stream().map(cuenta -> entityToVo(cuenta)).collect(Collectors.toList());
        return BaseResponseVo.builder().data(cuentaVOList).build();
    }

    @Override
    @Transactional(readOnly = true)
    public BaseResponseVo getCuentaById(Long id) {
        Optional<Cuenta> account=cuentaRepository.findById(id);
        if(account.isPresent()){
            return BaseResponseVo.builder().data(entityToVo(account.get())).build();
        }else{
            return BaseResponseVo.builder().message(TransactionsConstants.ACCOUNT_NOT_FOUND_MESSAGE).build();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BaseResponseVo getCuentaByAccountNumber(String accountNumber) {
        Optional<Cuenta> account=cuentaRepository.findByNumeroCuenta(accountNumber);
        if(account.isPresent()){
            return BaseResponseVo.builder().data(entityToVo(account.get())).build();
        }else{
            return BaseResponseVo.builder().message(TransactionsConstants.ACCOUNT_NOT_FOUND_MESSAGE).build();
        }
    }



    @Override
    @Transactional
    public BaseResponseVo createCuenta(CuentaVO cuenta) {
        try {
            if (cuentaRepository.existsByNumeroCuenta(cuenta.getNumeroCuenta())) {
                return BaseResponseVo.builder().message(TransactionsConstants.EXIST_ACCOUNT_MESSAGE)
                    .build();
            }
            Cuenta account = voToEntity(cuenta);
            cuentaRepository.save(account);
            cuenta.setId(account.getId());
            return BaseResponseVo.builder().data(cuenta).build();
        }catch (Exception e){
            throw new TransactionException("Error creating account: ",e);
        }
    }

    @Override
    @Transactional
    public BaseResponseVo updateCuenta(CuentaVO cuenta) {
        Optional<Cuenta> cuentaOptional=cuentaRepository.findByNumeroCuenta(cuenta.getNumeroCuenta());
        if(cuentaOptional.isEmpty()){
            return BaseResponseVo.builder().message(TransactionsConstants.ACCOUNT_NOT_FOUND_MESSAGE)
                .build();
        }
        Cuenta account = voToEntity(cuenta);
        if (null == cuenta.getId()) account.setId(cuentaOptional.get().getId());
        cuentaRepository.save(account);
        return BaseResponseVo.builder().data(cuenta).build();
    }

    @Override
    @Transactional
    public BaseResponseVo deleteCuenta(Long id) {
        Optional<Cuenta> cuentaOptional=cuentaRepository.findById(id);
        if(cuentaOptional.isEmpty()){
            return BaseResponseVo.builder().message(TransactionsConstants.ACCOUNT_NOT_FOUND_MESSAGE)
                .build();
        }
        cuentaRepository.deleteById(id);
        log.info(TransactionsConstants.ACCOUNT_DELETED_MESSAGE.format(id.toString()));
        //log.info("Account with id number"+id+" was deleted");
        return BaseResponseVo.builder().message(TransactionsConstants.SUCCESSFUL_ACCOUNT_DELETE)
            .build();

    }


    public CuentaVO entityToVo(Cuenta cuenta){
        return CuentaVO.builder()
            .id(cuenta.getId())
            .numeroCuenta(cuenta.getNumeroCuenta())
            .tipoCuenta(cuenta.getTipoCuenta())
            .saldoInicial(cuenta.getSaldoInicial())
            .estado(cuenta.getEstado())
            .build();
    }

    public Cuenta voToEntity(CuentaVO cuenta){
        return Cuenta.builder()
            .id(cuenta.getId())
            .numeroCuenta(cuenta.getNumeroCuenta())
            .tipoCuenta(cuenta.getTipoCuenta())
            .saldoInicial(cuenta.getSaldoInicial())
            .estado(cuenta.getEstado())
            .clienteId(cuenta.getClienteId())
            .build();
    }

    @RabbitListener(queues = "clienteQueue")
    public void receiveCliente(String cliente) {
        log.info("CLIENTE RECIBIDO: "+cliente);

        this.objectMapper = new ObjectMapper();
        ClienteVO clienteVO = null;
        try {
            clienteVO = objectMapper.readValue(cliente, ClienteVO.class);
        } catch (JsonProcessingException e) {
            log.error("Error converting clientVO:"+e);
        }
        CuentaVO cuenta=CuentaVO.builder().tipoCuenta("AHORROS").numeroCuenta(generateRandomTenDigitNumber()+"").saldoInicial(
            BigDecimal.valueOf(0)).estado("ACTIVO").clienteId(clienteVO.getId()).build();
        createCuenta(cuenta);

    }

    public static long generateRandomTenDigitNumber() {
        Random random = new Random();

        // Generar un número aleatorio de 10 dígitos
        long min = 1000000000L;  // Valor mínimo de 10 dígitos
        long max = 9999999999L;  // Valor máximo de 10 dígitos
        long randomTenDigitNumber = min + ((long)(random.nextDouble() * (max - min)));

        return randomTenDigitNumber;
    }

}
