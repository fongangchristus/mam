package com.it4innov.service.criteria;

import com.it4innov.domain.enumeration.StatutPresence;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.Presence} entity. This class is used
 * in {@link com.it4innov.web.rest.PresenceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /presences?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PresenceCriteria implements Serializable, Criteria {

    /**
     * Class for filtering StatutPresence
     */
    public static class StatutPresenceFilter extends Filter<StatutPresence> {

        public StatutPresenceFilter() {}

        public StatutPresenceFilter(StatutPresenceFilter filter) {
            super(filter);
        }

        @Override
        public StatutPresenceFilter copy() {
            return new StatutPresenceFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter matriculeAdherant;

    private StatutPresenceFilter statutPresence;

    private LongFilter fichePresenceId;

    private Boolean distinct;

    public PresenceCriteria() {}

    public PresenceCriteria(PresenceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.matriculeAdherant = other.matriculeAdherant == null ? null : other.matriculeAdherant.copy();
        this.statutPresence = other.statutPresence == null ? null : other.statutPresence.copy();
        this.fichePresenceId = other.fichePresenceId == null ? null : other.fichePresenceId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PresenceCriteria copy() {
        return new PresenceCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getMatriculeAdherant() {
        return matriculeAdherant;
    }

    public StringFilter matriculeAdherant() {
        if (matriculeAdherant == null) {
            matriculeAdherant = new StringFilter();
        }
        return matriculeAdherant;
    }

    public void setMatriculeAdherant(StringFilter matriculeAdherant) {
        this.matriculeAdherant = matriculeAdherant;
    }

    public StatutPresenceFilter getStatutPresence() {
        return statutPresence;
    }

    public StatutPresenceFilter statutPresence() {
        if (statutPresence == null) {
            statutPresence = new StatutPresenceFilter();
        }
        return statutPresence;
    }

    public void setStatutPresence(StatutPresenceFilter statutPresence) {
        this.statutPresence = statutPresence;
    }

    public LongFilter getFichePresenceId() {
        return fichePresenceId;
    }

    public LongFilter fichePresenceId() {
        if (fichePresenceId == null) {
            fichePresenceId = new LongFilter();
        }
        return fichePresenceId;
    }

    public void setFichePresenceId(LongFilter fichePresenceId) {
        this.fichePresenceId = fichePresenceId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PresenceCriteria that = (PresenceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(matriculeAdherant, that.matriculeAdherant) &&
            Objects.equals(statutPresence, that.statutPresence) &&
            Objects.equals(fichePresenceId, that.fichePresenceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, matriculeAdherant, statutPresence, fichePresenceId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PresenceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (matriculeAdherant != null ? "matriculeAdherant=" + matriculeAdherant + ", " : "") +
            (statutPresence != null ? "statutPresence=" + statutPresence + ", " : "") +
            (fichePresenceId != null ? "fichePresenceId=" + fichePresenceId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
