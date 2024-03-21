import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFichePresence } from 'app/entities/fiche-presence/fiche-presence.model';
import { FichePresenceService } from 'app/entities/fiche-presence/service/fiche-presence.service';
import { StatutPresence } from 'app/entities/enumerations/statut-presence.model';
import { PresenceService } from '../service/presence.service';
import { IPresence } from '../presence.model';
import { PresenceFormService, PresenceFormGroup } from './presence-form.service';

@Component({
  standalone: true,
  selector: 'jhi-presence-update',
  templateUrl: './presence-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PresenceUpdateComponent implements OnInit {
  isSaving = false;
  presence: IPresence | null = null;
  statutPresenceValues = Object.keys(StatutPresence);

  fichePresencesSharedCollection: IFichePresence[] = [];

  editForm: PresenceFormGroup = this.presenceFormService.createPresenceFormGroup();

  constructor(
    protected presenceService: PresenceService,
    protected presenceFormService: PresenceFormService,
    protected fichePresenceService: FichePresenceService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareFichePresence = (o1: IFichePresence | null, o2: IFichePresence | null): boolean =>
    this.fichePresenceService.compareFichePresence(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ presence }) => {
      this.presence = presence;
      if (presence) {
        this.updateForm(presence);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const presence = this.presenceFormService.getPresence(this.editForm);
    if (presence.id !== null) {
      this.subscribeToSaveResponse(this.presenceService.update(presence));
    } else {
      this.subscribeToSaveResponse(this.presenceService.create(presence));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPresence>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(presence: IPresence): void {
    this.presence = presence;
    this.presenceFormService.resetForm(this.editForm, presence);

    this.fichePresencesSharedCollection = this.fichePresenceService.addFichePresenceToCollectionIfMissing<IFichePresence>(
      this.fichePresencesSharedCollection,
      presence.fichePresence,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.fichePresenceService
      .query()
      .pipe(map((res: HttpResponse<IFichePresence[]>) => res.body ?? []))
      .pipe(
        map((fichePresences: IFichePresence[]) =>
          this.fichePresenceService.addFichePresenceToCollectionIfMissing<IFichePresence>(fichePresences, this.presence?.fichePresence),
        ),
      )
      .subscribe((fichePresences: IFichePresence[]) => (this.fichePresencesSharedCollection = fichePresences));
  }
}
