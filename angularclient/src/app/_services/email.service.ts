import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {catchError} from 'rxjs/operators';
import {EmailMessage} from '../_model/email-message';
import {throwError} from 'rxjs';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class EmailService {

  private emailUrl: string;

  constructor(private http: HttpClient) {
    this.emailUrl = environment.apiAddress + '/email';
  }

  public sendEmail(emailMessage: EmailMessage) {
    return this.http.post<EmailMessage>(this.emailUrl + '/send', emailMessage).pipe(
      catchError(this.handleError)
    );
  }

  public sendMultipleMails(emailMessage: EmailMessage) {
    return this.http.post<EmailMessage>(this.emailUrl + '/sendMultiple', emailMessage).pipe(
      catchError(this.handleError)
    );
  }

  handleError(error: HttpErrorResponse) {
    return throwError(error);
  }
}
