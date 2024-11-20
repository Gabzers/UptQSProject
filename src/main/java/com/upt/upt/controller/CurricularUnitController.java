package com.upt.upt.controller;

import com.upt.upt.entity.CurricularUnit;
import com.upt.upt.service.CurricularUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller for managing CurricularUnit entities.
 * Provides endpoints for CRUD operations.
 */

@RestController
@RequestMapping("/curricularUnit")
public class CurricularUnitController {

    private final CurricularUnitService curricularUnitService;

    @Autowired
    public CurricularUnitController(CurricularUnitService curricularUnitService) {
        this.curricularUnitService = curricularUnitService;
    }

    /**
     * Create a new curricular unit.
     *
     * @param curricularUnit the curricular unit to create
     * @return the ResponseEntity with the newly created entity
     */
    @PostMapping
    public ResponseEntity<CurricularUnit> saveCurricularUnit(@RequestBody CurricularUnit curricularUnit) {
        CurricularUnit newCurricularUnit = curricularUnitService.saveCurricularUnit(curricularUnit);
        return ResponseEntity.ok(newCurricularUnit);
    }

    /**
     * Get all curricular units.
     *
     * @return the list of curricular units
     */
    @GetMapping
    public List<CurricularUnit> getAllCurricularUnits() {
        return curricularUnitService.getAllCurricularUnits();
    }

    /**
     * Get a curricular unit by ID.
     *
     * @param id the ID of the curricular unit
     * @return the curricular unit, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<CurricularUnit> getCurricularUnitById(@PathVariable Long id) {
        Optional<CurricularUnit> curricularUnit = curricularUnitService.getCurricularUnitById(id);
        return curricularUnit.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Update a curricular unit by ID.
     *
     * @param id the ID of the curricular unit to update
     * @param updatedCurricularUnit the updated data
     * @return the updated curricular unit, or 404 if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<CurricularUnit> updateCurricularUnit(
            @PathVariable Long id,
            @RequestBody CurricularUnit updatedCurricularUnit) {
        CurricularUnit updatedUnit = curricularUnitService.updateCurricularUnit(id, updatedCurricularUnit);
        return ResponseEntity.ok(updatedUnit);
    }

    /**
     * Delete a curricular unit by ID.
     *
     * @param id the ID of the curricular unit to delete
     * @return success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCurricularUnit(@PathVariable Long id) {
        curricularUnitService.deleteCurricularUnit(id);
        return ResponseEntity.ok("Curricular unit deleted successfully.");
    }
}
