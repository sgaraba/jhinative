import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 17700,
  login: 'y66R',
};

export const sampleWithPartialData: IUser = {
  id: 2380,
  login: 'EfN+m@rPzE\\Vir3YL',
};

export const sampleWithFullData: IUser = {
  id: 28275,
  login: '4.m4A@Q2g\\uw68Xv2',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
