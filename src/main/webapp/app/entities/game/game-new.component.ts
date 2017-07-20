import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {EventManager, ParseLinks, PaginationUtil, AlertService} from 'ng-jhipster';
import {Observable} from 'rxjs/Rx';
import {Response} from '@angular/http';

import {Game} from './game.model';
import {GameService} from './game.service';
import {ITEMS_PER_PAGE, Principal, ResponseWrapper, User} from '../../shared';
import {PaginationConfig} from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-game-new',
    templateUrl: './game-new.component.html'
})
export class GameNewComponent implements OnInit {

    user: User;
    game: Game;
    authorities: any[];
    isSaving: boolean;

    constructor(
        private gameService: GameService,
        private alertService: AlertService,
        private eventManager: EventManager,
        private principal: Principal,
        private router: Router,
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.game = new Game();
        this.principal.identity().then((user) => {
            this.user = user;
        });
    }

    save() {
        this.isSaving = true;
        this.game.creatorID = this.user.id;
        this.subscribeToSaveResponse(this.gameService.create(this.game), true);
    }

    private subscribeToSaveResponse(result: Observable<Game>, isCreated: boolean) {
        result.subscribe((res: Game) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Game, isCreated: boolean) {
        this.alertService.success(
            isCreated ? `Se ha creado un nuevo juego: ${result.id}`
                : `Se ha modificado un juego: ${result.id}`,
            null, null);

        this.isSaving = false;
        this.router.navigate(['/game-waiting', result.id]);
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
