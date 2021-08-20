import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class BoardService {

  constructor(private httpClient: HttpClient) { }

  private _model: NewGameRequestModel ;


  createNewGame(model: NewGameRequestModel) : Observable<GameDto>{
    return this.httpClient.post<GameDto>(environment.backendURL+"mancala/new-game", model);
  }

  moveStones(model: MovePitRequestModel) : Observable<GameDto>{
    return this.httpClient.post<GameDto>(environment.backendURL+"mancala/move-stones", model);
  }


  getModel(): NewGameRequestModel {
    return this._model;
  }

  setModel(value: NewGameRequestModel) {
    this._model = value;
  }
}
