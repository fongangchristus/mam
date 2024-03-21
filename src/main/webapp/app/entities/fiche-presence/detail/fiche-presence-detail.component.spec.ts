import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { FichePresenceDetailComponent } from './fiche-presence-detail.component';

describe('FichePresence Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FichePresenceDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: FichePresenceDetailComponent,
              resolve: { fichePresence: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FichePresenceDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load fichePresence on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FichePresenceDetailComponent);

      // THEN
      expect(instance.fichePresence).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
