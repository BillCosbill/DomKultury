import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {catchError} from 'rxjs/operators';
import {throwError} from 'rxjs';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ImageService {

  constructor(private http: HttpClient) {
  }

  public saveImage(uploadImageData) {
    return this.http.post(environment.apiAddress + '/uploadFile', uploadImageData, {observe: 'response'}).pipe(
      catchError(this.handleError)
    );
  }

  public getImage(imageId) {
    return this.http.get(environment.apiAddress + '/downloadFile/' + imageId).pipe(
      catchError(this.handleError)
    );
  }

  handleError(error: HttpErrorResponse) {
    return throwError(error);
  }

}
