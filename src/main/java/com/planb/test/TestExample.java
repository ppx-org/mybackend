
package com.planb.test;


import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Embedded;

import java.time.LocalDate;
import java.time.LocalTime;


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