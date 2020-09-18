import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ImageService {

  constructor(private http: HttpClient) {
  }

  public saveImage(uploadImageData) {
    return this.http.post('http://localhost:8081/uploadFile', uploadImageData, {observe: 'response'});
  }

  public getImage(imageId) {
    return this.http.get('http://localhost:8081/downloadFile/' + imageId);
  }

}
