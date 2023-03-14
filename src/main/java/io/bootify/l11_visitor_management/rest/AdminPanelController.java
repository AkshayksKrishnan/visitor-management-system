package io.bootify.l11_visitor_management.rest;

import io.bootify.l11_visitor_management.model.UserDTO;
import io.bootify.l11_visitor_management.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@RestController
@RequestMapping("/api/admin-panel")
public class AdminPanelController {
    /*
    Create FLAT using csv file.
    Create User using csv file.
    update user.
    Create Gatekeeper.
    Generate report.
    */
    static private Logger LOGGER = LoggerFactory.getLogger(AdminPanelController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/create-user")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createUser(@RequestBody @Valid final UserDTO userDTO) {
        return new ResponseEntity<>(userService.create(userDTO), HttpStatus.CREATED);
    }

    @PostMapping("/user-csv/upload")
    public ResponseEntity<String> uploadUserCSV(@RequestParam("file") MultipartFile file){
        String message="";
        try{
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(),"UTF-8"));
            CSVParser csvParser = new CSVParser(fileReader,
                    CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for(CSVRecord csvRecord:csvRecords){
                UserDTO usersDTO = UserDTO.builder().
                        name(csvRecord.get("name")).
                        phone(csvRecord.get("phone")).
                        email(csvRecord.get("email")).
                        flat(Long.parseLong(csvRecord.get("flat"))).
                        address(Long.parseLong(csvRecord.get("address"))).
                        role(Long.parseLong(csvRecord.get("role"))).build();
                userService.create(usersDTO);
                LOGGER.info("Read user name :{}",usersDTO.getName());
            }
            message = "File uploaded successfully : "+file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (IOException e) {
            LOGGER.error("Exception occurred: {}",e);
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }
}
