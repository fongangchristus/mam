import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFichePresence, NewFichePresence } from '../fiche-presence.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFichePresence for edit and NewFichePresenceFormGroupInput for create.
 */
type FichePresenceFormGroupInput = IFichePresence | PartialWithRequiredKeyOf<NewFichePresence>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFichePresence | NewFichePresence> = Omit<T, 'dateJour'> & {
  dateJour?: string | null;
};

type FichePresenceFormRawValue = FormValueOf<IFichePresence>;

type NewFichePresenceFormRawValue = FormValueOf<NewFichePresence>;

type FichePresenceFormDefaults = Pick<NewFichePresence, 'id' | 'dateJour'>;

type FichePresenceFormGroupContent = {
  id: FormControl<FichePresenceFormRawValue['id'] | NewFichePresence['id']>;
  libelle: FormControl<FichePresenceFormRawValue['libelle']>;
  dateJour: FormControl<FichePresenceFormRawValue['dateJour']>;
  description: FormControl<FichePresenceFormRawValue['description']>;
  codeEvenement: FormControl<FichePresenceFormRawValue['codeEvenement']>;
  codeTypeEvenement: FormControl<FichePresenceFormRawValue['codeTypeEvenement']>;
};

export type FichePresenceFormGroup = FormGroup<FichePresenceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FichePresenceFormService {
  createFichePresenceFormGroup(fichePresence: FichePresenceFormGroupInput = { id: null }): FichePresenceFormGroup {
    const fichePresenceRawValue = this.convertFichePresenceToFichePresenceRawValue({
      ...this.getFormDefaults(),
      ...fichePresence,
    });
    return new FormGroup<FichePresenceFormGroupContent>({
      id: new FormControl(
        { value: fichePresenceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      libelle: new FormControl(fichePresenceRawValue.libelle, {
        validators: [Validators.required],
      }),
      dateJour: new FormControl(fichePresenceRawValue.dateJour, {
        validators: [Validators.required],
      }),
      description: new FormControl(fichePresenceRawValue.description),
      codeEvenement: new FormControl(fichePresenceRawValue.codeEvenement, {
        validators: [Validators.required],
      }),
      codeTypeEvenement: new FormControl(fichePresenceRawValue.codeTypeEvenement, {
        validators: [Validators.required],
      }),
    });
  }

  getFichePresence(form: FichePresenceFormGroup): IFichePresence | NewFichePresence {
    return this.convertFichePresenceRawValueToFichePresence(form.getRawValue() as FichePresenceFormRawValue | NewFichePresenceFormRawValue);
  }

  resetForm(form: FichePresenceFormGroup, fichePresence: FichePresenceFormGroupInput): void {
    const fichePresenceRawValue = this.convertFichePresenceToFichePresenceRawValue({ ...this.getFormDefaults(), ...fichePresence });
    form.reset(
      {
        ...fichePresenceRawValue,
        id: { value: fichePresenceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FichePresenceFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateJour: currentTime,
    };
  }

  private convertFichePresenceRawValueToFichePresence(
    rawFichePresence: FichePresenceFormRawValue | NewFichePresenceFormRawValue,
  ): IFichePresence | NewFichePresence {
    return {
      ...rawFichePresence,
      dateJour: dayjs(rawFichePresence.dateJour, DATE_TIME_FORMAT),
    };
  }

  private convertFichePresenceToFichePresenceRawValue(
    fichePresence: IFichePresence | (Partial<NewFichePresence> & FichePresenceFormDefaults),
  ): FichePresenceFormRawValue | PartialWithRequiredKeyOf<NewFichePresenceFormRawValue> {
    return {
      ...fichePresence,
      dateJour: fichePresence.dateJour ? fichePresence.dateJour.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
