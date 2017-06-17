import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AlertService, EventManager } from 'ng-jhipster';

import { Level } from './level.model';
import { LevelPopupService } from './level-popup.service';
import { LevelService } from './level.service';

@Component({
    selector: 'jhi-level-delete-dialog',
    templateUrl: './level-delete-dialog.component.html'
})
export class LevelDeleteDialogComponent {

    level: Level;

    constructor(
        private levelService: LevelService,
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.levelService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'levelListModification',
                content: 'Deleted an level'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success(`A Level is deleted with identifier ${id}`, null, null);
    }
}

@Component({
    selector: 'jhi-level-delete-popup',
    template: ''
})
export class LevelDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private levelPopupService: LevelPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.levelPopupService
                .open(LevelDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
