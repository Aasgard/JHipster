package fr.istic.opower.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A House.
 */
@Entity
@Table(name = "house")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class House implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "square_maters")
    private Double squareMaters;
    
    @Column(name = "nomber_of_rooms")
    private Integer nomberOfRooms;
    
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @OneToMany(mappedBy = "house")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ElectronicDevice> devicess = new HashSet<>();

    @OneToMany(mappedBy = "house")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Heater> heaterss = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getSquareMaters() {
        return squareMaters;
    }
    
    public void setSquareMaters(Double squareMaters) {
        this.squareMaters = squareMaters;
    }

    public Integer getNomberOfRooms() {
        return nomberOfRooms;
    }
    
    public void setNomberOfRooms(Integer nomberOfRooms) {
        this.nomberOfRooms = nomberOfRooms;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Set<ElectronicDevice> getDevicess() {
        return devicess;
    }

    public void setDevicess(Set<ElectronicDevice> electronicDevices) {
        this.devicess = electronicDevices;
    }

    public Set<Heater> getHeaterss() {
        return heaterss;
    }

    public void setHeaterss(Set<Heater> heaters) {
        this.heaterss = heaters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        House house = (House) o;
        if(house.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, house.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "House{" +
            "id=" + id +
            ", squareMaters='" + squareMaters + "'" +
            ", nomberOfRooms='" + nomberOfRooms + "'" +
            '}';
    }
}
