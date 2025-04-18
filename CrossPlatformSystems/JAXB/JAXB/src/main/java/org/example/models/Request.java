package org.example.models;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Request {
    @XmlElement(name = "id", namespace = "http://example.com/schema")
    private int id;

    @XmlElement(name = "user_id", namespace = "http://example.com/schema")
    private int userId;

    @XmlElement(name = "start_point", namespace = "http://example.com/schema")
    private String startPoint;

    @XmlElement(name = "end_point", namespace = "http://example.com/schema")
    private String endPoint;

    @XmlElement(name = "datetime", namespace = "http://example.com/schema")
    private String datetime;

    @XmlElement(name = "preference_parameter", namespace = "http://example.com/schema")
    private String preferenceParameter;

    @XmlElement(name = "status", namespace = "http://example.com/schema")
    private String status;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getStartPoint() { return startPoint; }
    public void setStartPoint(String startPoint) { this.startPoint = startPoint; }
    public String getEndPoint() { return endPoint; }
    public void setEndPoint(String endPoint) { this.endPoint = endPoint; }
    public String getDatetime() { return datetime; }
    public void setDatetime(String datetime) { this.datetime = datetime; }
    public String getPreferenceParameter() { return preferenceParameter; }
    public void setPreferenceParameter(String preferenceParameter) { this.preferenceParameter = preferenceParameter; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}