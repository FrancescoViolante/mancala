import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class BoardService {

  constructor(private httpClient: HttpClient) { }

  createNewGame() : Observable<any>{


    return this.httpClient.get(environment.backendURL+"actions/new-game");
  }
}
