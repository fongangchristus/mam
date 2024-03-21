import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFichePresence } from '../fiche-presence.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../fiche-presence.test-samples';

import { FichePresenceService, RestFichePresence } from './fiche-presence.service';

const requireRestSample: RestFichePresence = {
  ...sampleWithRequiredData,
  dateJour: sampleWithRequiredData.dateJour?.toJSON(),
};

describe('FichePresence Service', () => {
  let service: FichePresenceService;
  let httpMock: HttpTestingController;
  let expectedResult: IFichePresence | IFichePresence[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FichePresenceService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a FichePresence', () => {
      const fichePresence = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(fichePresence).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FichePresence', () => {
      const fichePresence = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(fichePresence).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FichePresence', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FichePresence', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FichePresence', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFichePresenceToCollectionIfMissing', () => {
      it('should add a FichePresence to an empty array', () => {
        const fichePresence: IFichePresence = sampleWithRequiredData;
        expectedResult = service.addFichePresenceToCollectionIfMissing([], fichePresence);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fichePresence);
      });

      it('should not add a FichePresence to an array that contains it', () => {
        const fichePresence: IFichePresence = sampleWithRequiredData;
        const fichePresenceCollection: IFichePresence[] = [
          {
            ...fichePresence,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFichePresenceToCollectionIfMissing(fichePresenceCollection, fichePresence);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FichePresence to an array that doesn't contain it", () => {
        const fichePresence: IFichePresence = sampleWithRequiredData;
        const fichePresenceCollection: IFichePresence[] = [sampleWithPartialData];
        expectedResult = service.addFichePresenceToCollectionIfMissing(fichePresenceCollection, fichePresence);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fichePresence);
      });

      it('should add only unique FichePresence to an array', () => {
        const fichePresenceArray: IFichePresence[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const fichePresenceCollection: IFichePresence[] = [sampleWithRequiredData];
        expectedResult = service.addFichePresenceToCollectionIfMissing(fichePresenceCollection, ...fichePresenceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fichePresence: IFichePresence = sampleWithRequiredData;
        const fichePresence2: IFichePresence = sampleWithPartialData;
        expectedResult = service.addFichePresenceToCollectionIfMissing([], fichePresence, fichePresence2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fichePresence);
        expect(expectedResult).toContain(fichePresence2);
      });

      it('should accept null and undefined values', () => {
        const fichePresence: IFichePresence = sampleWithRequiredData;
        expectedResult = service.addFichePresenceToCollectionIfMissing([], null, fichePresence, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fichePresence);
      });

      it('should return initial array if no FichePresence is added', () => {
        const fichePresenceCollection: IFichePresence[] = [sampleWithRequiredData];
        expectedResult = service.addFichePresenceToCollectionIfMissing(fichePresenceCollection, undefined, null);
        expect(expectedResult).toEqual(fichePresenceCollection);
      });
    });

    describe('compareFichePresence', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFichePresence(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFichePresence(entity1, entity2);
        const compareResult2 = service.compareFichePresence(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFichePresence(entity1, entity2);
        const compareResult2 = service.compareFichePresence(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFichePresence(entity1, entity2);
        const compareResult2 = service.compareFichePresence(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
