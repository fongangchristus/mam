import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'fiche-presence',
    data: { pageTitle: 'FichePresences' },
    loadChildren: () => import('./fiche-presence/fiche-presence.routes'),
  },
  {
    path: 'presence',
    data: { pageTitle: 'Presences' },
    loadChildren: () => import('./presence/presence.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
