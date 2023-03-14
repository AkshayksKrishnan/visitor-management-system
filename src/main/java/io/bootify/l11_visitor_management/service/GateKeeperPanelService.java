package io.bootify.l11_visitor_management.service;

import io.bootify.l11_visitor_management.model.AddressDTO;
import io.bootify.l11_visitor_management.model.CreateVisitorRequestDTO;
import io.bootify.l11_visitor_management.model.VisitorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GateKeeperPanelService {

    @Autowired
    private AddressService addressService;

    @Autowired
    private  VisitorService visitorService;

    public Long create(CreateVisitorRequestDTO createVisitorRequestDTO) {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setLine1(createVisitorRequestDTO.getLine1());
        addressDTO.setLine2(createVisitorRequestDTO.getLine2());
        addressDTO.setPincode(createVisitorRequestDTO.getPincode());
        addressDTO.setCity(createVisitorRequestDTO.getCity());
        addressDTO.setState(createVisitorRequestDTO.getState());
        addressDTO.setCountry(createVisitorRequestDTO.getCountry());

        Long addressId = addressService.create(addressDTO);
        VisitorDTO visitorDTO = VisitorDTO.builder().name(createVisitorRequestDTO.getName())
                .phone(createVisitorRequestDTO.getPhone())
                .idType(createVisitorRequestDTO.getIdType())
                .idNumber(createVisitorRequestDTO.getIdNumber())
                .address(addressId).build();

        return visitorService.create(visitorDTO);
    }
}
