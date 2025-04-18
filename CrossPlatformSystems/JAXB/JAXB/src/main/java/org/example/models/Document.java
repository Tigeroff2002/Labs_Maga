package org.example.models;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "document", namespace = "http://example.com/schema")
@XmlAccessorType(XmlAccessType.FIELD)
public class Document {
    @XmlElement(name = "user", namespace = "http://example.com/schema")
    private User user;

    @XmlElement(name = "request", namespace = "http://example.com/schema")
    private Request request;

    @XmlElement(name = "crossroad", namespace = "http://example.com/schema")
    private Crossroad crossroad;

    @XmlElement(name = "roadSegment", namespace = "http://example.com/schema")
    private List<RoadSegment> roadSegments;
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Request getRequest() { return request; }
    public void setRequest(Request request) { this.request = request; }
    public Crossroad getCrossroad() { return crossroad; }
    public void setCrossroad(Crossroad crossroad) { this.crossroad = crossroad; }
    public List<RoadSegment> getRoadSegments() { return roadSegments; }
    public void setRoadSegments(List<RoadSegment> roadSegments) { this.roadSegments = roadSegments; }
}