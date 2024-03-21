package com.it4innov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.it4innov.domain.enumeration.StatutPresence;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Presence.
 */
@Entity
@Table(name = "presence")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Presence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "matricule_adherant", nullable = false)
    private String matriculeAdherant;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statut_presence", nullable = false)
    private StatutPresence statutPresence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "presences" }, allowSetters = true)
    private FichePresence fichePresence;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Presence id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatriculeAdherant() {
        return this.matriculeAdherant;
    }

    public Presence matriculeAdherant(String matriculeAdherant) {
        this.setMatriculeAdherant(matriculeAdherant);
        return this;
    }

    public void setMatriculeAdherant(String matriculeAdherant) {
        this.matriculeAdherant = matriculeAdherant;
    }

    public StatutPresence getStatutPresence() {
        return this.statutPresence;
    }

    public Presence statutPresence(StatutPresence statutPresence) {
        this.setStatutPresence(statutPresence);
        return this;
    }

    public void setStatutPresence(StatutPresence statutPresence) {
        this.statutPresence = statutPresence;
    }

    public FichePresence getFichePresence() {
        return this.fichePresence;
    }

    public void setFichePresence(FichePresence fichePresence) {
        this.fichePresence = fichePresence;
    }

    public Presence fichePresence(FichePresence fichePresence) {
        this.setFichePresence(fichePresence);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Presence)) {
            return false;
        }
        return getId() != null && getId().equals(((Presence) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Presence{" +
            "id=" + getId() +
            ", matriculeAdherant='" + getMatriculeAdherant() + "'" +
            ", statutPresence='" + getStatutPresence() + "'" +
            "}";
    }
}
