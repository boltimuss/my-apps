import { Component, OnInit, ChangeDetectionStrategy, Input, ElementRef, HostBinding } from '@angular/core';
import Map from 'ol/Map';
import { MessageBusService } from 'src/app/services/message-bus.service';
import { Subscriber } from 'src/app/services/message-bus.service';

@Component({
  selector: 'app-map',
  template: '',
  styles: [':host { width: 100%; height: 100%; }',
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class MapComponent implements OnInit,Subscriber {

  @HostBinding('style.width')
  width:Object = '100%';

  @Input() map: Map;
  constructor(private elementRef: ElementRef) {
    this.map = new Map({});
  }
  onMessageReceived(message: Object): void {
    this.width = message;
  }
  
  ngOnInit() {
    this.map.setTarget(this.elementRef.nativeElement);
    MessageBusService.subscribe("shrinkMap", this);
  }
}