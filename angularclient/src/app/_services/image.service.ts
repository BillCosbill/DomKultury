import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {catchError} from 'rxjs/operators';
import {throwError} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ImageService {

  constructor(private http: HttpClient) {
  }

  public saveImage(uploadImageData) {
    return this.http.post('http://localhost:8081/uploadFile', uploadImageData, {observe: 'response'}).pipe(
      catchError(this.handleError)
    );
  }

  public getImage(imageId) {
    return this.http.get('http://localhost:8081/downloadFile/' + imageId).pipe(
      catchError(this.handleError)
    );
  }

  handleError(error: HttpErrorResponse) {
    return throwError(error);
  }

}
