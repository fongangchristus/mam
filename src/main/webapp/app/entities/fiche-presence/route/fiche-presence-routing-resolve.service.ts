import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFichePresence } from '../fiche-presence.model';
import { FichePresenceService } from '../service/fiche-presence.service';

export const fichePresenceResolve = (route: ActivatedRouteSnapshot): Observable<null | IFichePresence> => {
  const id = route.params['id'];
  if (id) {
    return inject(FichePresenceService)
      .find(id)
      .pipe(
        mergeMap((fichePresence: HttpResponse<IFichePresence>) => {
          if (fichePresence.body) {
            return of(fichePresence.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default fichePresenceResolve;
