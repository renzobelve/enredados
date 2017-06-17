import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, ParseLinks, PaginationUtil, AlertService } from 'ng-jhipster';

import { GamePlayer } from './game-player.model';
import { GamePlayerService } from './game-player.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-game-player',
    templateUrl: './game-player.component.html'
})
export class GamePlayerComponent implements OnInit, OnDestroy {
gamePlayers: GamePlayer[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private gamePlayerService: GamePlayerService,
        private alertService: AlertService,
        private eventManager: EventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.gamePlayerService.query().subscribe(
            (res: ResponseWrapper) => {
                this.gamePlayers = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInGamePlayers();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: GamePlayer) {
        return item.id;
    }
    registerChangeInGamePlayers() {
        this.eventSubscriber = this.eventManager.subscribe('gamePlayerListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
