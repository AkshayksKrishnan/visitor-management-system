package io.bootify.l11_visitor_management.rest;

import io.bootify.l11_visitor_management.domain.User;
import io.bootify.l11_visitor_management.model.VisitDTO;
import io.bootify.l11_visitor_management.service.VisitService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/user-panel")
public class UserPanelController {

    @Autowired
    private VisitService visitService;

    @PostMapping("/approve-visit/{visitID}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity approveVisit(@PathVariable final Long visitID )
    {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        visitService.approveVisit(visitID,user.getId());
        return new ResponseEntity<>("Approved", HttpStatus.CREATED);
    }

    @PostMapping("/reject-visit/{visitID}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity rejectVisit(@PathVariable final Long visitID)
    {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        visitService.rejectVisit(visitID,user.getId());
        return new ResponseEntity<>("Rejected", HttpStatus.CREATED);
    }

    @GetMapping("/get-pending-visits")
    @ApiResponse(responseCode="200")
    public ResponseEntity<List<VisitDTO>> getPendingVisits(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(visitService.getPendingVisitsByUser(user.getId()));
    /*for getting pending visits , flat id will be better,
                so using flat_id as key in redis cache*/
    }

}
