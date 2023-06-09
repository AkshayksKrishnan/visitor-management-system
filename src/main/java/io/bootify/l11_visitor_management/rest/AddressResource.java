package io.bootify.l11_visitor_management.rest;

import io.bootify.l11_visitor_management.model.AddressDTO;
import io.bootify.l11_visitor_management.service.AddressService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping(value = "/api/address", produces = MediaType.APPLICATION_JSON_VALUE)
public class AddressResource {

    private final AddressService addressService;

    public AddressResource(final AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public ResponseEntity<List<AddressDTO>> getAllAddress() {
        return ResponseEntity.ok(addressService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDTO> getAddress(@PathVariable final Long id) {
        return ResponseEntity.ok(addressService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createAddress(@RequestBody @Valid final AddressDTO addressDTO) {
        return new ResponseEntity<>(addressService.create(addressDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAddress(@PathVariable final Long id, @RequestBody @Valid final AddressDTO addressDTO) {
        addressService.update(id, addressDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAddress(@PathVariable final Long id) {
        addressService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
