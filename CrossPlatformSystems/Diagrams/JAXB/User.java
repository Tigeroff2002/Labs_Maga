package JAXB;

import javax.xml.bind.annotation.*;

class User {
    @XmlElement(name = "id", namespace = "http://example.com/schema")
    private int id;
    
    @XmlElement(name = "email", namespace = "http://example.com/schema")
    private String email;
    
    @XmlElement(name = "role", namespace = "http://example.com/schema")
    private String role;
    
    @XmlElement(name = "full_name", namespace = "http://example.com/schema")
    private String fullName;

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
}