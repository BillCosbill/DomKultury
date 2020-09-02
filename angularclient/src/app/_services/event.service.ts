import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {Event} from '../_model/event';
import {catchError} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class EventService {

  private eventUrl: string;


  constructor(private http: HttpClient) {
    this.eventUrl = 'http://localhost:8081/events';
  }

  public findAll(): Observable<Event[]> {
    return this.http.get<Event[]>(this.eventUrl).pipe(
      catchError(this.handleError)
    );
  }

  public getEvent(id: number) {
    return this.http.get<Event>(this.eventUrl + '/' + id).pipe(
      catchError(this.handleError)
    );
  }

  public deleteEvent(id: number) {
    return this.http.delete<Event>(this.eventUrl + '/' + id).pipe(
      catchError(this.handleError)
    );
  }

  public addUserToEvent(event: Event, userId: number, eventId: number) {
    return this.http.patch<Event>(this.eventUrl + '/' + eventId + '/addUser/' + userId, event).pipe(
      catchError(this.handleError)
    );
  }

  public deleteUserFromEvent(event: Event, userId: number, eventId: number) {
    return this.http.patch<Event>(this.eventUrl + '/' + eventId + '/deleteUser/' + userId, event).pipe(
      catchError(this.handleError)
    );
  }

  //TODO zrobić jakieś popapy z errorami i z udanymi akcjami np addUserToEvent
  //TODO dodać obsługę błędów w innych serwisach !!!!!!!!!!!!!!!!
  handleError(error: HttpErrorResponse) {
    console.log(error.error.message);
    return throwError(error);
  }
}
