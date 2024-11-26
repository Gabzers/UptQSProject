package com.upt.upt.repository;

import com.upt.upt.entity.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    // Aqui você pode adicionar consultas personalizadas se necessário
}
