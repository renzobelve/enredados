import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { GamePlayer } from './game-player.model';
import { GamePlayerService } from './game-player.service';

@Component({
    selector: 'jhi-game-player-detail',
    templateUrl: './game-player-detail.component.html'
})
export class GamePlayerDetailComponent implements OnInit, OnDestroy {

    gamePlayer: GamePlayer;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private gamePlayerService: GamePlayerService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInGamePlayers();
    }

    load(id) {
        this.gamePlayerService.find(id).subscribe((gamePlayer) => {
            this.gamePlayer = gamePlayer;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInGamePlayers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'gamePlayerListModification',
            (response) => this.load(this.gamePlayer.id)
        );
    }
}
