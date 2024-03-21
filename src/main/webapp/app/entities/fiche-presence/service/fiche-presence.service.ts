import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFichePresence, NewFichePresence } from '../fiche-presence.model';

export type PartialUpdateFichePresence = Partial<IFichePresence> & Pick<IFichePresence, 'id'>;

type RestOf<T extends IFichePresence | NewFichePresence> = Omit<T, 'dateJour'> & {
  dateJour?: string | null;
};

export type RestFichePresence = RestOf<IFichePresence>;

export type NewRestFichePresence = RestOf<NewFichePresence>;

export type PartialUpdateRestFichePresence = RestOf<PartialUpdateFichePresence>;

export type EntityResponseType = HttpResponse<IFichePresence>;
export type EntityArrayResponseType = HttpResponse<IFichePresence[]>;

@Injectable({ providedIn: 'root' })
export class FichePresenceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fiche-presences');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(fichePresence: NewFichePresence): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fichePresence);
    return this.http
      .post<RestFichePresence>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(fichePresence: IFichePresence): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fichePresence);
    return this.http
      .put<RestFichePresence>(`${this.resourceUrl}/${this.getFichePresenceIdentifier(fichePresence)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(fichePresence: PartialUpdateFichePresence): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fichePresence);
    return this.http
      .patch<RestFichePresence>(`${this.resourceUrl}/${this.getFichePresenceIdentifier(fichePresence)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFichePresence>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFichePresence[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFichePresenceIdentifier(fichePresence: Pick<IFichePresence, 'id'>): number {
    return fichePresence.id;
  }

  compareFichePresence(o1: Pick<IFichePresence, 'id'> | null, o2: Pick<IFichePresence, 'id'> | null): boolean {
    return o1 && o2 ? this.getFichePresenceIdentifier(o1) === this.getFichePresenceIdentifier(o2) : o1 === o2;
  }

  addFichePresenceToCollectionIfMissing<Type extends Pick<IFichePresence, 'id'>>(
    fichePresenceCollection: Type[],
    ...fichePresencesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const fichePresences: Type[] = fichePresencesToCheck.filter(isPresent);
    if (fichePresences.length > 0) {
      const fichePresenceCollectionIdentifiers = fichePresenceCollection.map(
        fichePresenceItem => this.getFichePresenceIdentifier(fichePresenceItem)!,
      );
      const fichePresencesToAdd = fichePresences.filter(fichePresenceItem => {
        const fichePresenceIdentifier = this.getFichePresenceIdentifier(fichePresenceItem);
        if (fichePresenceCollectionIdentifiers.includes(fichePresenceIdentifier)) {
          return false;
        }
        fichePresenceCollectionIdentifiers.push(fichePresenceIdentifier);
        return true;
      });
      return [...fichePresencesToAdd, ...fichePresenceCollection];
    }
    return fichePresenceCollection;
  }

  protected convertDateFromClient<T extends IFichePresence | NewFichePresence | PartialUpdateFichePresence>(fichePresence: T): RestOf<T> {
    return {
      ...fichePresence,
      dateJour: fichePresence.dateJour?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restFichePresence: RestFichePresence): IFichePresence {
    return {
      ...restFichePresence,
      dateJour: restFichePresence.dateJour ? dayjs(restFichePresence.dateJour) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFichePresence>): HttpResponse<IFichePresence> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFichePresence[]>): HttpResponse<IFichePresence[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
