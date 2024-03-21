import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFichePresence } from '../fiche-presence.model';
import { FichePresenceService } from '../service/fiche-presence.service';
import { FichePresenceFormService, FichePresenceFormGroup } from './fiche-presence-form.service';

@Component({
  standalone: true,
  selector: 'jhi-fiche-presence-update',
  templateUrl: './fiche-presence-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FichePresenceUpdateComponent implements OnInit {
  isSaving = false;
  fichePresence: IFichePresence | null = null;

  editForm: FichePresenceFormGroup = this.fichePresenceFormService.createFichePresenceFormGroup();

  constructor(
    protected fichePresenceService: FichePresenceService,
    protected fichePresenceFormService: FichePresenceFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fichePresence }) => {
      this.fichePresence = fichePresence;
      if (fichePresence) {
        this.updateForm(fichePresence);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fichePresence = this.fichePresenceFormService.getFichePresence(this.editForm);
    if (fichePresence.id !== null) {
      this.subscribeToSaveResponse(this.fichePresenceService.update(fichePresence));
    } else {
      this.subscribeToSaveResponse(this.fichePresenceService.create(fichePresence));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFichePresence>>): void {
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

  protected updateForm(fichePresence: IFichePresence): void {
    this.fichePresence = fichePresence;
    this.fichePresenceFormService.resetForm(this.editForm, fichePresence);
  }
}
