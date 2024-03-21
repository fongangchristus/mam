import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPresence } from '../presence.model';
import { PresenceService } from '../service/presence.service';

@Component({
  standalone: true,
  templateUrl: './presence-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PresenceDeleteDialogComponent {
  presence?: IPresence;

  constructor(
    protected presenceService: PresenceService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.presenceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
