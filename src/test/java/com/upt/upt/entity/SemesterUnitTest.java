package com.upt.upt.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for SemesterUnit.
 */
class SemesterUnitTest {

    private SemesterUnit semesterUnit;

    @BeforeEach
    void setUp() {
        // Criação de uma instância de SemesterUnit para os testes
        semesterUnit = new SemesterUnit();
        semesterUnit.setId(1L);
        semesterUnit.setStartDate("2024-01-01");
        semesterUnit.setEndDate("2024-06-30");
        semesterUnit.setExamPeriodStart("2024-06-01");
        semesterUnit.setExamPeriodEnd("2024-06-15");
        semesterUnit.setResitPeriodStart("2024-07-01");
        semesterUnit.setResitPeriodEnd("2024-07-15");

        // Inicializa a lista de curricularUnits
        semesterUnit.setCurricularUnits(new ArrayList<>());
    }

    @Test
    void testGettersAndSetters() {
        // Verificar se os valores definidos são recuperados corretamente
        assertEquals(1L, semesterUnit.getId());
        assertEquals("2024-01-01", semesterUnit.getStartDate());
        assertEquals("2024-06-30", semesterUnit.getEndDate());
        assertEquals("2024-06-01", semesterUnit.getExamPeriodStart());
        assertEquals("2024-06-15", semesterUnit.getExamPeriodEnd());
        assertEquals("2024-07-01", semesterUnit.getResitPeriodStart());
        assertEquals("2024-07-15", semesterUnit.getResitPeriodEnd());
    }

    @Test
    void testAddCurricularUnit() {
        // Mock de CurricularUnit
        CurricularUnit mockCurricularUnit = Mockito.mock(CurricularUnit.class);

        // Adicionar CurricularUnit e verificar a relação bidirecional
        semesterUnit.addCurricularUnit(mockCurricularUnit);
        assertEquals(1, semesterUnit.getCurricularUnits().size());
        assertTrue(semesterUnit.getCurricularUnits().contains(mockCurricularUnit));

        // Verifica se o método setSemesterUnit foi chamado no CurricularUnit mockado
        Mockito.verify(mockCurricularUnit).setSemesterUnit(semesterUnit);
    }

    @Test
    void testRemoveCurricularUnit() {
        // Mock de CurricularUnit
        CurricularUnit mockCurricularUnit = Mockito.mock(CurricularUnit.class);

        // Adicionar e depois remover CurricularUnit
        semesterUnit.addCurricularUnit(mockCurricularUnit);
        semesterUnit.removeCurricularUnit(mockCurricularUnit);

        // Verificar que a lista está vazia após a remoção
        assertTrue(semesterUnit.getCurricularUnits().isEmpty());

        // Verifica se o método setSemesterUnit(null) foi chamado no CurricularUnit mockado
        Mockito.verify(mockCurricularUnit).setSemesterUnit(null);
    }

    @Test
    void testEqualsAndHashCode() {
        // Criar outro objeto igual
        SemesterUnit anotherSemesterUnit = new SemesterUnit();
        anotherSemesterUnit.setId(1L);

        // Verificar igualdade
        assertEquals(semesterUnit, anotherSemesterUnit);
        assertEquals(semesterUnit.hashCode(), anotherSemesterUnit.hashCode());

        // Alterar o ID e verificar desigualdade
        anotherSemesterUnit.setId(2L);
        assertNotEquals(semesterUnit, anotherSemesterUnit);
    }

    @Test
    void testToString() {
        String expected = "SemesterUnit{" +
                "id=1, startDate='2024-01-01', endDate='2024-06-30', " +
                "examPeriodStart='2024-06-01', examPeriodEnd='2024-06-15', " +
                "resitPeriodStart='2024-07-01', resitPeriodEnd='2024-07-15'}";
        assertEquals(expected, semesterUnit.toString());
    }
}
