import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Level } from './level.model';
import { LevelPopupService } from './level-popup.service';
import { LevelService } from './level.service';

@Component({
    selector: 'jhi-level-dialog',
    templateUrl: './level-dialog.component.html'
})
export class LevelDialogComponent implements OnInit {

    level: Level;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private levelService: LevelService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.level.id !== undefined) {
            this.subscribeToSaveResponse(
                this.levelService.update(this.level), false);
        } else {
            this.subscribeToSaveResponse(
                this.levelService.create(this.level), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<Level>, isCreated: boolean) {
        result.subscribe((res: Level) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Level, isCreated: boolean) {
        this.alertService.success(
            isCreated ? `A new Level is created with identifier ${result.id}`
            : `A Level is updated with identifier ${result.id}`,
            null, null);

        this.eventManager.broadcast({ name: 'levelListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-level-popup',
    template: ''
})
export class LevelPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private levelPopupService: LevelPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.levelPopupService
                    .open(LevelDialogComponent, params['id']);
            } else {
                this.modalRef = this.levelPopupService
                    .open(LevelDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
