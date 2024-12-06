package com.upt.upt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.Objects;

/**
 * RoomUnit class represents a room with its specific attributes, such as number, type, material, etc.
 */
@Entity
@Table(name = "room_unit")
public class RoomUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id; // ID of the room

    @Column(name = "room_number", nullable = false)
    @NotNull
    private String roomNumber; // Room number (e.g., 101, A2)

    @Column(name = "room_designation", nullable = false)
    @NotNull
    private String designation; // Room designation (e.g., auditorium, classroom, laboratory)

    @Column(name = "room_material_type", nullable = false)
    @NotNull
    private String materialType; // Type of material (e.g., desks or computers)

    @Column(name = "room_seats_count", nullable = false)
    @Min(0)
    private Integer seatsCount; // Number of seats in the room

    @Column(name = "room_building", nullable = false)
    @NotNull
    private String building; // Building information (e.g., floor number or different building)

    // Constructors
    public RoomUnit() {}

    public RoomUnit(Long id, String roomNumber, String designation, String materialType, Integer seatsCount, String building) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.designation = designation;
        this.materialType = materialType;
        this.seatsCount = seatsCount;
        this.building = building;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public Integer getSeatsCount() {
        return seatsCount;
    }

    public void setSeatsCount(Integer seatsCount) {
        this.seatsCount = seatsCount;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomUnit roomUnit = (RoomUnit) o;
        return Objects.equals(id, roomUnit.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "RoomUnit{" +
                "id=" + id +
                ", roomNumber='" + roomNumber + '\'' +
                ", designation='" + designation + '\'' +
                ", materialType='" + materialType + '\'' +
                ", seatsCount=" + seatsCount +
                ", building='" + building + '\'' +
                '}';
    }
}
