package com.SecurityDemo.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SecurityDemo.demo.model.Facility;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Integer> {

	public Facility findByName(String name);

}
