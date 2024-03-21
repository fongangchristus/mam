import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFichePresence } from '../fiche-presence.model';
import { FichePresenceService } from '../service/fiche-presence.service';

@Component({
  standalone: true,
  templateUrl: './fiche-presence-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FichePresenceDeleteDialogComponent {
  fichePresence?: IFichePresence;

  constructor(
    protected fichePresenceService: FichePresenceService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fichePresenceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
