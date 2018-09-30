package server;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Sarthak Chavan
 */
public class Faculty {
    
    private int id;
    private String firstName,lastName;
    private String empType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmpType() {
        return empType;
    }

    public void setEmpType(String empType) {
        this.empType = empType;
    }

    public Faculty(int id, String firstName, String lastName, String empType) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.empType = empType;
    }
    
    public Faculty() {
        
    }
    
}
