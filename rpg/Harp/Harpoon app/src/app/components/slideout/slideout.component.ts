import { Component, OnInit } from '@angular/core';
import { MessageBusService } from 'src/app/services/message-bus.service';
import { Subscriber } from 'src/app/services/message-bus.service';

@Component({
  selector: 'app-slideout',
  templateUrl: './slideout.component.html',
  styleUrls: ['./slideout.component.css']
})
export class SlideoutComponent implements OnInit, Subscriber {

  private sidePanelToggle:boolean = false;

  constructor() { }

  onMessageReceived(message: Object): void {
    this.toggleSidebarMenu();
  }
  
  toggleSidebarMenu(): void {
    this.sidePanelToggle = !this.sidePanelToggle;
    if (this.sidePanelToggle)
    {
      MessageBusService.sendMessage("shrinkMap", "80%");
    }
    else 
    {
      MessageBusService.sendMessage("shrinkMap", "100%");
    }
  }

  ngOnInit(): void {
    MessageBusService.subscribe("toggleSidePanel", this);
  }

}
