package org.example.models;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class RoadSegment {
    @XmlElement(name = "id", namespace = "http://example.com/schema")
    private int id;

    @XmlElement(name = "city_id", namespace = "http://example.com/schema")
    private int cityId;

    @XmlElement(name = "start_coord", namespace = "http://example.com/schema")
    private String startCoord;

    @XmlElement(name = "end_coord", namespace = "http://example.com/schema")
    private String endCoord;

    @XmlElement(name = "length", namespace = "http://example.com/schema")
    private double length;

    @XmlElement(name = "quality_factor", namespace = "http://example.com/schema")
    private double qualityFactor;

    @XmlElement(name = "user_intensity", namespace = "http://example.com/schema")
    private double userIntensity;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCityId() { return cityId; }
    public void setCityId(int cityId) { this.cityId = cityId; }
    public String getStartCoord() { return startCoord; }
    public void setStartCoord(String startCoord) { this.startCoord = startCoord; }
    public String getEndCoord() { return endCoord; }
    public void setEndCoord(String endCoord) { this.endCoord = endCoord; }
    public double getLength() { return length; }
    public void setLength(double length) { this.length = length; }
    public double getQualityFactor() { return qualityFactor; }
    public void setQualityFactor(double qualityFactor) { this.qualityFactor = qualityFactor; }
    public double getUserIntensity() { return userIntensity; }
    public void setUserIntensity(double userIntensity) { this.userIntensity = userIntensity; }

    public double getRating() {
        return qualityFactor * userIntensity;
    }
}
