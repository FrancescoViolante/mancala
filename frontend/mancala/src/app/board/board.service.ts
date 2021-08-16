import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class BoardService {

  constructor(private httpClient: HttpClient) { }

  createNewGame() : Observable<GameDto>{
    return this.httpClient.get<GameDto>(environment.backendURL+"mancala/new-game");
  }

  moveStones(model: MovePitRequestModel) : Observable<GameDto>{
    return this.httpClient.post<GameDto>(environment.backendURL+"mancala/move-stones", model);
  }
}
