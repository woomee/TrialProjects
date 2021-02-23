import { Directive, ElementRef, OnInit } from '@angular/core';

declare var Cesium:any;

@Directive({
  selector: '[appCesium]'
})
export class CesiumDirective implements OnInit {

  constructor(private el: ElementRef) { }

  ngOnInit() {
    const viewer = new Cesium.Viewer(this.el.nativeElement);

    /* 警告を消すため */
    viewer.infoBox.frame.sandbox = "allow-same-origin allow-top-navigation allow-pointer-lock allow-popups allow-forms allow-scripts";
    viewer.infoBox.frame.src = "";
    // viewer.infoBox.frame.removeAttribute("sandbox");
  }

}
