import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../fiche-presence.test-samples';

import { FichePresenceFormService } from './fiche-presence-form.service';

describe('FichePresence Form Service', () => {
  let service: FichePresenceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FichePresenceFormService);
  });

  describe('Service methods', () => {
    describe('createFichePresenceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFichePresenceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelle: expect.any(Object),
            dateJour: expect.any(Object),
            description: expect.any(Object),
            codeEvenement: expect.any(Object),
            codeTypeEvenement: expect.any(Object),
          }),
        );
      });

      it('passing IFichePresence should create a new form with FormGroup', () => {
        const formGroup = service.createFichePresenceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelle: expect.any(Object),
            dateJour: expect.any(Object),
            description: expect.any(Object),
            codeEvenement: expect.any(Object),
            codeTypeEvenement: expect.any(Object),
          }),
        );
      });
    });

    describe('getFichePresence', () => {
      it('should return NewFichePresence for default FichePresence initial value', () => {
        const formGroup = service.createFichePresenceFormGroup(sampleWithNewData);

        const fichePresence = service.getFichePresence(formGroup) as any;

        expect(fichePresence).toMatchObject(sampleWithNewData);
      });

      it('should return NewFichePresence for empty FichePresence initial value', () => {
        const formGroup = service.createFichePresenceFormGroup();

        const fichePresence = service.getFichePresence(formGroup) as any;

        expect(fichePresence).toMatchObject({});
      });

      it('should return IFichePresence', () => {
        const formGroup = service.createFichePresenceFormGroup(sampleWithRequiredData);

        const fichePresence = service.getFichePresence(formGroup) as any;

        expect(fichePresence).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFichePresence should not enable id FormControl', () => {
        const formGroup = service.createFichePresenceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFichePresence should disable id FormControl', () => {
        const formGroup = service.createFichePresenceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
