package io.bootify.l11_visitor_management.rest;

import io.bootify.l11_visitor_management.model.VisitDTO;
import io.bootify.l11_visitor_management.service.VisitService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-panel")
public class UserPanelController {

    @Autowired
    private VisitService visitService;

    @PostMapping("/approve-visit/{visitID}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity approveVisit(@PathVariable final Long visitID, @RequestHeader  final Long userID )
    {
        visitService.approveVisit(visitID,userID);
        return new ResponseEntity<>("Approved", HttpStatus.CREATED);
    }

    @PostMapping("/reject-visit/{visitID}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity rejectVisit(@PathVariable final Long visitID, @RequestHeader  final Long userID )
    {
        visitService.rejectVisit(visitID,userID);
        return new ResponseEntity<>("Rejected", HttpStatus.CREATED);
    }

    @GetMapping("/get-pending-visits")
    @ApiResponse(responseCode="200")
    public ResponseEntity<List<VisitDTO>> getPendingVisits(@RequestHeader final Long userID){
        return ResponseEntity.ok(visitService.getPendingVisitsByUser(userID));
    }

}
