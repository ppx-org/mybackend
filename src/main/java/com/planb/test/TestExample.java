
package com.planb.test;


import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.jdbc.repository.query.Query;


class TestExample {
    @Id
    Integer exampleId;
    String exampleName;
    String exampleType;

    LocalDate exampleDate;

    LocalTime exampleTime;
   
    @ReadOnlyProperty
    String subName;

}