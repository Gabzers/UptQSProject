package com.upt.upt.service;

import com.upt.upt.entity.AssessmentUnit;
import com.upt.upt.entity.CoordinatorUnit;
import com.upt.upt.entity.CurricularUnit;
import com.upt.upt.entity.YearUnit;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PdfService {

    public byte[] generatePdfForYearAndSemester(CoordinatorUnit coordinator, Integer year, Integer semester) {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Add logo and message in the same paragraph
            Image logo = Image.getInstance("src/main/resources/static/images/upt-logo.png");
            logo.scaleToFit(30, 30); // Adjust logo size
            logo.setAlignment(Element.ALIGN_LEFT);

            Font smallFont = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
            Paragraph logoAndMessage = new Paragraph();
            logoAndMessage.add(new Chunk(logo, 0, -10));
            logoAndMessage.add(new Chunk(" 2024 Universidade Portucalense Infante D. Henrique", smallFont));
            logoAndMessage.setAlignment(Element.ALIGN_LEFT);
            document.add(logoAndMessage);

            document.add(new Paragraph(" ")); // Add a blank line

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font tableHeaderFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font tableBodyFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

            Paragraph title = new Paragraph(coordinator.getCourse() + " - Assessment Map - Year " + year + " - " + (semester == 1 ? "1st Semester" : "2nd Semester"), titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" ")); // Add a blank line

            PdfPTable table = new PdfPTable(10);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{5, 4, 2, 3, 3, 2, 3, 3, 5, 2});

            addTableHeader(table, tableHeaderFont);
            addRows(table, coordinator, year, semester, tableBodyFont);

            document.add(table);
            document.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return out.toByteArray();
    }

    private void addTableHeader(PdfPTable table, Font font) {
        Stream.of("UC Name", "Assessment Type", "Weight", "Exam Period", "Computer Required", "Class Time", "Start Time", "End Time", "Rooms", "Minimum Grade")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setPhrase(new Phrase(columnTitle, font));
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setPadding(8);
                    table.addCell(header);
                });
    }

    private void addRows(PdfPTable table, CoordinatorUnit coordinator, Integer year, Integer semester, Font font) {
        YearUnit currentYear = coordinator.getDirectorUnit().getCurrentYear();
        List<AssessmentUnit> assessments = (semester == 1 ? currentYear.getFirstSemester().getCurricularUnits() : currentYear.getSecondSemester().getCurricularUnits()).stream()
                .filter(uc -> uc.getYear().equals(year) && coordinator.getCurricularUnits().contains(uc))
                .flatMap(uc -> uc.getAssessments().stream())
                .sorted((a1, a2) -> a1.getStartTime().compareTo(a2.getStartTime()))
                .collect(Collectors.toList());

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (AssessmentUnit assessment : assessments) {
            addCell(table, assessment.getCurricularUnit().getNameUC(), font);
            addCell(table, assessment.getType(), font);
            addCell(table, String.valueOf(assessment.getWeight()), font);
            addCell(table, assessment.getExamPeriod(), font);
            addCell(table, assessment.getComputerRequired() ? "Yes" : "No", font);
            addCell(table, assessment.getClassTime() ? "Yes" : "No", font);
            addCell(table, assessment.getStartTime().format(dateFormatter) + "\n" + assessment.getStartTime().format(timeFormatter), font);
            addCell(table, assessment.getEndTime().format(dateFormatter) + "\n" + assessment.getEndTime().format(timeFormatter), font);
            addCell(table, assessment.getRooms().stream().map(room -> room.getRoomNumber() + " - " + room.getBuilding()).collect(Collectors.joining("\n")), font);
            addCell(table, String.valueOf(assessment.getMinimumGrade()), font);
        }
    }

    private void addCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(8);
        table.addCell(cell);
    }
}