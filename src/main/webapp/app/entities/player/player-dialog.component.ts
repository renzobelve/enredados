import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Player } from './player.model';
import { PlayerPopupService } from './player-popup.service';
import { PlayerService } from './player.service';
import { User, UserService } from '../../shared';
import { Level, LevelService } from '../level';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-player-dialog',
    templateUrl: './player-dialog.component.html'
})
export class PlayerDialogComponent implements OnInit {

    player: Player;
    authorities: any[];
    isSaving: boolean;

    users: User[];

    levels: Level[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private playerService: PlayerService,
        private userService: UserService,
        private levelService: LevelService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.levelService.query()
            .subscribe((res: ResponseWrapper) => { this.levels = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.player.id !== undefined) {
            this.subscribeToSaveResponse(
                this.playerService.update(this.player), false);
        } else {
            this.subscribeToSaveResponse(
                this.playerService.create(this.player), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<Player>, isCreated: boolean) {
        result.subscribe((res: Player) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Player, isCreated: boolean) {
        this.alertService.success(
            isCreated ? `A new Player is created with identifier ${result.id}`
            : `A Player is updated with identifier ${result.id}`,
            null, null);

        this.eventManager.broadcast({ name: 'playerListModification', content: 'OK'});
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

    trackUserById(index: number, item: User) {
        return item.id;
    }

    trackLevelById(index: number, item: Level) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-player-popup',
    template: ''
})
export class PlayerPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private playerPopupService: PlayerPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.playerPopupService
                    .open(PlayerDialogComponent, params['id']);
            } else {
                this.modalRef = this.playerPopupService
                    .open(PlayerDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
