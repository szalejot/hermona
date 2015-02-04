package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import util.ToString;

@Entity
@Table(name = "technika")
public class Technika {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int technikaId;
	@Column(unique = true)
	private String nazwa;
	
	public Technika() { }

	public Technika(int technikaId, String nazwa) {
		this.technikaId = technikaId;
		this.nazwa = nazwa;
	}
	
	public Technika(String nazwa) {
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

	@Override
	public String toString() {
		return "Technika [nazwa=" + nazwa + "]";
	}
	
	public static ToString getStringRenderer() {
		return new ToString() {
		    public String toString(final Object object) {
		        final Technika value = (Technika) object;
		        if (value == null) {
		        	return "";
		        } else {
		        	return value.getNazwa();
		        }
		    }
		};
	}
}
