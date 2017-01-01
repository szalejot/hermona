package model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "grafika",
	uniqueConstraints = {@UniqueConstraint(columnNames = {"teka", "numerInwentarza"})})
public class Grafika implements Cloneable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int grafikaId;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "teka", nullable = false)
	private Teka teka;
	@Column(nullable = false)
	private String numerInwentarza = "";
	private String temat = "";
	private String seria = "";
	@ManyToMany(cascade = CascadeType.PERSIST)
	private Set<Technika> techniki = new HashSet<Technika>();
	private String wymiary = "";
	private String projektant = "";
	private String rytownik = "";
	private String wydawca = "";
	private String sygnatury = "";
	private Integer rokOd;
	private Integer rokDo;
	private String miejsceWydania = "";
	private String opis = "";
	private String inskrypcje = "";
	private String bibliografia = "";
	private String uwagi = "";
	@ManyToMany(cascade = CascadeType.PERSIST)
	private Set<Kategoria> kategorie = new HashSet<Kategoria>(0);
	private String ilustracjaPath = "";
	private String katalogi = "";
	private String innyAutor = "";

	public Grafika () { }
	
	public Grafika(int grafikaId, Teka teka, String numerInwentarza,
			String temat, String seria, Set<Technika> techniki, String wymiary,
			String projektant, String rytownik, String wydawca,
			String sygnatury, Integer rokOd, Integer rokDo, String miejsceWydania,
			String opis, String inskrypcje, String bibliografia, String uwagi,
			Set<Kategoria> kategorie, String ilustracjaPath, String katalogi, String innyAutor) {
		this.grafikaId = grafikaId;
		this.teka = teka;
		this.numerInwentarza = numerInwentarza;
		this.temat = temat;
		this.seria = seria;
		this.techniki = techniki;
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
		this.katalogi = katalogi;
		this.innyAutor = innyAutor;
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

	public String getTemat() {
		return temat;
	}

	public void setTemat(String temat) {
		this.temat = temat;
	}

	public String getSeria() {
		return seria;
	}

	public void setSeria(String seria) {
		this.seria = seria;
	}

	public Set<Technika> getTechniki() {
		return techniki;
	}

	public void setTechniki(Set<Technika> techniki) {
		this.techniki = techniki;
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

	public Integer getRokOd() {
		return rokOd;
	}

	public void setRokOd(Integer rokOd) {
		this.rokOd = rokOd;
	}

	public Integer getRokDo() {
		return rokDo;
	}

	public void setRokDo(Integer rokDo) {
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

	@Override
	public String toString() {
		return "Grafika [teka=" + teka + ", numerInwentarza=" + numerInwentarza
				+ ", tytul=" + temat + ", seria=" + seria + ", technika="
				+ techniki + ", wymiary=" + wymiary + ", projektant="
				+ projektant + ", rytownik=" + rytownik + ", wydawca="
				+ wydawca + ", sygnatury=" + sygnatury + ", rokOd=" + rokOd
				+ ", rokDo=" + rokDo + ", miejsceWydania=" + miejsceWydania
				+ ", opis=" + opis + ", inskrypcje=" + inskrypcje
				+ ", bibliografia=" + bibliografia + ", uwagi=" + uwagi
				+ ", kategorie=" + kategorie + ", ilustracjaPath="
				+ ilustracjaPath + "]";
	}
	
	public Grafika getClone() {
		return new Grafika(getGrafikaId(), getTeka(), getNumerInwentarza(),
				getTemat(), getSeria(), getTechniki(), getWymiary(),
				getProjektant(), getRytownik(), getWydawca(), getSygnatury(),
				getRokOd(), getRokDo(), getMiejsceWydania(), getOpis(),
				getInskrypcje(), getBibliografia(), getUwagi(), getKategorie(),
				getIlustracjaPath(), getKatalogi(), getInnyAutor());
	}

	public String getKatalogi() {
		return katalogi;
	}

	public void setKatalogi(String katalogi) {
		this.katalogi = katalogi;
	}

	public String getInnyAutor() {
		return innyAutor;
	}

	public void setInnyAutor(String innyAutor) {
		this.innyAutor = innyAutor;
	}
	
	public Object getFieldByName(String fieldName) {
		switch(fieldName) {
			case "teka":
				return getTeka();
			case "numerInwentarza":
				return getNumerInwentarza();
			case "temat":
				return getTemat();
			case "seria":
				return getSeria();
			case "techniki":
				return getTechniki();
			case "wymiary":
				return getWymiary();
			case "projektant":
				return getProjektant();
			case "rytownik":
				return getRytownik();
			case "wydawca":
				return getWydawca();
			case "sygnatury":
				return getSygnatury();
			case "rokOd":
				return getRokOd();
			case "rokDo":
				return getRokDo();
			case "miejsceWydania":
				return getMiejsceWydania();
			case "opis":
				return getOpis();
			case "inskrypcje":
				return getInskrypcje();
			case "bibliografia":
				return getBibliografia();
			case "uwagi":
				return getUwagi();
			case "kategorie":
				return getKategorie();
			case "innyAutor":
				return getInnyAutor();
			case "katalogi":
				return getKatalogi();
			case "ilustracjaPath":
				return getIlustracjaPath();
			default:
				return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void setFieldByName(String fieldName, Object newValue) {
		switch(fieldName) {
			case "teka":
				setTeka((Teka)newValue);
				break;
			case "numerInwentarza":
				setNumerInwentarza((String)newValue);
				break;
			case "temat":
				setTemat((String)newValue);
				break;
			case "seria":
				setSeria((String)newValue);
				break;
			case "techniki":
				setTechniki((Set<Technika>)newValue);
				break;
			case "wymiary":
				setWymiary((String)newValue);
				break;
			case "projektant":
				setProjektant((String)newValue);
				break;
			case "rytownik":
				setRytownik((String)newValue);
				break;
			case "wydawca":
				setWydawca((String)newValue);
				break;
			case "sygnatury":
				setSygnatury((String)newValue);
				break;
			case "rokOd":
				setRokOd((Integer)newValue);
				break;
			case "rokDo":
				setRokDo((Integer)newValue);
				break;
			case "miejsceWydania":
				setMiejsceWydania((String)newValue);
				break;
			case "opis":
				setOpis((String)newValue);
				break;
			case "inskrypcje":
				setInskrypcje((String)newValue);
				break;
			case "bibliografia":
				setBibliografia((String)newValue);
				break;
			case "uwagi":
				setUwagi((String)newValue);
				break;
			case "kategorie":
				setKategorie((Set<Kategoria>)newValue);
				break;
			case "innyAutor":
				setInnyAutor((String)newValue);
				break;
			case "katalogi":
				setKatalogi((String)newValue);
				break;
			case "ilustracjaPath":
				setIlustracjaPath((String)newValue);
				break;
		}
	}
}
