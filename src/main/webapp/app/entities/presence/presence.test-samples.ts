import { IPresence, NewPresence } from './presence.model';

export const sampleWithRequiredData: IPresence = {
  id: 9287,
  matriculeAdherant: 'viciously gadzooks deep',
  statutPresence: 'PRESENT',
};

export const sampleWithPartialData: IPresence = {
  id: 5399,
  matriculeAdherant: 'unto',
  statutPresence: 'PRESENT',
};

export const sampleWithFullData: IPresence = {
  id: 31299,
  matriculeAdherant: 'but upon',
  statutPresence: 'RETARD',
};

export const sampleWithNewData: NewPresence = {
  matriculeAdherant: 'what deadly',
  statutPresence: 'PRESENT',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
