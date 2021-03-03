import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Viewer } from 'cesium';


declare var Cesium:any;

@Component({
  selector: 'app-cesium',
  templateUrl: './cesium.component.html',
  styleUrls: ['./cesium.component.scss']
})
export class CesiumComponent implements OnInit {

  @Input() initOption: string = "";
  @Output() initViewerEvent = new EventEmitter<Viewer>();
  
  public cesiumViewer;

  constructor() { }

  ngOnInit(): void {

    this.cesiumViewer = new Cesium.Viewer("cesiumContainer");

    /* 警告を消すため */
    this.cesiumViewer.infoBox.frame.sandbox = "allow-same-origin allow-top-navigation allow-pointer-lock allow-popups allow-forms allow-scripts";
    this.cesiumViewer.infoBox.frame.src = "";
    // viewer.infoBox.frame.removeAttribute("sandbox");

    console.log("CesiumComponent initOption=" + this.initOption);

    /* 初期化が完了したら親コンポーネントへViewerを通知する */
    this.initViewerEvent.emit(this.cesiumViewer);
  }
}
