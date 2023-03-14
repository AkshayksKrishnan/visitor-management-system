package io.bootify.l11_visitor_management.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class VisitDTO {

    private Long id;

    @NotNull
    private VisitStatus status;


    private LocalDateTime inTime;

    private LocalDateTime outTime;

    @Size(max = 255)
    private String purpose;

    @Size(max = 255)
    private String urlOfImage;

    @Size(max = 255)
    private String noOfPeople;

    @NotNull
    private Long visitor;

    @NotNull
    private Long flat;

}
