import {Component, OnInit} from '@angular/core';
import * as mapboxgl from 'mapbox-gl';

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.css']
})
export class ContactComponent implements OnInit {

  map: mapboxgl.Map;
  style = 'mapbox://styles/mapbox/streets-v11';
  lat = 53.116847;
  lng = 23.146574;

  constructor() {
  }

  ngOnInit(): void {
    Object.getOwnPropertyDescriptor(mapboxgl, 'accessToken').set('pk.eyJ1IjoiYmlsbGNvc2J5IiwiYSI6ImNrOXBtbDI1ODBidjUzZm1tdHNibHlkcm0ifQ.hlcWkDT_XaeP3t1S5Hyvyw');
    this.map = new mapboxgl.Map({
      container: 'map',
      style: this.style,
      zoom: 13,
      center: [this.lng, this.lat]
    });
    // Add map controls
    this.map.addControl(new mapboxgl.NavigationControl());


    new mapboxgl.Marker()
      .setLngLat([this.lng, this.lat])
      .addTo(this.map);
  }

}
