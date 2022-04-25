package fr.ylyade.courtage.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@Table(name = "ta_t_maitrise_oeuvre")
public class TaTMaitriseOeuvre implements Serializable {

	private static final long serialVersionUID = 7825147621287109315L;

	private Integer idTMaitriseOeuvre;
	private String liblTitre;
	private String liblNature;
	private String codeTMaitriseOeuvre;

	private Integer versionObj;
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_t_maitrise_oeuvre", unique = true, nullable = false)
	public Integer getIdTMaitriseOeuvre() {
		return idTMaitriseOeuvre;
	}

	public void setIdTMaitriseOeuvre(Integer idTMaitriseOeuvre) {
		this.idTMaitriseOeuvre = idTMaitriseOeuvre;
	}

	@Column(name = "libl_titre")
	public String getLiblTitre() {
		return liblTitre;
	}

	public void setLiblTitre(String liblTitre) {
		this.liblTitre = liblTitre;
	}

	@Column(name = "libl_nature")
	public String getLiblNature() {
		return liblNature;
	}

	public void setLiblNature(String liblNature) {
		this.liblNature = liblNature;
	}

	@Column(name = "code_type")
	public String getCodeTMaitriseOeuvre() {
		return codeTMaitriseOeuvre;
	}

	public void setCodeTMaitriseOeuvre(String codeType) {
		this.codeTMaitriseOeuvre = codeType;
	}

	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return this.quiCree;
	}

	public void setQuiCree(String quiCreeTCivilite) {
		this.quiCree = quiCreeTCivilite;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return this.quandCree;
	}

	public void setQuandCree(Date quandCreeTCivilite) {
		this.quandCree = quandCreeTCivilite;
	}

	@Column(name = "qui_modif", length = 50)
	public String getQuiModif() {
		return this.quiModif;
	}

	public void setQuiModif(String quiModifTCivilite) {
		this.quiModif = quiModifTCivilite;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModif() {
		return this.quandModif;
	}

	public void setQuandModif(Date quandModifTCivilite) {
		this.quandModif = quandModifTCivilite;
	}

	@Column(name = "ip_acces", length = 50)
	public String getIpAcces() {
		return this.ipAcces;
	}

	public void setIpAcces(String ipAcces) {
		this.ipAcces = ipAcces;
	}

	@Version
	@Column(name = "version_obj")
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}
}
