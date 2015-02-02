package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "technika")
public class Technika {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int technikaId;
	private String nazwa;

	public Technika(int technikaId, String nazwa) {
		this.technikaId = technikaId;
		this.nazwa = nazwa;
	}

	public int getTechnikaId() {
		return technikaId;
	}

	public void setTechnikaId(int technikaId) {
		this.technikaId = technikaId;
	}

	public String getNazwa() {
		return nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}

}
