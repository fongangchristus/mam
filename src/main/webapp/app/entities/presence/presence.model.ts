import { IFichePresence } from 'app/entities/fiche-presence/fiche-presence.model';
import { StatutPresence } from 'app/entities/enumerations/statut-presence.model';

export interface IPresence {
  id: number;
  matriculeAdherant?: string | null;
  statutPresence?: keyof typeof StatutPresence | null;
  fichePresence?: Pick<IFichePresence, 'id'> | null;
}

export type NewPresence = Omit<IPresence, 'id'> & { id: null };
