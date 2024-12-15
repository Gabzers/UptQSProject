package com.upt.upt.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import java.util.Objects;
import java.util.List;
import java.util.ArrayList;

/**
 * RoomUnit class represents a room with its specific attributes, such as number, type, material, etc.
 * 
 * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
@Entity
@Table(name = "room_unit")
public class RoomUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @Column(name = "room_number", nullable = false)
    @NotNull
    private String roomNumber;

    @Column(name = "room_designation", nullable = false)
    @NotNull
    private String designation;

    @Column(name = "room_material_type", nullable = false)
    @NotNull
    private String materialType;

    @Column(name = "room_seats_count", nullable = false)
    @Min(0)
    private Integer seatsCount;

    @Column(name = "room_building", nullable = false)
    @NotNull
    private String building;

    @ManyToMany(mappedBy = "rooms")
    private List<AssessmentUnit> assessments = new ArrayList<>();

    /**
     * Default constructor.
     * 
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public RoomUnit() {}

    /**
     * Constructor with parameters.
     * 
     * @param id the ID of the room unit
     * @param roomNumber the room number
     * @param designation the designation of the room
     * @param materialType the material type of the room
     * @param seatsCount the number of seats in the room
     * @param building the building where the room is located
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public RoomUnit(Long id, String roomNumber, String designation, String materialType, Integer seatsCount, String building) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.designation = designation;
        this.materialType = materialType;
        this.seatsCount = seatsCount;
        this.building = building;
    }

    /**
     * Gets the ID of the room unit.
     * 
     * @return the ID of the room unit
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the room unit.
     * 
     * @param id the ID to set
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the room number.
     * 
     * @return the room number
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public String getRoomNumber() {
        return roomNumber;
    }

    /**
     * Sets the room number.
     * 
     * @param roomNumber the room number to set
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    /**
     * Gets the designation of the room.
     * 
     * @return the designation of the room
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * Sets the designation of the room.
     * 
     * @param designation the designation to set
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    /**
     * Gets the material type of the room.
     * 
     * @return the material type of the room
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public String getMaterialType() {
        return materialType;
    }

    /**
     * Sets the material type of the room.
     * 
     * @param materialType the material type to set
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    /**
     * Gets the number of seats in the room.
     * 
     * @return the number of seats in the room
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public Integer getSeatsCount() {
        return seatsCount;
    }

    /**
     * Sets the number of seats in the room.
     * 
     * @param seatsCount the number of seats to set
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void setSeatsCount(Integer seatsCount) {
        this.seatsCount = seatsCount;
    }

    /**
     * Gets the building where the room is located.
     * 
     * @return the building where the room is located
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public String getBuilding() {
        return building;
    }

    /**
     * Sets the building where the room is located.
     * 
     * @param building the building to set
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void setBuilding(String building) {
        this.building = building;
    }

    /**
     * Gets the list of assessments associated with the room.
     * 
     * @return the list of assessments
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public List<AssessmentUnit> getAssessments() {
        return assessments;
    }

    /**
     * Sets the list of assessments associated with the room.
     * 
     * @param assessments the list of assessments to set
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void setAssessments(List<AssessmentUnit> assessments) {
        this.assessments = assessments;
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
