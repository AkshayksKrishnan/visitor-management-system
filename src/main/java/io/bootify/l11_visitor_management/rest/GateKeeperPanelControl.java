package io.bootify.l11_visitor_management.rest;

import io.bootify.l11_visitor_management.model.CreateVisitorRequestDTO;
import io.bootify.l11_visitor_management.model.VisitDTO;
import io.bootify.l11_visitor_management.model.VisitorDTO;
import io.bootify.l11_visitor_management.service.GateKeeperPanelService;
import io.bootify.l11_visitor_management.service.VisitService;
import io.bootify.l11_visitor_management.service.VisitorService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gatekeeper-panel")
public class GateKeeperPanelControl {

    @Autowired
    private VisitorService visitorService;

    @Autowired
    private GateKeeperPanelService gateKeeperPanelService;

    @Autowired
    private VisitService visitService;

    @GetMapping("/getVisitorById")
    public ResponseEntity<VisitorDTO> getVisitor(@RequestParam final String idNum) {
        return ResponseEntity.ok(visitorService.getByIdNum(idNum));
    }

    @PostMapping("/create-visitor")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createVisitor(@RequestBody @Valid final CreateVisitorRequestDTO createVisitorRequestDTO) {
        return new ResponseEntity<>(gateKeeperPanelService.create(createVisitorRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/create-visit")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createVisit(@RequestBody @Valid final VisitDTO visitDTO) {
        return new ResponseEntity<>(visitService.create(visitDTO), HttpStatus.CREATED);
    }

    @PostMapping("/entry/{id}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity markEntry(@PathVariable final Long id) {
        visitService.markEntry(id);
        return new ResponseEntity<>("Updated",HttpStatus.CREATED);
    }

    @PostMapping("/exit/{id}")
    @ApiResponse(responseCode="200")
    public ResponseEntity markExit(@PathVariable final Long id) {
        visitService.markExit(id);
        return new ResponseEntity<>("Updated",HttpStatus.CREATED);
    }

}
