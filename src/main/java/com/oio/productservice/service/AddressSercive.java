package com.oio.productservice.service;

import com.oio.productservice.dao.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressSercive {
    @Autowired
    private AddressRepository ar;

    public List<String> listOfSido() {
        return ar.listOfSiDo();
    }

    public List<String> listOfSiGunGu(String siDo) {
        return ar.listOfSiGunGu(siDo);
    }

    public  List<String> listOfEupMyeonRo(String siDo, String siGunGu) {
        return ar.listOfEupMyeonRo(siDo, siGunGu);
    }
}
