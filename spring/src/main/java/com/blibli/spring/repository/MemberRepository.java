package com.blibli.spring.repository;

import com.blibli.spring.model.Member;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemberRepository extends MongoRepository<Member, String> {

    Member findByUsername(String username);
}
