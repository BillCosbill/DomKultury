import {Student} from './student';

export class EmailMessage {
  fromId: number;
  to: string;
  subject: string;
  text: string;
  studentList: Student[];
}
