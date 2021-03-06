import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Game } from './game.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class GameService {

    private resourceUrl = 'api/games';

    constructor(private http: Http) { }

    create(game: Game): Observable<Game> {
        const copy = this.convert(game);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(game: Game): Game {
        const copy: Game = Object.assign({}, game);
        return copy;
    }

    find(id: number): Observable<Game> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    addPlayer(gameID: string, playerID: string): Observable<ResponseWrapper> {
        const body = {id: gameID, creatorID: playerID};
        return this.http.post(this.resourceUrl + '/add-player', body).map((res: Response) => {
            return res.json();
        });
    }
}
