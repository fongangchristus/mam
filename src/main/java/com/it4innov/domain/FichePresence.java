package com.it4innov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A FichePresence.
 */
@Entity
@Table(name = "fiche_presence")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FichePresence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @NotNull
    @Column(name = "date_jour", nullable = false)
    private Instant dateJour;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "code_evenement", nullable = false)
    private Long codeEvenement;

    @NotNull
    @Column(name = "code_type_evenement", nullable = false)
    private Long codeTypeEvenement;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "fichePresence")
    @JsonIgnoreProperties(value = { "fichePresence" }, allowSetters = true)
    private Set<Presence> presences = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FichePresence id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public FichePresence libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Instant getDateJour() {
        return this.dateJour;
    }

    public FichePresence dateJour(Instant dateJour) {
        this.setDateJour(dateJour);
        return this;
    }

    public void setDateJour(Instant dateJour) {
        this.dateJour = dateJour;
    }

    public String getDescription() {
        return this.description;
    }

    public FichePresence description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCodeEvenement() {
        return this.codeEvenement;
    }

    public FichePresence codeEvenement(Long codeEvenement) {
        this.setCodeEvenement(codeEvenement);
        return this;
    }

    public void setCodeEvenement(Long codeEvenement) {
        this.codeEvenement = codeEvenement;
    }

    public Long getCodeTypeEvenement() {
        return this.codeTypeEvenement;
    }

    public FichePresence codeTypeEvenement(Long codeTypeEvenement) {
        this.setCodeTypeEvenement(codeTypeEvenement);
        return this;
    }

    public void setCodeTypeEvenement(Long codeTypeEvenement) {
        this.codeTypeEvenement = codeTypeEvenement;
    }

    public Set<Presence> getPresences() {
        return this.presences;
    }

    public void setPresences(Set<Presence> presences) {
        if (this.presences != null) {
            this.presences.forEach(i -> i.setFichePresence(null));
        }
        if (presences != null) {
            presences.forEach(i -> i.setFichePresence(this));
        }
        this.presences = presences;
    }

    public FichePresence presences(Set<Presence> presences) {
        this.setPresences(presences);
        return this;
    }

    public FichePresence addPresence(Presence presence) {
        this.presences.add(presence);
        presence.setFichePresence(this);
        return this;
    }

    public FichePresence removePresence(Presence presence) {
        this.presences.remove(presence);
        presence.setFichePresence(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FichePresence)) {
            return false;
        }
        return getId() != null && getId().equals(((FichePresence) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FichePresence{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", dateJour='" + getDateJour() + "'" +
            ", description='" + getDescription() + "'" +
            ", codeEvenement=" + getCodeEvenement() +
            ", codeTypeEvenement=" + getCodeTypeEvenement() +
            "}";
    }
}
