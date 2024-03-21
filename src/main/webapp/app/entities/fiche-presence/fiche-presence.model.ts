import dayjs from 'dayjs/esm';
import { IPresence } from 'app/entities/presence/presence.model';

export interface IFichePresence {
  id: number;
  libelle?: string | null;
  dateJour?: dayjs.Dayjs | null;
  description?: string | null;
  codeEvenement?: number | null;
  codeTypeEvenement?: number | null;
  presences?: Pick<IPresence, 'id'>[] | null;
}

export type NewFichePresence = Omit<IFichePresence, 'id'> & { id: null };
