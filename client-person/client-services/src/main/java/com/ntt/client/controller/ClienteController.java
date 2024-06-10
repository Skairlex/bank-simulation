package com.ntt.client.controller;


import java.util.List;
import java.util.stream.Collectors;
import com.ntt.client.request.ClientRequest;
import com.ntt.client.service.IClienteService;
import com.ntt.client.utils.BaseResponseVo;
import com.ntt.client.vo.ClienteVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private IClienteService clienteService;

    @GetMapping
    public ResponseEntity<BaseResponseVo> getAllClientes() {
        return ResponseEntity.ok(clienteService.getAllClientes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseVo> getClienteById(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(clienteService.getClienteById(id));

    }

    @GetMapping("/identificacion/{id}")
    public ResponseEntity<BaseResponseVo> getClienteByIdentification(@PathVariable String id) {
        return ResponseEntity.ok(clienteService.getClienteByIdentification(id));

    }

    @PostMapping
    public ResponseEntity<BaseResponseVo> createCliente(@Valid @RequestBody ClienteVO cliente,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(BaseResponseVo.builder().code(400).errors(errors).build());
        }
        return ResponseEntity.ok(clienteService.createCliente(cliente));
    }

    @PutMapping
    public ResponseEntity<BaseResponseVo> updateCliente(@Valid @RequestBody ClientRequest clienteDetails,
    BindingResult bindingResult) {
            if (bindingResult.hasErrors()) {
                List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(BaseResponseVo.builder().code(400).errors(errors).build());
            }
        return ResponseEntity.ok(clienteService.updateCliente( clienteDetails));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponseVo> deleteCliente(@PathVariable String identification) {
        return ResponseEntity.ok(clienteService.deleteCliente(identification));
    }
}
