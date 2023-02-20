import { Component, ElementRef, OnInit, ViewChild  } from '@angular/core';
import Map from 'ol/Map';
import { MousePosition } from 'ol/control';
import OSM, {ATTRIBUTION} from 'ol/source/OSM';
import TileLayer from 'ol/layer/Tile';
import View from 'ol/View';
import { createStringXY } from 'ol/coordinate';
import { transform } from 'ol/proj';
import { transformExtent } from 'ol/proj';
import { DragPan, Interaction, MouseWheelZoom, Select } from 'ol/interaction';
import PointerInteraction from 'ol/interaction/Pointer';
import { Subscriber } from 'src/app/services/message-bus.service';
import { MessageBusService } from 'src/app/services/message-bus.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, Subscriber {

  @ViewChild('popup') popupDiv: ElementRef | undefined;
  title = 'Harpoon';
  map: Map;
  popTop: number = 0;
  popLeft: number = 0;
  popupVisible:boolean = false;
  mapSize:number = 1;

  mousePositionControl = new MousePosition({
    coordinateFormat: createStringXY(4),
    projection: 'EPSG:4326'
  });

  constructor() 
  {
    this.map = new Map({});
  }

  onClose():void {
    this.hidePopup();
  }

  displayPopup():void {

    let s:String = "display: inline; position: absolute;\
    background-color: red;\
    box-shadow: 0 1px 4px rgba(0,0,0,0.2);\
    padding: 15px;\
    border-radius: 20px;\
    border: 1px solid #cccccc;\
    top: "+this.popTop+"px;\
    left: "+this.popLeft+"px;\
    min-width: 280px;";

    if (this.popupDiv != null) {
      this.popupDiv.nativeElement.style = s;
    }

  }

  hidePopup():void {

    let s:String = "display: none;";

      if (this.popupDiv != null) {
        this.popupDiv.nativeElement.style = s;
      }

      this.popupVisible = false;
  }

  onMessageReceived(message: Object): void {
    if (message == "80%")
    {
      this.mapSize = .8;
    }
    else 
    {
      this.mapSize = 1.0;
    }
  }

  ngOnInit(): void {

    MessageBusService.subscribe("shrinkMap", this);

    this.popTop = 0;
    this.popLeft = 0;
    this.hidePopup();

    this.map = new Map({
      interactions: [new MouseWheelZoom(), new PointerInteraction(), new Select(), new DragPan()],
      view: new View({
        center: [0, 0],
        zoom: 1,
        extent: transformExtent([-180,84,180,-84], 'EPSG:4326', 'EPSG:3857')
      }),
      controls: [this.mousePositionControl],
      layers: [
        new TileLayer({
          preload: Infinity,
          source: new OSM(),
        })
      ],
      target: 'ol-map'
    });

    this.map.on("pointermove", function(evt){
      var lonlat = transform(evt.coordinate, 'EPSG:3857', 'EPSG:4326');
      var lon = lonlat[0];
      var lat = lonlat[1];

    });
    
    this.map.on("dblclick", (evt) =>{
      
      console.log(evt.coordinate);

      if (!this.popupVisible) 
      {
        this.popupVisible = true;
        this.popLeft = this.map.getPixelFromCoordinate(evt.coordinate)[0];
        this.popTop = this.map.getPixelFromCoordinate(evt.coordinate)[1];

        if (this.mapSize == .8)
        {
          this.popLeft += (.2 * window.innerWidth);
        }

        this.displayPopup();
      }

      else 
      {
        return;
      }
      
    });
    
    this.map.on("click", function(evt){
    });
  }
}

