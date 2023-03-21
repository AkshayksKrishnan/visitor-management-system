package io.bootify.l11_visitor_management.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter
@Setter
public class VisitDTO implements Serializable {

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
