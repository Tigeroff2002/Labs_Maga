package org.example.models;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Crossroad {
    @XmlElement(name = "id", namespace = "http://example.com/schema")
    private int id;

    @XmlElement(name = "city_id", namespace = "http://example.com/schema")
    private int cityId;

    @XmlElement(name = "coord", namespace = "http://example.com/schema")
    private String coord;

    @XmlElement(name = "type", namespace = "http://example.com/schema")
    private String type;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCityId() { return cityId; }
    public void setCityId(int cityId) { this.cityId = cityId; }
    public String getCoord() { return coord; }
    public void setCoord(String coord) { this.coord = coord; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}