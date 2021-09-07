package com.platb.test;


import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalTime;

class TestExample {
    @Id
    Integer exampleId;
    String exampleName;
    String exampleType;
    LocalDate exampleDate;
    LocalTime exampleTime;

}