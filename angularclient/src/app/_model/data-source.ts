export class DataSource {
  Subject: string;
  StartTime: any;
  EndTime: any;


  constructor(Subject: string, StartTime: any, EndTime: any) {
    this.Subject = Subject;
    this.StartTime = StartTime;
    this.EndTime = EndTime;
  }
}
