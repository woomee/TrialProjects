import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CesiumDirective } from './cesium.directive';
import { MapComponent } from './pages/map/map.component';
import { CesiumComponent } from './shared/cesium/cesium.component';

@NgModule({
  declarations: [
    AppComponent,
    CesiumDirective,
    MapComponent,
    CesiumComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
