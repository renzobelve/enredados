import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { GamePlayer } from './game-player.model';
import { GamePlayerPopupService } from './game-player-popup.service';
import { GamePlayerService } from './game-player.service';
import { Player, PlayerService } from '../player';
import { Game, GameService } from '../game';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-game-player-dialog',
    templateUrl: './game-player-dialog.component.html'
})
export class GamePlayerDialogComponent implements OnInit {

    gamePlayer: GamePlayer;
    authorities: any[];
    isSaving: boolean;

    players: Player[];

    games: Game[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private gamePlayerService: GamePlayerService,
        private playerService: PlayerService,
        private gameService: GameService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.playerService.query()
            .subscribe((res: ResponseWrapper) => { this.players = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.gameService.query()
            .subscribe((res: ResponseWrapper) => { this.games = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.gamePlayer.id !== undefined) {
            this.subscribeToSaveResponse(
                this.gamePlayerService.update(this.gamePlayer), false);
        } else {
            this.subscribeToSaveResponse(
                this.gamePlayerService.create(this.gamePlayer), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<GamePlayer>, isCreated: boolean) {
        result.subscribe((res: GamePlayer) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: GamePlayer, isCreated: boolean) {
        this.alertService.success(
            isCreated ? `A new Game Player is created with identifier ${result.id}`
            : `A Game Player is updated with identifier ${result.id}`,
            null, null);

        this.eventManager.broadcast({ name: 'gamePlayerListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackPlayerById(index: number, item: Player) {
        return item.id;
    }

    trackGameById(index: number, item: Game) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-game-player-popup',
    template: ''
})
export class GamePlayerPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private gamePlayerPopupService: GamePlayerPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.gamePlayerPopupService
                    .open(GamePlayerDialogComponent, params['id']);
            } else {
                this.modalRef = this.gamePlayerPopupService
                    .open(GamePlayerDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
