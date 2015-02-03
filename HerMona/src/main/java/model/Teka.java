package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.sql.Date;

@Entity
@Table(name = "teka")
public class Teka {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int tekaId;
	@Column(unique = true)
	private Integer numer;
	private String tytul;
	private Date data;

	public Teka() { }
	
	public Teka(int tekaId, Integer numer, String tytul, Date data) {
		this.tekaId = tekaId;
		this.numer = numer;
		this.tytul = tytul;
		this.data = data;
	}

	public Teka(Integer numer, String tytul, Date data) {
		this.numer = numer;
		this.tytul = tytul;
		this.data = data;
	}

	public int getTekaId() {
		return tekaId;
	}

	public void setTekaId(int tekaId) {
		this.tekaId = tekaId;
	}

	public Integer getNumer() {
		return numer;
	}

	public void setNumer(Integer numer) {
		this.numer = numer;
	}

	public String getTytul() {
		return tytul;
	}

	public void setTytul(String tytul) {
		this.tytul = tytul;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Teka [numer=" + numer + ", tytul=" + tytul + ", data=" + data
				+ "]";
	}
}
