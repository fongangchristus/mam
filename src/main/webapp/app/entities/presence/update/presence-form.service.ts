import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPresence, NewPresence } from '../presence.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPresence for edit and NewPresenceFormGroupInput for create.
 */
type PresenceFormGroupInput = IPresence | PartialWithRequiredKeyOf<NewPresence>;

type PresenceFormDefaults = Pick<NewPresence, 'id'>;

type PresenceFormGroupContent = {
  id: FormControl<IPresence['id'] | NewPresence['id']>;
  matriculeAdherant: FormControl<IPresence['matriculeAdherant']>;
  statutPresence: FormControl<IPresence['statutPresence']>;
  fichePresence: FormControl<IPresence['fichePresence']>;
};

export type PresenceFormGroup = FormGroup<PresenceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PresenceFormService {
  createPresenceFormGroup(presence: PresenceFormGroupInput = { id: null }): PresenceFormGroup {
    const presenceRawValue = {
      ...this.getFormDefaults(),
      ...presence,
    };
    return new FormGroup<PresenceFormGroupContent>({
      id: new FormControl(
        { value: presenceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      matriculeAdherant: new FormControl(presenceRawValue.matriculeAdherant, {
        validators: [Validators.required],
      }),
      statutPresence: new FormControl(presenceRawValue.statutPresence, {
        validators: [Validators.required],
      }),
      fichePresence: new FormControl(presenceRawValue.fichePresence),
    });
  }

  getPresence(form: PresenceFormGroup): IPresence | NewPresence {
    return form.getRawValue() as IPresence | NewPresence;
  }

  resetForm(form: PresenceFormGroup, presence: PresenceFormGroupInput): void {
    const presenceRawValue = { ...this.getFormDefaults(), ...presence };
    form.reset(
      {
        ...presenceRawValue,
        id: { value: presenceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PresenceFormDefaults {
    return {
      id: null,
    };
  }
}
