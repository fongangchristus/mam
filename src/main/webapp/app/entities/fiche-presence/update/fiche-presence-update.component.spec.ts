import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FichePresenceService } from '../service/fiche-presence.service';
import { IFichePresence } from '../fiche-presence.model';
import { FichePresenceFormService } from './fiche-presence-form.service';

import { FichePresenceUpdateComponent } from './fiche-presence-update.component';

describe('FichePresence Management Update Component', () => {
  let comp: FichePresenceUpdateComponent;
  let fixture: ComponentFixture<FichePresenceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fichePresenceFormService: FichePresenceFormService;
  let fichePresenceService: FichePresenceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), FichePresenceUpdateComponent],
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
      .overrideTemplate(FichePresenceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FichePresenceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fichePresenceFormService = TestBed.inject(FichePresenceFormService);
    fichePresenceService = TestBed.inject(FichePresenceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const fichePresence: IFichePresence = { id: 456 };

      activatedRoute.data = of({ fichePresence });
      comp.ngOnInit();

      expect(comp.fichePresence).toEqual(fichePresence);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFichePresence>>();
      const fichePresence = { id: 123 };
      jest.spyOn(fichePresenceFormService, 'getFichePresence').mockReturnValue(fichePresence);
      jest.spyOn(fichePresenceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fichePresence });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fichePresence }));
      saveSubject.complete();

      // THEN
      expect(fichePresenceFormService.getFichePresence).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fichePresenceService.update).toHaveBeenCalledWith(expect.objectContaining(fichePresence));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFichePresence>>();
      const fichePresence = { id: 123 };
      jest.spyOn(fichePresenceFormService, 'getFichePresence').mockReturnValue({ id: null });
      jest.spyOn(fichePresenceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fichePresence: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fichePresence }));
      saveSubject.complete();

      // THEN
      expect(fichePresenceFormService.getFichePresence).toHaveBeenCalled();
      expect(fichePresenceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFichePresence>>();
      const fichePresence = { id: 123 };
      jest.spyOn(fichePresenceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fichePresence });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fichePresenceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
