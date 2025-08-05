package edu.HanYi.repository;

import edu.HanYi.model.ContactRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRequestRepository extends JpaRepository<ContactRequest, Integer> {
}
