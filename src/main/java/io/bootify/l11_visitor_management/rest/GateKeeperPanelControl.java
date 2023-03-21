package io.bootify.l11_visitor_management.rest;

import io.bootify.l11_visitor_management.model.CreateVisitorRequestDTO;
import io.bootify.l11_visitor_management.model.VisitDTO;
import io.bootify.l11_visitor_management.model.VisitorDTO;
import io.bootify.l11_visitor_management.service.GateKeeperPanelService;
import io.bootify.l11_visitor_management.service.VisitService;
import io.bootify.l11_visitor_management.service.VisitorService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/gatekeeper-panel")
public class GateKeeperPanelControl {

    @Autowired
    private VisitorService visitorService;

    @Autowired
    private GateKeeperPanelService gateKeeperPanelService;

    @Autowired
    private VisitService visitService;

    /*Visitor APIs*/
    @GetMapping("/getBy/visitor-Id")
    public ResponseEntity<VisitorDTO> getVisitor(@RequestParam final String idNum) {
        return ResponseEntity.ok(visitorService.getByIdNum(idNum));
    }

    @GetMapping("/getAll/visitors")
    public ResponseEntity<List<VisitorDTO>> getAllVisitors() {
        return ResponseEntity.ok(visitorService.findAll());
    }

    @PostMapping("/create/visitor")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createVisitor(@RequestBody  CreateVisitorRequestDTO createVisitorRequestDTO) {
        return new ResponseEntity<>(gateKeeperPanelService.create(createVisitorRequestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/update/visitor/{id}")
    public ResponseEntity<Void> updateVisitor(@PathVariable final Long id,
                                              @RequestBody @Valid final VisitorDTO visitorDTO) {
        visitorService.update(id, visitorDTO);
        return ResponseEntity.ok().build();
    }

    /*Visit APIs*/
    @PostMapping("/create/visit")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createVisit(@RequestBody @Valid final VisitDTO visitDTO) {
        return new ResponseEntity<>(visitService.create(visitDTO), HttpStatus.CREATED);
    }

    @GetMapping("/getAll/visits")
    public ResponseEntity<List<VisitDTO>> getAllVisits() {
        return ResponseEntity.ok(visitService.findAll());
    }

    @GetMapping("/getBy/visitId/{id}")
    public ResponseEntity<VisitDTO> getVisitById(@PathVariable @Valid final Long id){
        return ResponseEntity.ok(visitService.get(id));
    }

    @PostMapping("visit/entry/{id}")
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
