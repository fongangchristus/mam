import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPresence } from '../presence.model';
import { PresenceService } from '../service/presence.service';

export const presenceResolve = (route: ActivatedRouteSnapshot): Observable<null | IPresence> => {
  const id = route.params['id'];
  if (id) {
    return inject(PresenceService)
      .find(id)
      .pipe(
        mergeMap((presence: HttpResponse<IPresence>) => {
          if (presence.body) {
            return of(presence.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default presenceResolve;
