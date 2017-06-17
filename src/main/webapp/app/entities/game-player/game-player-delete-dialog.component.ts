import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AlertService, EventManager } from 'ng-jhipster';

import { GamePlayer } from './game-player.model';
import { GamePlayerPopupService } from './game-player-popup.service';
import { GamePlayerService } from './game-player.service';

@Component({
    selector: 'jhi-game-player-delete-dialog',
    templateUrl: './game-player-delete-dialog.component.html'
})
export class GamePlayerDeleteDialogComponent {

    gamePlayer: GamePlayer;

    constructor(
        private gamePlayerService: GamePlayerService,
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.gamePlayerService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'gamePlayerListModification',
                content: 'Deleted an gamePlayer'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success(`A Game Player is deleted with identifier ${id}`, null, null);
    }
}

@Component({
    selector: 'jhi-game-player-delete-popup',
    template: ''
})
export class GamePlayerDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private gamePlayerPopupService: GamePlayerPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.gamePlayerPopupService
                .open(GamePlayerDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
