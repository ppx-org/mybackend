package com.platb.test;

import com.platb.common.jdbc.MyDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestServ extends MyDaoSupport {

    private PersonRepository personRepository;

    @Autowired
    public TestServ(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void test() {
        List<Person> list = personRepository.findByLastName("sss");

        String s = list.get(0).firstName;

        System.out.println("---------003:" + s);
    }
}
