package autohouser;

import spireautomator.UMass;

public class Room {
    private int row;
    private String area;
    private String building;
    private String design;
    private String type;
    private String id;

    public Room() {
        this.row = -1;
        this.area = "";
        this.building = "";
        this.design = "";
        this.type = "";
        this.id = "";
    }

    public Room(int row, String building, String design, String type) {
        this();
        this.row = row;
        this.building = building;
        this.design = design;
        this.type = type;
        this.area = UMass.getResidentialArea(building);
    }

    public Room(int row, String area, String building, String design, String type) {
        this.row = row;
        this.area = area;
        this.building = building;
        this.design = design;
        this.type = type;
    }

    public int getRow() {
        return row;
    }

    public String getArea() {
        return area;
    }

    public String getBuilding() {
        return building;
    }

    public String getDesign() {
        return design;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }
}
