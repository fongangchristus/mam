import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { FichePresenceComponent } from './list/fiche-presence.component';
import { FichePresenceDetailComponent } from './detail/fiche-presence-detail.component';
import { FichePresenceUpdateComponent } from './update/fiche-presence-update.component';
import FichePresenceResolve from './route/fiche-presence-routing-resolve.service';

const fichePresenceRoute: Routes = [
  {
    path: '',
    component: FichePresenceComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FichePresenceDetailComponent,
    resolve: {
      fichePresence: FichePresenceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FichePresenceUpdateComponent,
    resolve: {
      fichePresence: FichePresenceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FichePresenceUpdateComponent,
    resolve: {
      fichePresence: FichePresenceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default fichePresenceRoute;
