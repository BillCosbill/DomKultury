import {Role} from './role';

export class User {
  id: number;
  username: string;
  firstName: string;
  lastName: string;
  pesel: string;
  email: string;
  password: string;
  roles: Role[];
  enable: boolean;
  subjects: number[];
}
