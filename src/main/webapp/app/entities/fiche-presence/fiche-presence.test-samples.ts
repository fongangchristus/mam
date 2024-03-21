import dayjs from 'dayjs/esm';

import { IFichePresence, NewFichePresence } from './fiche-presence.model';

export const sampleWithRequiredData: IFichePresence = {
  id: 26863,
  libelle: 'blissfully worth burdensome',
  dateJour: dayjs('2024-03-20T07:33'),
  codeEvenement: 4959,
  codeTypeEvenement: 24595,
};

export const sampleWithPartialData: IFichePresence = {
  id: 21498,
  libelle: 'to',
  dateJour: dayjs('2024-03-19T23:00'),
  codeEvenement: 18483,
  codeTypeEvenement: 15220,
};

export const sampleWithFullData: IFichePresence = {
  id: 9950,
  libelle: 'needily tournament',
  dateJour: dayjs('2024-03-20T07:44'),
  description: 'adjourn empty',
  codeEvenement: 16713,
  codeTypeEvenement: 11758,
};

export const sampleWithNewData: NewFichePresence = {
  libelle: 'across',
  dateJour: dayjs('2024-03-20T00:19'),
  codeEvenement: 18774,
  codeTypeEvenement: 25982,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
