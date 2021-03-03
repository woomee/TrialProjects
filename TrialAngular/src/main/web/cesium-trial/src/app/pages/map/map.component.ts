import { Component, OnInit } from '@angular/core';
import { Viewer } from 'cesium';
import { CesiumComponent } from 'src/app/shared/cesium/cesium.component';

declare var Cesium:any;

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit {

  cesiumInitOption="initOption from Map Component";

  cesiumComp: CesiumComponent = new CesiumComponent();

  constructor() { }

  ngOnInit(): void {
  }

  onInitCesium(cesium: CesiumComponent) {
    this.cesiumComp = cesium;

    this.cesiumComp.cesiumViewer.camera.flyTo({
      destination : Cesium.Cartesian3.fromDegrees(-117.16, 32.71, 15000.0)
    })
  }

  onInitViewer(cesiumViewer: Viewer) {
    cesiumViewer.camera.flyTo({
      destination : Cesium.Cartesian3.fromDegrees(-117.16, 32.71, 15000.0)
    });
  }

}
