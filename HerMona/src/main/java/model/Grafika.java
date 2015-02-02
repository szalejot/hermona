package model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "grafika")
public class Grafika {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int grafikaId;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "tekaID", nullable = true)
	private Teka teka;
	private String numerInwentarza;
	private String tytul;
	private String seria;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "technikaID", nullable = true)
	private Technika technika;
	private String wymiary;
	private String projektant;
	private String rytownik;
	private String wydawca;
	private String sygnatury;
	private int rokOd;
	private int rokDo;
	private String miejsceWydania;
	private String opis;
	private String inskrypcje;
	private String bibliografia;
	private String uwagi;
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "grafika_kategoria", 
		joinColumns = { @JoinColumn(name = "grafikaId", nullable = false, updatable = false) }, 
		inverseJoinColumns = { @JoinColumn(name = "kategoriaId", nullable = false, updatable = false) })
	private Set<Kategoria> kategorie;
	private String ilustracjaPath;

	public Grafika(int grafikaId, Teka teka, String numerInwentarza,
			String tytul, String seria, Technika technika, String wymiary,
			String projektant, String rytownik, String wydawca,
			String sygnatury, int rokOd, int rokDo, String miejsceWydania,
			String opis, String inskrypcje, String bibliografia, String uwagi,
			Set<Kategoria> kategorie, String ilustracjaPath) {
		this.grafikaId = grafikaId;
		this.teka = teka;
		this.numerInwentarza = numerInwentarza;
		this.tytul = tytul;
		this.seria = seria;
		this.technika = technika;
		this.wymiary = wymiary;
		this.projektant = projektant;
		this.rytownik = rytownik;
		this.wydawca = wydawca;
		this.sygnatury = sygnatury;
		this.rokOd = rokOd;
		this.rokDo = rokDo;
		this.miejsceWydania = miejsceWydania;
		this.opis = opis;
		this.inskrypcje = inskrypcje;
		this.bibliografia = bibliografia;
		this.uwagi = uwagi;
		this.kategorie = kategorie;
		this.ilustracjaPath = ilustracjaPath;
	}

	public int getGrafikaId() {
		return grafikaId;
	}

	public void setGrafikaId(int grafikaId) {
		this.grafikaId = grafikaId;
	}

	public Teka getTeka() {
		return teka;
	}

	public void setTeka(Teka teka) {
		this.teka = teka;
	}

	public String getNumerInwentarza() {
		return numerInwentarza;
	}

	public void setNumerInwentarza(String numerInwentarza) {
		this.numerInwentarza = numerInwentarza;
	}

	public String getTytul() {
		return tytul;
	}

	public void setTytul(String tytul) {
		this.tytul = tytul;
	}

	public String getSeria() {
		return seria;
	}

	public void setSeria(String seria) {
		this.seria = seria;
	}

	public Technika getTechnika() {
		return technika;
	}

	public void setTechnika(Technika technika) {
		this.technika = technika;
	}

	public String getWymiary() {
		return wymiary;
	}

	public void setWymiary(String wymiary) {
		this.wymiary = wymiary;
	}

	public String getProjektant() {
		return projektant;
	}

	public void setProjektant(String projektant) {
		this.projektant = projektant;
	}

	public String getRytownik() {
		return rytownik;
	}

	public void setRytownik(String rytownik) {
		this.rytownik = rytownik;
	}

	public String getWydawca() {
		return wydawca;
	}

	public void setWydawca(String wydawca) {
		this.wydawca = wydawca;
	}

	public String getSygnatury() {
		return sygnatury;
	}

	public void setSygnatury(String sygnatury) {
		this.sygnatury = sygnatury;
	}

	public int getRokOd() {
		return rokOd;
	}

	public void setRokOd(int rokOd) {
		this.rokOd = rokOd;
	}

	public int getRokDo() {
		return rokDo;
	}

	public void setRokDo(int rokDo) {
		this.rokDo = rokDo;
	}

	public String getMiejsceWydania() {
		return miejsceWydania;
	}

	public void setMiejsceWydania(String miejsceWydania) {
		this.miejsceWydania = miejsceWydania;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public String getInskrypcje() {
		return inskrypcje;
	}

	public void setInskrypcje(String inskrypcje) {
		this.inskrypcje = inskrypcje;
	}

	public String getBibliografia() {
		return bibliografia;
	}

	public void setBibliografia(String bibliografia) {
		this.bibliografia = bibliografia;
	}

	public String getUwagi() {
		return uwagi;
	}

	public void setUwagi(String uwagi) {
		this.uwagi = uwagi;
	}

	public Set<Kategoria> getKategorie() {
		return kategorie;
	}

	public void setKategorie(Set<Kategoria> kategorie) {
		this.kategorie = kategorie;
	}

	public String getIlustracjaPath() {
		return ilustracjaPath;
	}

	public void setIlustracjaPath(String ilustracjaPath) {
		this.ilustracjaPath = ilustracjaPath;
	}

}
