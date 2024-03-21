import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPresence, NewPresence } from '../presence.model';

export type PartialUpdatePresence = Partial<IPresence> & Pick<IPresence, 'id'>;

export type EntityResponseType = HttpResponse<IPresence>;
export type EntityArrayResponseType = HttpResponse<IPresence[]>;

@Injectable({ providedIn: 'root' })
export class PresenceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/presences');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(presence: NewPresence): Observable<EntityResponseType> {
    return this.http.post<IPresence>(this.resourceUrl, presence, { observe: 'response' });
  }

  update(presence: IPresence): Observable<EntityResponseType> {
    return this.http.put<IPresence>(`${this.resourceUrl}/${this.getPresenceIdentifier(presence)}`, presence, { observe: 'response' });
  }

  partialUpdate(presence: PartialUpdatePresence): Observable<EntityResponseType> {
    return this.http.patch<IPresence>(`${this.resourceUrl}/${this.getPresenceIdentifier(presence)}`, presence, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPresence>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPresence[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPresenceIdentifier(presence: Pick<IPresence, 'id'>): number {
    return presence.id;
  }

  comparePresence(o1: Pick<IPresence, 'id'> | null, o2: Pick<IPresence, 'id'> | null): boolean {
    return o1 && o2 ? this.getPresenceIdentifier(o1) === this.getPresenceIdentifier(o2) : o1 === o2;
  }

  addPresenceToCollectionIfMissing<Type extends Pick<IPresence, 'id'>>(
    presenceCollection: Type[],
    ...presencesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const presences: Type[] = presencesToCheck.filter(isPresent);
    if (presences.length > 0) {
      const presenceCollectionIdentifiers = presenceCollection.map(presenceItem => this.getPresenceIdentifier(presenceItem)!);
      const presencesToAdd = presences.filter(presenceItem => {
        const presenceIdentifier = this.getPresenceIdentifier(presenceItem);
        if (presenceCollectionIdentifiers.includes(presenceIdentifier)) {
          return false;
        }
        presenceCollectionIdentifiers.push(presenceIdentifier);
        return true;
      });
      return [...presencesToAdd, ...presenceCollection];
    }
    return presenceCollection;
  }
}
