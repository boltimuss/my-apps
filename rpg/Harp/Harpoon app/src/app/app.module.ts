import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { MapComponent } from './components/map/map.component';
import { SlideoutComponent } from './components/slideout/slideout.component';
import { MessageBusService } from './services/message-bus.service';

@NgModule({
  declarations: [
    AppComponent,
    MapComponent,
    SlideoutComponent
  ],
  imports: [
    BrowserModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
