export class Attendance {
  id: number;
  lessonId: number;
  studentId: number;
  present: boolean;

  constructor(lessonId: number, studentId: number) {
    this.lessonId = lessonId;
    this.studentId = studentId;
  }
}
