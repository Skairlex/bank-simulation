package com.ntt.transaction.controller;


import java.util.List;
import java.util.stream.Collectors;
import com.ntt.transaction.vo.CuentaVO;
import com.ntt.transaction.service.ICuentaService;
import com.ntt.transaction.utils.BaseResponseVo;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api/cuentas")
@Slf4j
public class CuentaController {


    @Autowired
    private ICuentaService cuentaService;

    @GetMapping
    public ResponseEntity<BaseResponseVo> getAllCuentas() {
        return ResponseEntity.ok(cuentaService.getAllCuentas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseVo> getCuentaById(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaService.getCuentaById(id));
    }

    @GetMapping("/number/{number}")
    public ResponseEntity<BaseResponseVo> getByAccountNumber(@PathVariable String number) {
        return ResponseEntity.ok(cuentaService.getCuentaByAccountNumber(number));
    }

    @PostMapping
    public ResponseEntity<BaseResponseVo> createCuenta(@Valid @RequestBody CuentaVO cuenta,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(BaseResponseVo.builder().code(400).errors(errors).build());
        }
        return ResponseEntity.ok(cuentaService.createCuenta(cuenta));
    }

    @PutMapping
    public ResponseEntity<BaseResponseVo> updateCuenta(@Valid  @RequestBody CuentaVO cuentaDetails,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(BaseResponseVo.builder().code(400).errors(errors).build());
        }

        return ResponseEntity.ok(cuentaService.updateCuenta(cuentaDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponseVo> deleteCuenta(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaService.deleteCuenta(id));
    }
}
