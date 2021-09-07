package com.platb.test;


import org.springframework.data.annotation.Id;

import java.time.LocalDate;

class Person {
    @Id
    Long id;
    String firstName;
    LocalDate dob;
}