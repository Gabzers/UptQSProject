package com.upt.upt.service;

import com.upt.upt.entity.DirectorUnit;
import com.upt.upt.entity.YearUnit;
import com.upt.upt.entity.SemesterUnit;
import com.upt.upt.repository.DirectorUnitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class DirectorUnitServiceTest {

    @Mock
    private DirectorUnitRepository directorUnitRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private DirectorUnitService directorUnitService;

    private SemesterUnit firstSemester;
    private SemesterUnit secondSemester;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa os mocks

        // Inicialização dos semestres para os testes
        firstSemester = new SemesterUnit();
        firstSemester.setStartDate("2024-01-01");
        firstSemester.setEndDate("2024-06-30");

        secondSemester = new SemesterUnit();
        secondSemester.setStartDate("2024-07-01");
        secondSemester.setEndDate("2024-12-31");
    }

    @Test
    @DisplayName("Deve salvar um novo DirectorUnit")
    void shouldSaveDirector() {
        // Dado
        DirectorUnit director = new DirectorUnit();
        director.setName("John Doe");
        director.setUsername("jdoe");
        director.setPassword("secure123");
        director.setDepartment("Engineering");

        when(directorUnitRepository.save(director)).thenReturn(director);

        // Ação
        DirectorUnit savedDirector = directorUnitService.saveDirector(director);

        // Verificação
        assertThat(savedDirector).isNotNull();
        assertThat(savedDirector.getName()).isEqualTo("John Doe");
        verify(directorUnitRepository, times(1)).save(director);
    }

    @Test
    @DisplayName("Deve retornar todos os DirectorUnits")
    void shouldGetAllDirectors() {
        // Dado
        List<DirectorUnit> directors = Arrays.asList(
                new DirectorUnit(1L, "John Doe", "jdoe", "pass123", "Engineering", new ArrayList<>(), new ArrayList<>()),
                new DirectorUnit(2L, "Jane Doe", "jane", "pass456", "Science", new ArrayList<>(), new ArrayList<>())
        );
        when(directorUnitRepository.findAll()).thenReturn(directors);

        // Ação
        List<DirectorUnit> result = directorUnitService.getAllDirectors();

        // Verificação
        assertThat(result).hasSize(2);
        verify(directorUnitRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar um DirectorUnit pelo ID")
    void shouldGetDirectorById() {
        // Dado
        DirectorUnit director = new DirectorUnit(1L, "John Doe", "jdoe", "pass123", "Engineering", new ArrayList<>(), new ArrayList<>());
        when(directorUnitRepository.findById(1L)).thenReturn(Optional.of(director));

        // Ação
        Optional<DirectorUnit> result = directorUnitService.getDirectorById(1L);

        // Verificação
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("John Doe");
        verify(directorUnitRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar DirectorUnit inexistente")
    void shouldThrowExceptionWhenUpdatingNonExistentDirector() {
        // Dado
        when(directorUnitRepository.findById(99L)).thenReturn(Optional.empty());

        // Ação & Verificação
        assertThatThrownBy(() -> directorUnitService.updateDirector(99L, new HashMap<>()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid Director ID: 99");
        verify(directorUnitRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Deve retornar o ano mais recente de um diretor")
    void shouldGetMostRecentYear() {
        // Dado
        YearUnit year1 = new YearUnit(1L, firstSemester, secondSemester, "2023-07-01", "2023-07-10", null);
        YearUnit year2 = new YearUnit(2L, firstSemester, secondSemester, "2024-07-01", "2024-07-10", null);

        DirectorUnit director = new DirectorUnit();
        director.addAcademicYear(year1);
        director.addAcademicYear(year2);

        // Ação
        YearUnit mostRecentYear = directorUnitService.getMostRecentYear(director);

        // Verificação
        assertThat(mostRecentYear).isEqualTo(year2);
    }

    @Test
    @DisplayName("Deve retornar anos passados corretamente")
    void shouldReturnPastYears() {
        // Dado
        YearUnit year1 = new YearUnit(1L, firstSemester, secondSemester, "2023-07-01", "2023-07-10", null);
        YearUnit year2 = new YearUnit(2L, firstSemester, secondSemester, "2024-07-01", "2024-07-10", null);

        DirectorUnit director = new DirectorUnit();
        director.addAcademicYear(year1);
        director.addAcademicYear(year2);

        // Ação
        List<YearUnit> pastYears = director.getPastYears();

        // Verificação
        assertThat(pastYears).containsExactly(year1);
    }

    @Test
    @DisplayName("Deve criar um novo DirectorUnit com sucesso")
    void shouldCreateDirectorSuccessfully() {
        // Dado
        when(userService.usernameExists("uniqueUsername")).thenReturn(false);

        Map<String, String> params = Map.of(
                "director-name", "New Director",
                "director-username", "uniqueUsername",
                "director-password", "password123",
                "director-department", "Engineering"
        );

        // Ação
        DirectorUnit director = directorUnitService.createDirector(params);

        // Verificação
        assertThat(director.getName()).isEqualTo("New Director");
        assertThat(director.getUsername()).isEqualTo("uniqueUsername");
        assertThat(director.getPassword()).isEqualTo("password123");
        assertThat(director.getDepartment()).isEqualTo("Engineering");
    }
}
