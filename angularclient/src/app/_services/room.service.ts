import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {Room} from '../_model/room';
import {Lesson} from '../_model/lesson';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RoomService {

  private roomUrl: string;

  constructor(private http: HttpClient) {
    this.roomUrl = environment.apiAddress + '/rooms';
  }

  public findAll(): Observable<Room[]> {
    return this.http.get<Room[]>(this.roomUrl).pipe(
      catchError(this.handleError)
    );
  }

  public getRoomLessons(id: number) {
    return this.http.get<Lesson[]>(this.roomUrl + '/' + id + '/lessons').pipe(
      catchError(this.handleError)
    );
  }

  public addRoom(room: Room, imageId: string) {
    return this.http.post<Room>(this.roomUrl + '?imageId=' + imageId, room).pipe(
      catchError(this.handleError)
    );
  }

  public getRoom(id: number) {
    return this.http.get<Room>(this.roomUrl + '/' + id).pipe(
      catchError(this.handleError)
    );
  }

  public deleteRoom(id: number) {
    return this.http.delete<Room>(this.roomUrl + '/' + id).pipe(
      catchError(this.handleError)
    );
  }

  public deleteImageFromRoom(id: number) {
    return this.http.patch(this.roomUrl + '/' + id + '/deleteImage', null).pipe(
      catchError(this.handleError)
    );
  }

  public updateRoom(room: Room, id: number, imageId: string) {
    return this.http.put<Room>(this.roomUrl + '/' + id + '?imageId=' + imageId, room).pipe(
      catchError(this.handleError)
    );
  }

  handleError(error: HttpErrorResponse) {
    return throwError(error);
  }

}
