import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '4ce379e7-32e1-48e0-8fe2-c62793b6329f',
};

export const sampleWithPartialData: IAuthority = {
  name: 'eea88fce-7fe1-405a-a501-4b0c0ba00d45',
};

export const sampleWithFullData: IAuthority = {
  name: 'e85a3d16-441b-4d47-8dd8-5d176afab118',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
