package com.oio.productservice.dao;

import com.oio.productservice.jpa.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

    @Query(nativeQuery=true,
            value = "SELECT DISTINCT si_do FROM address")
    List<String> listOfSiDo();

    @Query(nativeQuery = true,
            value = "SELECT DISTINCT si_gun_gu FROM address WHERE si_do = :siDo")
    List<String> listOfSiGunGu(@Param("siDo") String siDo);

    @Query(nativeQuery=true,
            value ="SELECT DISTINCT eup_myeon_ro FROM address WHERE si_do = :siDo AND si_gun_gu = :siGunGu")
    List<String> listOfEupMyeonRo(@Param("siDo") String siDo, @Param("siGunGu") String siGunGu);


    @Query(nativeQuery=true,
            value = "SELECT * FROM address WHERE address_no=:addressNo")
    AddressEntity findByAddressNo(@Param("addressNo")Long addressNo);

    AddressEntity findBySiDoAndSiGunGuAndEupMyeonRo(String siDo, String siGunGu, String eupMyeonRo);
}