package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import util.ToString;

@Entity
@Table(name = "kategoria")
public class Kategoria {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int kategoriaId;
	@Column(unique = true)
	private String nazwa;
	
	public Kategoria() { }

	public Kategoria(int kategoriaId, String nazwa) {
		this.kategoriaId = kategoriaId;
		this.nazwa = nazwa;
	}
	
	public Kategoria (String nazwa) {
		this.nazwa = nazwa;
	}

	public int getKategoriaId() {
		return kategoriaId;
	}

	public void setKategoriaId(int kategoriaId) {
		this.kategoriaId = kategoriaId;
	}

	public String getNazwa() {
		return nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}

	@Override
	public String toString() {
		return nazwa;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof Kategoria))return false;
	    Kategoria otherKategoria = (Kategoria)other;
	    return getNazwa().equals(otherKategoria.getNazwa());
	}
	
	public static ToString getStringRenderer() {
		return new ToString() {
		    public String toString(final Object object) {
		        final Kategoria value = (Kategoria) object;
		        if (value == null) {
		        	return "";
		        } else {
		        	return value.getNazwa();
		        }
		    }
		};
	}

}
