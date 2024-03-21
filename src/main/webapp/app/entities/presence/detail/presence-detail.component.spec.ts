import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PresenceDetailComponent } from './presence-detail.component';

describe('Presence Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PresenceDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PresenceDetailComponent,
              resolve: { presence: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PresenceDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load presence on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PresenceDetailComponent);

      // THEN
      expect(instance.presence).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
