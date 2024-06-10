package com.ntt.client.service;

import java.util.List;
import java.util.Optional;
import com.ntt.client.entity.Cliente;
import com.ntt.client.request.ClientRequest;
import com.ntt.client.utils.BaseResponseVo;
import com.ntt.client.vo.ClienteVO;

public interface IClienteService {

    BaseResponseVo getAllClientes() ;

    BaseResponseVo getClienteById(Long id) ;

    BaseResponseVo getClienteByIdentification(String identification);

    BaseResponseVo createCliente(ClienteVO cliente);

    BaseResponseVo updateCliente(ClientRequest clienteDetails);

    BaseResponseVo deleteCliente(String identification) ;
}
