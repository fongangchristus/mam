import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IFichePresence } from 'app/entities/fiche-presence/fiche-presence.model';
import { FichePresenceService } from 'app/entities/fiche-presence/service/fiche-presence.service';
import { PresenceService } from '../service/presence.service';
import { IPresence } from '../presence.model';
import { PresenceFormService } from './presence-form.service';

import { PresenceUpdateComponent } from './presence-update.component';

describe('Presence Management Update Component', () => {
  let comp: PresenceUpdateComponent;
  let fixture: ComponentFixture<PresenceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let presenceFormService: PresenceFormService;
  let presenceService: PresenceService;
  let fichePresenceService: FichePresenceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), PresenceUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PresenceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PresenceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    presenceFormService = TestBed.inject(PresenceFormService);
    presenceService = TestBed.inject(PresenceService);
    fichePresenceService = TestBed.inject(FichePresenceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call FichePresence query and add missing value', () => {
      const presence: IPresence = { id: 456 };
      const fichePresence: IFichePresence = { id: 28937 };
      presence.fichePresence = fichePresence;

      const fichePresenceCollection: IFichePresence[] = [{ id: 29855 }];
      jest.spyOn(fichePresenceService, 'query').mockReturnValue(of(new HttpResponse({ body: fichePresenceCollection })));
      const additionalFichePresences = [fichePresence];
      const expectedCollection: IFichePresence[] = [...additionalFichePresences, ...fichePresenceCollection];
      jest.spyOn(fichePresenceService, 'addFichePresenceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ presence });
      comp.ngOnInit();

      expect(fichePresenceService.query).toHaveBeenCalled();
      expect(fichePresenceService.addFichePresenceToCollectionIfMissing).toHaveBeenCalledWith(
        fichePresenceCollection,
        ...additionalFichePresences.map(expect.objectContaining),
      );
      expect(comp.fichePresencesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const presence: IPresence = { id: 456 };
      const fichePresence: IFichePresence = { id: 17656 };
      presence.fichePresence = fichePresence;

      activatedRoute.data = of({ presence });
      comp.ngOnInit();

      expect(comp.fichePresencesSharedCollection).toContain(fichePresence);
      expect(comp.presence).toEqual(presence);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPresence>>();
      const presence = { id: 123 };
      jest.spyOn(presenceFormService, 'getPresence').mockReturnValue(presence);
      jest.spyOn(presenceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ presence });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: presence }));
      saveSubject.complete();

      // THEN
      expect(presenceFormService.getPresence).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(presenceService.update).toHaveBeenCalledWith(expect.objectContaining(presence));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPresence>>();
      const presence = { id: 123 };
      jest.spyOn(presenceFormService, 'getPresence').mockReturnValue({ id: null });
      jest.spyOn(presenceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ presence: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: presence }));
      saveSubject.complete();

      // THEN
      expect(presenceFormService.getPresence).toHaveBeenCalled();
      expect(presenceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPresence>>();
      const presence = { id: 123 };
      jest.spyOn(presenceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ presence });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(presenceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFichePresence', () => {
      it('Should forward to fichePresenceService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(fichePresenceService, 'compareFichePresence');
        comp.compareFichePresence(entity, entity2);
        expect(fichePresenceService.compareFichePresence).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
