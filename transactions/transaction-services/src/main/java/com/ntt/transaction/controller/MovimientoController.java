package com.ntt.transaction.controller;

import java.util.List;
import java.util.stream.Collectors;
import com.ntt.transaction.request.MovimientoUpdateRequest;
import com.ntt.transaction.vo.MovimientoVO;
import com.ntt.transaction.service.IMovimientoService;
import com.ntt.transaction.utils.BaseResponseVo;
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
@RequestMapping("/api/movimientos")
public class MovimientoController {

    @Autowired
    private IMovimientoService movimientosService;

    @GetMapping
    public ResponseEntity<BaseResponseVo> getAllMovimientos() {
        return ResponseEntity.ok(movimientosService.getAllMovimientos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseVo> getMovimientoById(@PathVariable Long id) {
        return ResponseEntity.ok(movimientosService.getMovimientoById(id));
    }

    @PostMapping
    public ResponseEntity<BaseResponseVo> createMovimiento(@Valid @RequestBody MovimientoVO movimiento,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(BaseResponseVo.builder().code(400).errors(errors).build());
        }

            return  ResponseEntity.ok(movimientosService.createMovimiento(movimiento));
    }

    @PutMapping
    public ResponseEntity<BaseResponseVo> updateMovimiento(@Valid @RequestBody MovimientoUpdateRequest movimientosDetails,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(BaseResponseVo.builder().code(400).errors(errors).build());
        }
        return ResponseEntity.ok(movimientosService.updateMovimiento( movimientosDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponseVo> deleteMovimiento(@PathVariable Long id) {
        return ResponseEntity.ok(movimientosService.deleteMovimiento(id));
    }

}
