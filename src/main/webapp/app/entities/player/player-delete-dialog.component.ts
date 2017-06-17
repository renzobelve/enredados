import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AlertService, EventManager } from 'ng-jhipster';

import { Player } from './player.model';
import { PlayerPopupService } from './player-popup.service';
import { PlayerService } from './player.service';

@Component({
    selector: 'jhi-player-delete-dialog',
    templateUrl: './player-delete-dialog.component.html'
})
export class PlayerDeleteDialogComponent {

    player: Player;

    constructor(
        private playerService: PlayerService,
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.playerService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'playerListModification',
                content: 'Deleted an player'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success(`A Player is deleted with identifier ${id}`, null, null);
    }
}

@Component({
    selector: 'jhi-player-delete-popup',
    template: ''
})
export class PlayerDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private playerPopupService: PlayerPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.playerPopupService
                .open(PlayerDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
