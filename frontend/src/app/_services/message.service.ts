import { Injectable, OnInit, EventEmitter } from "@angular/core";

@Injectable()
export class MessageService {
    errorEvent: EventEmitter<string>;
    
    constructor() { 
        this.errorEvent = new EventEmitter<string>();        
    }
}