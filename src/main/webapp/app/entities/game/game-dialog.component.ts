import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Game } from './game.model';
import { GamePopupService } from './game-popup.service';
import { GameService } from './game.service';
import { Question, QuestionService } from '../question';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-game-dialog',
    templateUrl: './game-dialog.component.html'
})
export class GameDialogComponent implements OnInit {

    game: Game;
    authorities: any[];
    isSaving: boolean;

    questions: Question[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private gameService: GameService,
        private questionService: QuestionService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.questionService.query()
            .subscribe((res: ResponseWrapper) => { this.questions = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.game.id !== undefined) {
            this.subscribeToSaveResponse(
                this.gameService.update(this.game), false);
        } else {
            this.subscribeToSaveResponse(
                this.gameService.create(this.game), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<Game>, isCreated: boolean) {
        result.subscribe((res: Game) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Game, isCreated: boolean) {
        this.alertService.success(
            isCreated ? `A new Game is created with identifier ${result.id}`
            : `A Game is updated with identifier ${result.id}`,
            null, null);

        this.eventManager.broadcast({ name: 'gameListModification', content: 'OK'});
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

    trackQuestionById(index: number, item: Question) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-game-popup',
    template: ''
})
export class GamePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private gamePopupService: GamePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.gamePopupService
                    .open(GameDialogComponent, params['id']);
            } else {
                this.modalRef = this.gamePopupService
                    .open(GameDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
