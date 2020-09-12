import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ImageService {

  private imageUrl: string;


  constructor(private http: HttpClient) {
    this.imageUrl = 'http://localhost:8081/image';
  }

  public saveImage(uploadImageData) {
    return this.http.post(this.imageUrl + '/upload', uploadImageData, {observe: 'response'});
  }

  public getImage(imageName) {
    return this.http.get(this.imageUrl + '/get/' + imageName);
  }

}
