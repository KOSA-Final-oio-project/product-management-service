package com.oio.productservice.controller;

import com.oio.productservice.service.AddressSercive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/address")
@RestController
public class AddressContoller {
    @Autowired
    private AddressSercive as;

    @GetMapping("/siDoList")
    public List<String> siDOList() {
        return as.listOfSido();
    }

    @GetMapping(value = "/siGunGuList/{siDo}", produces = "application/json;charset=UTF-8")
    public List<String> siGunGuList(@PathVariable String siDo){
        return as.listOfSiGunGu(siDo);
    }

    @GetMapping(value = "/eupMyeonRoList/{siDo}/{siGunGu}", produces = "application/json;charset=UTF-8")
    public List<String> eupMyeonRoList(@PathVariable String siDo, @PathVariable String siGunGu){
        return  as.listOfEupMyeonRo(siDo, siGunGu);
    }
}
