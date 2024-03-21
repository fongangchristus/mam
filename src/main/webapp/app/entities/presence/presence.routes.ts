import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PresenceComponent } from './list/presence.component';
import { PresenceDetailComponent } from './detail/presence-detail.component';
import { PresenceUpdateComponent } from './update/presence-update.component';
import PresenceResolve from './route/presence-routing-resolve.service';

const presenceRoute: Routes = [
  {
    path: '',
    component: PresenceComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PresenceDetailComponent,
    resolve: {
      presence: PresenceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PresenceUpdateComponent,
    resolve: {
      presence: PresenceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PresenceUpdateComponent,
    resolve: {
      presence: PresenceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default presenceRoute;
