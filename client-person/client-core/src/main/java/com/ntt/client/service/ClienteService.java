package com.ntt.client.service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntt.client.common.ClientConstants;
import com.ntt.client.entity.Cliente;
import com.ntt.client.repository.IClienteRepository;
import com.ntt.client.request.ClientRequest;
import com.ntt.client.utils.BaseResponseVo;
import com.ntt.client.vo.ClienteVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Lazy
@Service
@Slf4j
public class ClienteService implements IClienteService{

    @Autowired
    private IClienteRepository clienteRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Override
    @Transactional(readOnly = true)
    public BaseResponseVo getAllClientes() {
        List<Cliente> clienteList= clienteRepository.findAll();
        List<ClienteVO> cuentaVOList=clienteList.stream().map(cliente -> entityToVo(cliente)).collect(
            Collectors.toList());
        return BaseResponseVo.builder().data(cuentaVOList).build();

    }



    @Override
    @Transactional(readOnly = true)
    public BaseResponseVo getClienteById(Long id) {
        Optional<Cliente> cliente=clienteRepository.findById(id);
        if(cliente.isPresent()){
            return BaseResponseVo.builder().data(entityToVo(cliente.get())).build();
        }else{
            return BaseResponseVo.builder().message(ClientConstants.CLIENT_NOT_FOUND_MESSAGE).build();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BaseResponseVo getClienteByIdentification(String identification) {
        Optional<Cliente> cliente=clienteRepository.findByIdentificacion(identification);
        if(cliente.isPresent()){
            return BaseResponseVo.builder().data(entityToVo(cliente.get())).build();
        }else{
            return BaseResponseVo.builder().message(ClientConstants.CLIENT_NOT_FOUND_MESSAGE).build();
        }
    }

    @Override
    @Transactional
    public BaseResponseVo createCliente(ClienteVO cliente) {

        if (clienteRepository.existsByIdentificacion(cliente.getIdentificacion())) {
            return BaseResponseVo.builder().message(ClientConstants.EXIST_IDENTIFICATION_MESSAGE)
                .build();
        }
        String encryptedPassword = Base64.getEncoder().encodeToString(cliente.getPassword().getBytes());
        cliente.setPassword(encryptedPassword);

        Cliente clienteEntity= voToEntity(cliente);
        clienteRepository.save(clienteEntity);
        sendData(entityToVo(clienteEntity));
        return BaseResponseVo.builder().data(entityToVo(clienteEntity)).build() ;
    }

    private void sendData(ClienteVO clienteVO) {
        this.rabbitTemplate = rabbitTemplate;
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;
        try {
            json = objectMapper.writeValueAsString(clienteVO);
        } catch (JsonProcessingException e) {
            log.error("Error converting clientVO:"+e);
        }
        rabbitTemplate.convertAndSend("clienteExchange", "clienteRoutingKey", json);
    }


    @Override
    @Transactional
    public BaseResponseVo updateCliente( ClientRequest clienteDetails) {
        Optional<Cliente> clienteOptional=clienteRepository.findByIdentificacion(clienteDetails.getIdentificacion());
        if(clienteOptional.isEmpty()){
            return BaseResponseVo.builder().message(ClientConstants.CLIENT_NOT_FOUND_MESSAGE)
                .build();
        }
        if(null !=clienteDetails.getPassword()){
            String encryptedPassword = Base64.getEncoder().encodeToString(clienteDetails.getPassword().getBytes());
            clienteDetails.setPassword(encryptedPassword);
        }
        Cliente clienteEntity =clienteOptional.get();
        if(null!=clienteDetails.getPassword()){
            clienteEntity.setPassword(clienteDetails.getPassword());
        }
        if (clienteDetails.getNombre() != null) {
            clienteEntity.setNombre(clienteDetails.getNombre());
        }
        if (clienteDetails.getGenero() != null) {
            clienteEntity.setGenero(clienteDetails.getGenero());
        }
        if (clienteDetails.getEdad() > 0) {
            clienteEntity.setEdad(clienteDetails.getEdad());
        }
        if (clienteDetails.getIdentificacion() != null) {
            clienteEntity.setIdentificacion(clienteDetails.getIdentificacion());
        }
        if (clienteDetails.getDireccion() != null) {
            clienteEntity.setDireccion(clienteDetails.getDireccion());
        }
        if (clienteDetails.getTelefono() != null) {
            clienteEntity.setTelefono(clienteDetails.getTelefono());
        }

        clienteRepository.save(clienteEntity);
        ClienteVO clienteVO=entityToVo(clienteEntity);
        return BaseResponseVo.builder().data(clienteVO).build();

    }

    @Override
    @Transactional
    public BaseResponseVo deleteCliente(String identification) {
        Optional<Cliente> cuentaOptional=clienteRepository.findByIdentificacion(identification);
        if(cuentaOptional.isEmpty()){
            return BaseResponseVo.builder().message(ClientConstants.CLIENT_NOT_FOUND_MESSAGE)
                .build();
        }
        clienteRepository.deleteByIdentificacion(identification);
        return BaseResponseVo.builder().message(ClientConstants.SUCCESSFUL_CLIENT_DELETE)
            .build();

    }

    private ClienteVO entityToVo(Cliente cliente) {
        return ClienteVO.builder()
            .password(cliente.getPassword())
            .estado(cliente.isEstado())
            .id(cliente.getId())
            .nombre(cliente.getNombre())
            .genero(cliente.getGenero())
            .edad(cliente.getEdad())
            .identificacion(cliente.getIdentificacion())
            .direccion(cliente.getDireccion())
            .telefono(cliente.getTelefono())
            .build();
    }


    private Cliente voToEntity(ClienteVO cliente) {
        return Cliente.builder()
            .password(cliente.getPassword())
            .estado(cliente.isEstado())
            .id(cliente.getId())
            .nombre(cliente.getNombre())
            .genero(cliente.getGenero())
            .edad(cliente.getEdad())
            .identificacion(cliente.getIdentificacion())
            .direccion(cliente.getDireccion())
            .telefono(cliente.getTelefono())
            .build();
    }
}
